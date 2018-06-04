package fr.unice.polytech.pnsinnov.smartest;

import fr.smartest.exceptions.PluginException;
import fr.smartest.exceptions.SmartestException;
import fr.smartest.plugin.*;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import fr.unice.polytech.pnsinnov.smartest.exceptions.TestFailureException;
import fr.unice.polytech.pnsinnov.smartest.plugin.loader.PluginLoader;

import java.util.HashSet;
import java.util.Set;

public class Smartest {
    private final PluginLoader pluginLoader;

    public Smartest(Configuration configuration) {
        pluginLoader = new PluginLoader(configuration);
    }

    public Set<Test> listTests(String scope) throws PluginException {
        Language language = pluginLoader.language();
        ProductionTool productionTool = pluginLoader.productionTool();
        language.setUp(productionTool.getModules());
        VCS vcs = pluginLoader.vcs();
        return language.getTestsRelatedToChanges(scope, vcs.diff());
    }

    public void commit(String scope, String message) throws SmartestException {
        Set<TestReport> test = test(scope);
        Set<TestReport> failures = new HashSet<>();
        for (TestReport testReport : test) {
            if (testReport.getResult() != TestReport.Status.SUCESSFUL) {
                failures.add(testReport);
            }
        }
        if (!failures.isEmpty()) {
            throw new TestFailureException(failures);
        }
        pluginLoader.vcs().commit(message);
    }

    public Set<TestReport> test(String scope) throws PluginException {
        return pluginLoader.testFramework().run(listTests(scope));
    }
}
