package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;


import fr.smartest.exceptions.VCSException;

public class GitNotFoundException extends VCSException {

    public GitNotFoundException() {
    }

    public GitNotFoundException(String message) {
        super(message);
    }

    public GitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitNotFoundException(Throwable cause) {
        super(cause);
    }

    public GitNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
