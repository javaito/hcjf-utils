package org.hcjf.layers.distributed;

import org.hcjf.errors.HCJFRuntimeException;
import org.hcjf.layers.LayerInterface;
import org.hcjf.log.Log;
import org.hcjf.properties.SystemProperties;

import java.lang.reflect.Method;

public class NetworkSockets {

    private static final NetworkSockets instance = new NetworkSockets();
    private NetworkSocket networkSocket;

    private NetworkSockets() {
        String networkSocketClassName = SystemProperties.get(SystemProperties.Layer.NETWORK_SOCKET_IMPLEMENTATION);
        if (networkSocketClassName == null) {
            networkSocket = null;
        } else {
            try {
                networkSocket = (NetworkSocket) Class.forName(networkSocketClassName).getConstructor().newInstance();
            } catch (Exception ex) {
                Log.e(SystemProperties.get(SystemProperties.Layer.LOG_TAG), "Network socket implementation not found", ex);
            }
        }
    }

    private static NetworkSockets getInstance() {
        return instance;
    }


    /**
     * This method verifies if the layer and name indicated are published into the cloud.
     * @param layerClass Layer class.
     * @param implName Layer implementation name.
     * @return Returns true if the layer is published and false in otherwise.
     */
    public static boolean isLayerPublished(Class<? extends LayerInterface> layerClass, String implName) {
        NetworkSocket socket = getInstance().networkSocket;
        boolean result = false;
        if (socket != null) {
            result = socket.isLayerPublished(layerClass, implName);
        }
        return result;
    }

    /**
     * Returns the object that represent the distributed layer.
     * @param layerClass Layer class.
     * @param implName Layer implementation name.
     * @return Regex if exist or null.
     */
    public static String getRegexFromDistributedLayer(Class<? extends LayerInterface> layerClass, String implName) {
        NetworkSocket socket = getInstance().networkSocket;
        String result = null;
        if (socket != null) {
            result = socket.getRegexFromDistributedLayer(layerClass, implName);
        }
        return result;
    }

    /**
     * Publish a distributed layer into the cloud.
     * @param layerClass Layer class.
     * @param implName Layer implementation name.
     * @param regex Regex to match the layer.
     */
    public static void publishDistributedLayer(Class<? extends LayerInterface> layerClass, String implName, String regex) {
        NetworkSocket socket = getInstance().networkSocket;
        if (socket != null) {
            socket.publishDistributedLayer(layerClass, implName, regex);
        } else {
            throw new HCJFRuntimeException("Network socket implementation not found");
        }
    }

    /**
     * Invokes the remote instance of a layer.
     * @param layerClass Layer interface class.
     * @param implName Implementation name.
     * @param method Method to invoke.
     * @param parameters Parameters to invoke.
     * @param <O> Expected return data type.
     * @return Invocation result.
     */
    public static <O extends Object> O layerInvoke(Class<? extends LayerInterface> layerClass, String implName, Method method, Object... parameters){
        NetworkSocket socket = getInstance().networkSocket;
        O result;
        if (socket != null) {
            result = socket.layerInvoke(layerClass, implName, method, parameters);
        } else {
            throw new HCJFRuntimeException("Network socket implementation not found");
        }
        return result;
    }

}
