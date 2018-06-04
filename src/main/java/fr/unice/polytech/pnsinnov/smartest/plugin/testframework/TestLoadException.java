package fr.unice.polytech.pnsinnov.smartest.plugin.testframework;

import fr.smartest.exceptions.TestFrameworkException;

public class TestLoadException extends TestFrameworkException {

    public TestLoadException() {
    }

    public TestLoadException(String message) {
        super(message);
    }

    public TestLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestLoadException(Throwable cause) {
        super(cause);
    }

    public TestLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
