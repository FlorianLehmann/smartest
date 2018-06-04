package fr.unice.polytech.pnsinnov.smartest;

import fr.smartest.exceptions.PluginException;
import fr.smartest.plugin.Language;
import fr.smartest.plugin.Test;
import fr.smartest.plugin.TestReport;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import fr.unice.polytech.pnsinnov.smartest.plugin.loader.PluginLoader;

import java.util.List;
import java.util.Set;

public class Smartest {
    private final PluginLoader pluginLoader;

    public Smartest(Configuration configuration) {
        pluginLoader = new PluginLoader(configuration);
    }

    public Set<Test> listTests(String scope) throws PluginException {
        Language language = pluginLoader.language();
        language.setUp(pluginLoader.productionTool().getModules());
        return language.getTestsRelatedToChanges(scope);
    }

    public void commit(String scope, String message) throws PluginException {
        if (test(scope).stream().allMatch(testReport -> testReport.getResult() == TestReport.Status.SUCESSFUL)) {
            pluginLoader.vcs().commit(message);
        }
    }

    public List<TestReport> test(String scope) throws PluginException {
        return pluginLoader.testFramework().Run(listTests(scope));
    }
}
