package fr.unice.polytech.pnsinnov.smartest.plugin.testframework;

import fr.smartest.plugin.Test;
import fr.smartest.plugin.TestFramework;
import fr.smartest.plugin.TestReport;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JUnit5Runner implements TestFramework {
    @Override
    public List<TestReport> Run(Set<Test> set) {
        List<TestReport> testReports = new ArrayList<>();

        try {

            //TODO METTRE 2 STRINGS POUR PAS AVOIR DES TRUCS EN BRUT !
            File testPath = new File(Paths.get("target/test-classes/").toString());
            File srcPath = new File(Paths.get("target/classes/").toString());
            URL[] testFolder = {srcPath.toURI().toURL(), testPath.toURI().toURL()};

            URLClassLoader urlClassLoader = new URLClassLoader(testFolder);

            Set<Class> classes = new HashSet<>();

            for (Test test : set) {
                Set<String> itOnSet = Database.getInstance().getTestLinkToClass(test.getIdentifier());
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

            //TODO METTRE DES TESTRESULTS MAIS COMMENT ?

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("No class found for " + e.getLocalizedMessage());
        }

        return testReports;
    }

    @Override
    public boolean accept(String s) {
        return s.equals("junit5");
    }
}
