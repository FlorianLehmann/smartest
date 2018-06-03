package fr.unice.polytech.pnsinnov.smartest.plugin.loader;

import fr.smartest.exceptions.PluginException;

public class LoadPluginException extends PluginException {
    public LoadPluginException() {
    }

    public LoadPluginException(String message) {
        super(message);
    }

    public LoadPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadPluginException(Throwable cause) {
        super(cause);
    }

    public LoadPluginException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
