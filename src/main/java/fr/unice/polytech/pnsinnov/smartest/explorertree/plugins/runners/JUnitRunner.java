package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;


import fr.unice.polytech.pnsinnov.smartest.parser.Database;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

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
                LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                        .selectors(selectClass(cls))
                        .build();

                Launcher launcher = LauncherFactory.create();
                DiscoverySelector discoverySelector;
                TestResultListener listener = new TestResultListener();
                launcher.registerTestExecutionListeners(listener);
                launcher.execute(request);
                if (listener.getResult() == EnumTestResult.FAILED)
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