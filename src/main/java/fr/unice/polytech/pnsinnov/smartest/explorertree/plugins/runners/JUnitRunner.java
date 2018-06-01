package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;


import fr.unice.polytech.pnsinnov.smartest.parser.Database;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JUnitRunner implements TRunner {

    @Override
    public boolean runAllTests(Set<String> toRun) {
        try {
            File testPath = new File(Paths.get("target/test-classes/").toString());
            File srcPath = new File(Paths.get("target/classes/").toString());
            URL[] testFolder = {srcPath.toURI().toURL(), testPath.toURI().toURL()};

            URLClassLoader urlClassLoader = new URLClassLoader(testFolder);

            Set<Class> classes = new HashSet<>();

            for (String test : toRun) {
                Set<String> itOnSet = Database.getInstance().getTestLinkToClass(test);
                if (itOnSet != null) {
                    for (String singleTest : itOnSet) {
                        Class c = urlClassLoader.loadClass(singleTest);
                        classes.add(c);
                    }
                }
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
            return listener.getSummary().getTotalFailureCount() <= 0;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("No class found for " + e.getLocalizedMessage());
        }
        return false;
    }
}