package fr.unice.polytech.pnsinnov.smartest;

import fr.smartest.plugin.Test;
import fr.smartest.plugin.TestReport;

import java.util.ArrayList;
import java.util.List;

public class Smartest {
    public List<Test> listTests(String scope) {
        // language.getTestsRelatedToChanges(scope);
        return new ArrayList<>();
    }
    
    public void commit(String scope, String message) {
        if (test(scope).stream().allMatch(testReport -> testReport.getResult() == TestReport.Status.SUCESSFUL)) {
            // cvs.commit(message);
        }
    }

    public List<TestReport> test(String scope) {
        // testframework.runTest(listTests(scope);
        return new ArrayList<>();
    }
}
