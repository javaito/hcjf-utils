package org.hcjf.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Message manager
 * @author javaito
 *
 */
public abstract class Messages {

    private final Map<String, String> defaultMessages;


    protected Messages() {
        defaultMessages = new HashMap<>();
    }

    /**
     * Return the message associated to the error code.
     * @param messageCode Message code.
     * @param params Parameters to complete the message.
     * @return Message complete and translated.
     */
    protected String getInternalMessage(String messageCode, Object... params) {
        String result = null;

        if(result == null) {
            result = defaultMessages.get(messageCode);
            if(result == null) {
                result = messageCode;
            }
        }

        return String.format(result, params);
    }

    /**
     * Add the default value associated to error code.
     * @param errorCode Error code.
     * @param defaultMessage Default message.
     */
    protected void addInternalDefault(String errorCode, String defaultMessage) {
        defaultMessages.put(errorCode, defaultMessage);
    }

}
