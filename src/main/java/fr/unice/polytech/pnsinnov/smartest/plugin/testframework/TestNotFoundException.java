package fr.unice.polytech.pnsinnov.smartest.plugin.testframework;


import fr.smartest.exceptions.TestFrameworkException;

public class TestNotFoundException extends TestFrameworkException {

    public TestNotFoundException() {
    }

    public TestNotFoundException(String message) {
        super(message);
    }

    public TestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestNotFoundException(Throwable cause) {
        super(cause);
    }

    public TestNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
