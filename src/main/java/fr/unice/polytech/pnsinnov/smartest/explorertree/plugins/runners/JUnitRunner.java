package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;


import fr.unice.polytech.pnsinnov.smartest.parser.Database;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
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
import java.util.Set;

import static org.junit.platform.engine.discovery.DiscoverySelectors.*;

public class JUnitRunner implements TRunner{

    @Override
    public boolean runAllTests(Set<String> toRun) {
        try {
            File file = new File(Paths.get("target/test-classes/").toString());

            URL[] testFolder = {file.toURI().toURL()};

            URLClassLoader urlClassLoader = new URLClassLoader(testFolder);

            Set<Class> classes = new HashSet<>();

            for (String test :
                    toRun) {
                Set<String> itOnSet = Database.getInstance().getTestLinkToClass(test);
                if (itOnSet != null){
                    for (String singleTest :
                            itOnSet) {
                        Class c = urlClassLoader.loadClass(singleTest);

                        classes.add(c);
                    }
                }
            }

            for (Class cls: classes) {
                System.out.println(cls);
                Launcher launcher = LauncherFactory.create();

                SummaryGeneratingListener listener = new SummaryGeneratingListener();
                launcher.registerTestExecutionListeners(listener);
                LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                        .selectors(selectClass(cls))
                        .build();

                launcher.discover(request);
                launcher.execute(request);
                listener.getSummary().getFailures().get(0).getException();
                listener.getSummary().printTo(new PrintWriter(System.out));

                if (listener.getSummary().getTestsFailedCount() != 0)
                    return false;
            }

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("No class found for " + e.getLocalizedMessage());
        }
        return false;
    }
}