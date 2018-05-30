package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;


import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JUnitRunner implements TRunner{

    @Override
    public boolean runAllTests(List<String> toRun) {
        try {
            File file = new File(Paths.get("target/test-classes/").toString());

            URL[] testFolder = {file.toURI().toURL()};

            URLClassLoader urlClassLoader = new URLClassLoader(testFolder);

            List<Class> classes = new ArrayList<>();

            Class c = urlClassLoader.loadClass("fr.unice.polytech.pnsinnov.smartest.MainTest");

            classes.add(c);

            Request request = Request.classes((classes.toArray(new Class[classes.size()])));

            Result result = new JUnitCore().run(request);

            return result.wasSuccessful();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("No class found for " + e.getLocalizedMessage());
        }
        return false;
    }
}
