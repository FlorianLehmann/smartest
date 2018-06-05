package fr.unice.polytech.pnsinnov.smartest.plugin.testframework;

import fr.smartest.exceptions.TestFrameworkException;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.smartest.plugin.TestFramework;
import fr.smartest.plugin.TestReport;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JUnit5Runner implements TestFramework {
    private static final Logger logger = LogManager.getLogger(JUnit5Runner.class);
    private static final String IDENTIFIER = "Junit5";
    private String path;
    private List<Module> modules;

    @Override
    public void setUp(String path, List<Module> modules) {
        this.path = path;
        this.modules = modules;
        logger.debug("Setup Juni5 with path=\"" + path + "\" and modules=\"" + modules + "\"");
    }

    private URL[] getModulesURL() {
        Set<URL> sources = new HashSet<>();
        for (Module module : this.modules) {
            try {
                Path s = Paths.get(this.path, module.getCompiledSrcPath());
                Path t = Paths.get(this.path, module.getCompiledTestPath());
                sources.add(new File(s.toString()).toURI().toURL());
                sources.add(new File(t.toString()).toURI().toURL());
            }
            catch (MalformedURLException e) {
                logger.error(e);
            }
        }
        logger.debug("Loaded sources from " + sources);
        return sources.toArray(new URL[0]);
    }

    @Override
    public Set<TestReport> run(Set<Test> tests) throws TestFrameworkException {
        logger.debug("Running tests : " + tests);
        Set<TestReport> testReports = new HashSet<>();
        List<Test> sortedByPriority = tests.stream().sorted(this::compare).collect(Collectors.toList());
        logger.debug("Sorted tests by priority: " + sortedByPriority);
        URL[] urls = getModulesURL();
        try (URLClassLoader urlClassLoader = new URLClassLoader(urls)) {
            for (Test test : sortedByPriority) {
                Class<?> cls = urlClassLoader.loadClass(test.getIdentifier());
                testReports.add(run(test, cls));
            }
        }
        catch (IOException | ClassNotFoundException e) {
            logger.error(e);
        }
        logger.debug("Test reports=" + testReports);
        return testReports;
    }

    private TestReport run(Test test, Class<?> cls) {
        logger.debug("running test=" + test + "with loaded class " + cls);
        Launcher launcher = LauncherFactory.create();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(cls)).build();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        return createTestReportFrom(test, listener.getSummary());
    }

    private int compare(Test t1, Test t2) {
        return Integer.compare(t1.getPriority().getValue(), t2.getPriority().getValue());
    }

    private TestReport createTestReportFrom(Test test, TestExecutionSummary summary) {
        logger.debug("creating test report from test=" + test + " and summary=" + summary);
        TestReport.Status status = TestReport.Status.SUCESSFUL;
        Throwable throwable = null;
        for (TestExecutionSummary.Failure failure : summary.getFailures()) {
            status = TestReport.Status.FAILURE;
            throwable = failure.getException();
        }
        return new JunitTestReport(test, status, throwable);
    }

    @Override
    public boolean accept(String s) {
        return s != null && IDENTIFIER.equalsIgnoreCase(s.trim());
    }
}
