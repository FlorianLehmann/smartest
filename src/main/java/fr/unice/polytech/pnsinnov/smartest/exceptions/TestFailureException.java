package fr.unice.polytech.pnsinnov.smartest.exceptions;

import fr.smartest.exceptions.SmartestException;
import fr.smartest.plugin.TestReport;

import java.util.Set;

public class TestFailureException extends SmartestException {
    private final Set<TestReport> failures;

    public TestFailureException(Set<TestReport> failures) {
        super("Code has not been committed due to test failures");
        this.failures = failures;
    }

    public Set<TestReport> getFailures() {
        return failures;
    }
}
