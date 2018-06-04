package fr.unice.polytech.pnsinnov.smartest.plugin.production;

import fr.smartest.exceptions.ProductionToolException;

public class MavenCompileException extends ProductionToolException{

    public MavenCompileException() {
    }

    public MavenCompileException(String message) {
        super(message);
    }

    public MavenCompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public MavenCompileException(Throwable cause) {
        super(cause);
    }

    public MavenCompileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
