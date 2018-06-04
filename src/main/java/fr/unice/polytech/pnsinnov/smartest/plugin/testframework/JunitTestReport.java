package fr.unice.polytech.pnsinnov.smartest.plugin.testframework;

import fr.smartest.plugin.Test;
import fr.smartest.plugin.TestReport;

import java.util.Optional;

public class JunitTestReport implements TestReport {
    private final Status status;
    private final Throwable throwable;
    private final Test test;

    public JunitTestReport(Test test, Status status, Throwable throwable) {
        this.test = test;
        this.status = status;
        this.throwable = throwable;
    }

    @Override
    public Test getTest() {
        return test;
    }

    @Override
    public Status getResult() {
        return status;
    }

    @Override
    public Optional<Throwable> getCause() {
        return Optional.ofNullable(throwable);
    }

    @Override
    public String toString() {
        return "JunitTestReport{status=" + status + ", throwable=" + throwable + ", test=" + test + '}';
    }
}
