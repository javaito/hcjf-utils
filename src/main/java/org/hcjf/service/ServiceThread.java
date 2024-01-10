package org.hcjf.service;

import com.sun.management.ThreadMXBean;
import org.hcjf.errors.HCJFRuntimeException;
import org.hcjf.log.Log;
import org.hcjf.properties.SystemProperties;
import org.hcjf.utils.LruMap;

import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * These are the thread created by the factory in the
 * class service, all the services run over this kind of
 * thread.
 * @author javaito
 */
public class ServiceThread {

    private static final Map<Long,ServiceThread> serviceThreadInstances;

    static {
        serviceThreadInstances = new HashMap<>();
    }

    private final Long threadId;
    private ServiceSession session;
    private Long initialAllocatedMemory;
    private Long maxAllocatedMemory;
    private Long initialTime;
    private Long maxExecutionTime;

    public ServiceThread(ServiceThread serviceThread) {
        this.threadId = serviceThread.threadId;
        this.session = serviceThread.session;
        this.initialAllocatedMemory = serviceThread.initialAllocatedMemory;
        this.maxAllocatedMemory = serviceThread.maxAllocatedMemory;
        this.initialTime = serviceThread.initialTime;
        this.maxExecutionTime = serviceThread.maxExecutionTime;
    }

    public ServiceThread() {
        this.threadId = Thread.currentThread().threadId();
    }

    /**
     * Add an element into the layer stack.
     * @param element Layer stack element.
     */
    public final void putLayer(ServiceSession.LayerStackElement element) {
        getSession().putLayer(element);
    }

    /**
     * Remove the head of the layer stack.
     */
    public final void removeLayer() {
        getSession().removeLayer();
    }

    /**
     * This method return the stack of layer of the session.
     * @return Layer stack.
     */
    public Collection<ServiceSession.LayerStackElement> getLayerStack() {
        return getSession().getLayerStack();
    }

    /**
     * Return the session of the thread.
     * @return Session of the thread.
     */
    public final ServiceSession getSession() {
        return session;
    }

    /**
     * Returns the max allocated memory value for thread.
     * @return Max allocated memory value.
     */
    public final Long getMaxAllocatedMemory() {
        return maxAllocatedMemory;
    }

    /**
     * Sets the max allocated memory value for thread.
     * @param maxAllocatedMemory Max allocated memory value.
     */
    private void setMaxAllocatedMemory(Long maxAllocatedMemory) {
        this.maxAllocatedMemory = maxAllocatedMemory;
    }

    /**
     * Returns the max execution time value for thread.
     * @return Max execution time value.
     */
    public final Long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    /**
     * Sets the max execution time value for thread.
     * @param maxExecutionTime Max execution time value.
     */
    private void setMaxExecutionTime(Long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    /**
     * Returns the initial thread allocated memory counter when the current service session starts.
     * @return Initial thread allocated memory.
     */
    public Long getInitialAllocatedMemory() {
        return initialAllocatedMemory;
    }

    /**
     * Sets the initial thread allocated memory counter.
     * @param initialAllocatedMemory Initial thread allocated memory counter.
     */
    private void setInitialAllocatedMemory(Long initialAllocatedMemory) {
        this.initialAllocatedMemory = initialAllocatedMemory;
    }

    /**
     * Returns the initial thread time counter when the current service session starts.
     * @return Initial thread time counter.
     */
    public final Long getInitialTime() {
        return initialTime;
    }

    /**
     * Sets the initial time counter value.
     * @param initialTime Initial time counter value.
     */
    private void setInitialTime(Long initialTime) {
        this.initialTime = initialTime;
    }

    /**
     * Returns the accumulated time into the current thread.
     * @return Accumulated time.
     */
    public final Long getAccumulatedTime() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - getInitialTime();
    }

    /**
     * Set the session for the thread.
     * @param session Service session.
     */
    public final void setSession(ServiceSession session) {
        if(this.session != null) {
            //Remove the status of the current thread stored into the old session
            this.session.endThread();
        }

        if(session != null) {
            //Start the status of the current thread into the new session.
            session.startThread();

            //Init the counters
            setInitialAllocatedMemory(((ThreadMXBean)ManagementFactory.getThreadMXBean()).
                    getThreadAllocatedBytes(Thread.currentThread().threadId()));
            setInitialTime(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());
        }

        this.session = session;
    }

    /**
     * Returns service thread instance for the current thread.
     * @return Service thread instance.
     */
    public static synchronized ServiceThread getServiceThreadInstance() {
        return getServiceThreadInstance(ServiceThread.class);
    }

    /**
     * Returns a new service thread instance for the current thread.
     * @param serviceThreadClass Service thread class
     * @return Service thread instance.
     * @param <S> Expected service thread instance.
     */
    public static synchronized <S extends ServiceThread> S getServiceThreadInstance(Class<S> serviceThreadClass) {
        Long threadId = Thread.currentThread().threadId();
        ServiceThread instance = serviceThreadInstances.get(threadId);
        if (instance == null) {
            // In this case there aren't any instance for this thread then we need to create a base instance for this
            // thread id.
            try {
                instance = serviceThreadClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new HCJFRuntimeException("Unable to create service thread instance (%s)", serviceThreadClass.getName());
            }
            serviceThreadInstances.put(threadId, instance);
        } else if (!instance.getClass().equals(serviceThreadClass)) {
            // In this case the instance of service thread is not the same that we need then we need to create a new
            // service thread instance using previous instance as base.
            try {
                instance = serviceThreadClass.getConstructor(ServiceThread.class).newInstance(instance);
            } catch (Exception e) {
                e.printStackTrace();
                throw new HCJFRuntimeException("Unable to create service thread instance (%s)", serviceThreadClass.getName());
            }
            serviceThreadInstances.put(threadId, instance);
        }
        if (instance.getSession() == null) {
            instance.setSession(ServiceSession.getGuestSession());
        }
        return (S) instance;
    }

    /**
     * Verify if the current thread is interrupted.
     * @throws InterruptedException Throws this exception if the current thread is interrupted.
     */
    public static void checkInterruptedThread() throws InterruptedException {
        if(Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Service thread interrupted");
        }
    }

}
