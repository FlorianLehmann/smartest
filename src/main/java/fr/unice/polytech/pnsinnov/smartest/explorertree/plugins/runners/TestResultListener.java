package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

public class TestResultListener implements TestExecutionListener {

    private EnumTestResult result;

    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (testIdentifier.isTest()) {

            String result = testExecutionResult.getStatus().toString();

            if (result.equals(EnumTestResult.SUCCESSFUL.toString())) {
                this.result = EnumTestResult.SUCCESSFUL;
            } else {
                this.result = EnumTestResult.FAILED;
            }


        }
    }

    public EnumTestResult getResult() {
        return result;
    }

}
