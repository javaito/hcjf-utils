package org.hcjf.layers.plugins;

import org.hcjf.layers.Layer;

/**
 * Layer that are used as plugin
 * @author javaito
 */
public abstract class PluginLayer extends Layer {

    /**
     * Need obtain the invocation target to wrap the plugin.
     * @return Invation target.
     */
    @Override
    protected abstract Object getTarget();

}
