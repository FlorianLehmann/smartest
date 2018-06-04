package fr.unice.polytech.pnsinnov.smartest.plugin.testframework;

import fr.smartest.exceptions.TestFrameworkException;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.smartest.plugin.TestFramework;
import fr.smartest.plugin.TestReport;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JUnit5Runner implements TestFramework {

    private List<Module> modules;

    @Override
    public void setUp(List<Module> list) {
        this.modules = new ArrayList<>();
    }

    @Override
    public List<TestReport> Run(Set<Test> set) throws TestFrameworkException {
        List<TestReport> testReports = new ArrayList<>();

        try {

            List<URL> testFolders = new ArrayList<>();

            for (Module module :
                    this.modules) {
                testFolders.add(new File(Paths.get(module.getCompiledTestPath()).toString()).toURI().toURL());
                testFolders.add(new File(Paths.get(module.getCompiledSrcPath()).toString()).toURI().toURL());
            }
            URLClassLoader urlClassLoader = new URLClassLoader(testFolders.toArray(new URL[this.modules.size()*2]));

            Set<Class> classes = new HashSet<>();

            for (Test test : set) {
                Class c = urlClassLoader.loadClass(test.getIdentifier());
                classes.add(c);
            }

            List<ClassSelector> selectors = classes.stream()
                    .map(DiscoverySelectors::selectClass)
                    .collect(Collectors.toList());
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectors)
                    .build();
            Launcher launcher = LauncherFactory.create();
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            launcher.registerTestExecutionListeners(listener);
            launcher.execute(request);
            listener.getSummary().printTo(new PrintWriter(System.out));

            //TODO METTDRE DANS LA LIST A RETURN SAUF QUE Y4A PAS VRAIMENT MOYEN

        }
        catch (MalformedURLException e) {
            throw new TestLoadException(e);
        } catch (ClassNotFoundException e) {
            throw new TestNotFoundException(e);
        }


        return testReports;
    }

    @Override
    public boolean accept(String s) {
        return s.equals("junit5");
    }
}
