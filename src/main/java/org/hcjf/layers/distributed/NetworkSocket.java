package org.hcjf.layers.distributed;

import org.hcjf.layers.LayerInterface;

import java.lang.reflect.Method;

public interface NetworkSocket {

    /**
     * This method verifies if the layer and name indicated are published into the cloud.
     * @param layerClass Layer class.
     * @param implName Layer implementation name.
     * @return Returns true if the layer is published and false in otherwise.
     */
    boolean isLayerPublished(Class<? extends LayerInterface> layerClass, String implName);

    /**
     * Returns the object that represent the distributed layer.
     * @param layerClass Layer class.
     * @param implName Layer implementation name.
     * @return Regex if exist or null.
     */
    String getRegexFromDistributedLayer(Class<? extends LayerInterface> layerClass, String implName);

    /**
     * Publish a distributed layer into the cloud.
     * @param layerClass Layer class.
     * @param implName Layer implementation name.
     * @param regex Regex to match the layer.
     */
    void publishDistributedLayer(Class<? extends LayerInterface> layerClass, String implName, String regex);

    /**
     * Invokes the remote instance of a layer.
     * @param layerClass Layer interface class.
     * @param implName Implementation name.
     * @param method Method to invoke.
     * @param parameters Parameters to invoke.
     * @param <O> Expected return data type.
     * @return Invocation result.
     */
    <O extends Object> O layerInvoke(Class<? extends LayerInterface> layerClass, String implName, Method method, Object... parameters);

}
