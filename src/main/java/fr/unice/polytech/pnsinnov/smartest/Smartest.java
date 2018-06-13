package fr.unice.polytech.pnsinnov.smartest;

import fr.smartest.exceptions.PluginException;
import fr.smartest.exceptions.SmartestException;
import fr.smartest.plugin.*;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import fr.unice.polytech.pnsinnov.smartest.exceptions.TestFailureException;
import fr.unice.polytech.pnsinnov.smartest.plugin.loader.PluginLoader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Smartest {
    private static final Logger logger = LogManager.getLogger(Smartest.class);
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
            if (testReport.getResult() != TestReport.Status.SUCCESSFUL) {
                failures.add(testReport);
            }
        }
        if (!failures.isEmpty()) {
            logger.warn("Some tests failed : " + failures);
            throw new TestFailureException(failures);
        }

        pluginLoader.vcs().commit(message);
        pluginLoader.language().save();
    }

    public Set<TestReport> test(String scope) throws PluginException {
        TestFramework testFramework = pluginLoader.testFramework();
        ProductionTool productionTool = pluginLoader.productionTool();
        productionTool.compile();
        return testFramework.run(listTests(scope), productionTool.getModules());
    }

    public HashMap<String, Duration> bench() throws PluginException {
        HashMap<String, Duration> durations = new HashMap<>();

        VCS vcs = pluginLoader.vcs();
        TestFramework testFramework = pluginLoader.testFramework();
        ProductionTool productionTool = pluginLoader.productionTool();
        productionTool.compile();

        for (Language language :
                pluginLoader.getAllLanguages()) {
            logger.info("Running Smartest with plugin " + language.getClass().getSimpleName());

            LocalTime startTime = LocalTime.now();

            language.setUp(productionTool.getModules());
            //TODO METTRE DANS LES PLUGINS LANGUAGES UN ACCEPTEUR POUR LES SCOPES VIDES (PAR DEFAUT)
            testFramework.run(language.getTestsRelatedToChanges("", vcs.diff()), productionTool.getModules());

            durations.put(language.getClass().getSimpleName(), Duration.between(startTime, LocalTime.now()));
        }

        return durations;
    }
}
