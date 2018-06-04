package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;

import fr.smartest.exceptions.VCSException;

public class GitException extends VCSException {

    public GitException() {
    }

    public GitException(String message) {
        super(message);
    }

    public GitException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitException(Throwable cause) {
        super(cause);
    }

    public GitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
