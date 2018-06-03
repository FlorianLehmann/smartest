package fr.unice.polytech.pnsinnov.smartest.plugin.loader;

import fr.smartest.exceptions.PluginException;

public class SearchPluginException extends PluginException {
    public SearchPluginException() {
    }

    public SearchPluginException(String message) {
        super(message);
    }

    public SearchPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchPluginException(Throwable cause) {
        super(cause);
    }

    public SearchPluginException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
