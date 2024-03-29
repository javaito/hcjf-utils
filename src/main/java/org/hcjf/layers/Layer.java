package org.hcjf.layers;

import org.hcjf.errors.HCJFSecurityException;
import org.hcjf.layers.plugins.PluginLayer;
import org.hcjf.service.ServiceSession;
import org.hcjf.service.ServiceThread;
import org.hcjf.service.security.Permission;
import org.hcjf.service.security.SecurityPermissions;
import org.hcjf.utils.SynchronizedCountOperation;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * All the layer implementation extends this class, and this class is a proxy
 * between the layer client and implementation.
 * @author javaito
 */
public abstract class Layer implements LayerInterface {

    private final String implName;
    private final boolean stateful;
    private final SynchronizedCountOperation invocationMean;
    private final SynchronizedCountOperation executionTimeMean;
    private final SynchronizedCountOperation errorMean;

    /**
     * This is the end point for all the layer constructor.
     * @param implName Implementation name.
     * @param stateful Stateful status.
     */
    public Layer(String implName, boolean stateful) {
        this.implName = implName;
        this.stateful = stateful;
        this.invocationMean = new SynchronizedCountOperation(
                SynchronizedCountOperation.getMeanOperation(), 1000L);
        this.executionTimeMean = new SynchronizedCountOperation(
                SynchronizedCountOperation.getMeanOperation(), 1000L);
        this.errorMean = new SynchronizedCountOperation(
                SynchronizedCountOperation.getMeanOperation(), 1000L);
    }

    public Layer(String implName) {
        this(implName, true);
    }

    public Layer(boolean stateful) {
        this(null, stateful);
    }

    public Layer(){
        this(null, true);
    }

    /**
     * Return the layer implementation name.
     * @return Layer implementation name.
     */
    @Override
    public String getImplName() {
        return implName;
    }

    /**
     * Return if the layer is stateful or not.
     * @return Stateful
     */
    @Override
    public final boolean isStateful() {
        return stateful;
    }

    /**
     * Returns true if the layer implementations is an instanceof plugin or false in otherwise.
     * @return Plugin status.
     */
    @Override
    public  final boolean isPlugin() {
        return PluginLayer.class.isAssignableFrom(getClass());
    }

    /**
     * Return the string set with all the aliases for this implementation.
     * @return Aliases for this implementation.
     */
    public Set<String> getAliases() {
        return null;
    }

    /**
     * This method return true if the layer instance is overwritable for other instance
     * with the same name.
     * @return True if the layer is overwritable or false in otherwise.
     */
    public boolean isOverwritable() {
        return true;
    }

    /**
     * This method must be overrides to add restrictions over particular
     * implementations of the layers.
     * @return Access object.
     */
    protected Access checkAccess(){
        return Access.GRANTED;
    }

    /**
     * Delegation method to get some layer implementation.
     * @param layerClass Layer implementation class.
     * @param implName Layer implementation name.
     * @param <L> Expected layer implementation class.
     * @return Layer implementation.
     */
    protected final <L extends LayerInterface> L getLayer(Class<? extends L> layerClass, String implName) {
        return Layers.get(layerClass, implName);
    }

    /**
     * Return the layer proxy of the layer or null by default.
     * @return Layer proxy instance.
     */
    public LayerProxy getProxy() {
        return new LayerProxy() {
            @Override
            public ProxyInterceptor onBeforeInvoke(Method method, Object... params) {return null;}

            @Override
            public void onAfterInvoke(Method method, Object result, Object... params) {}
        };
    }

    /**
     * Verify if the current thread is working between the normal parameters.
     * @throws Throwable Any throwable throws for some check method.
     */
    private void analyzeThread() throws Throwable {
        ServiceThread.checkInterruptedThread();
    }

    /**
     * This method intercepts the call to layer implementation and
     * save some information about the thread behavior.
     * @param proxy Object to be called.
     * @param method Method to be called.
     * @param args Method to invoke the method.
     * @return Return the value returned for the proxy method.
     * @throws Throwable Throw all the generated exceptions.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        //Add one into the executions counter.
        invocationMean.add(1);

        //Store the start time of the execution.
        Long startTime = System.currentTimeMillis();

        try {
            analyzeThread();

            Access access = checkAccess();

            if (access == null) {
                throw new HCJFSecurityException("Access null");
            }
            if (!access.granted) {
                if (access.message != null && access.getThrowable() != null) {
                    throw new HCJFSecurityException(access.getMessage(), access.getThrowable());
                } else if (access.getMessage() != null) {
                    throw new HCJFSecurityException(access.getMessage());
                } else if (access.getThrowable() != null) {
                    throw new HCJFSecurityException("Empty message", access.getThrowable());
                }
            }

            ServiceThread serviceThread = ServiceThread.getServiceThreadInstance();
            serviceThread.putLayer(new ServiceSession.LayerStackElement(
                    getClass().getName(), getImplName(), isPlugin(), isStateful()));

            if (!method.getDeclaringClass().equals(LayerInterface.class)) {
                Method implementationMethod = getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
                for (Permission permission : implementationMethod.getDeclaredAnnotationsByType(Permission.class)) {
                    SecurityPermissions.checkPermission(getTarget().getClass(), permission.value());
                }
            }

            try {
                Object[] newArgs = AdaptableLayer.class.isAssignableFrom(getClass()) ?
                        ((AdaptableLayer)this).adaptArguments(method, args) : args;
                LayerProxy.ProxyInterceptor interceptor = getProxy().onBeforeInvoke(method, newArgs);
                if (interceptor == null || !interceptor.isCached()) {
                    result = method.invoke(getTarget(), newArgs);
                } else {
                    result = interceptor.getResult();
                }
                getProxy().onAfterInvoke(method, result, newArgs);
            } catch (Throwable throwable) {
                //Add one to the error mean counter.
                errorMean.add(1);
                throw throwable;
            } finally {
                if (serviceThread != null) {
                    serviceThread.removeLayer();
                }
            }
        } finally {
            //Add the invocation time int the layer counter.
            executionTimeMean.add(System.currentTimeMillis() - startTime);
        }
        return result;
    }

    /**
     * This method return the invocation target.
     * @return Invocation target.
     */
    protected Object getTarget() {
        return this;
    }

    /**
     * Return the session associated to the execution thread.
     * @return Service session.
     */
    protected final ServiceSession getSession() {
        return ServiceThread.getServiceThreadInstance().getSession();
    }

    /**
     * This class represents the access resume of the layer.
     */
    public final static class Access {

        private static final Access GRANTED = new Access(true);

        private final boolean granted;
        private final String message;
        private final Throwable throwable;

        public Access(boolean granted, String message, Throwable throwable) {
            this.granted = granted;
            this.message = message;
            this.throwable = throwable;
        }

        public Access(boolean granted, String message) {
            this(granted, message, null);
        }

        public Access(boolean granted) {
            this(granted, null, null);
        }

        /**
         * It returns true if access has been granted and false otherwise.
         * @return Granted resume.
         */
        public boolean isGranted() {
            return granted;
        }

        /**
         * Return a message associated to the granted resume.
         * @return Message.
         */
        public String getMessage() {
            return message;
        }

        /**
         * Return the throwable associated to the granted resume.
         * @return Throwable.
         */
        public Throwable getThrowable() {
            return throwable;
        }
    }

}
