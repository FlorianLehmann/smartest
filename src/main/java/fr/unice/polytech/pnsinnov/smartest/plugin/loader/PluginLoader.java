package fr.unice.polytech.pnsinnov.smartest.plugin.loader;

import com.google.common.io.PatternFilenameFilter;
import fr.smartest.exceptions.PluginException;
import fr.smartest.plugin.*;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginLoader {
    private static final Logger logger = LogManager.getLogger(PluginLoader.class);
    private static final String PACKAGE = "fr.unice.polytech.pnsinnov.smartest.plugin";
    private static final FilenameFilter FILENAME_FILTER = new PatternFilenameFilter(".*\\.jar");

    static {
        Reflections.log = null;
    }

    private final Configuration configuration;
    private Language language;
    private ProductionTool productionTool;
    private TestFramework testFramework;
    private VCS vcs;

    public PluginLoader(Configuration configuration) {
        this.configuration = configuration;
    }

    public Language language() throws PluginException {
        if (language == null) {
            logger.debug("loading language");
            language = initialize(configuration.language(), Language.class);
        }
        return language;
    }

    public TestFramework testFramework() throws PluginException {
        if (testFramework == null) {
            logger.debug("loading test framework");
            testFramework = initialize(configuration.testFramework(), TestFramework.class);
        }
        return testFramework;
    }

    public ProductionTool productionTool() throws PluginException {
        if (productionTool == null) {
            logger.debug("loading production tool");
            productionTool = initialize(configuration.productionTool(), ProductionTool.class);
            productionTool.setUp(configuration.projectPath());
        }
        return productionTool;
    }

    public VCS vcs() throws PluginException {
        if (vcs == null) {
            logger.debug("loading vcs");
            vcs = initialize(configuration.vcs(), VCS.class);
            vcs.setUp(configuration.gitPath(), configuration.projectPath());
        }
        return vcs;
    }

    private <T extends Plugin> T initialize(String identifier, Class<T> cls) throws PluginException {
        File[] files = listJar();
        Set<Class<? extends T>> classes = loadFromSmartest(cls);
        logger.info("Default plugin found in Smartest : " + classes);
        logger.info("Plugin found in plugin directory : " + Arrays.asList(files));
        Optional<? extends T> plugin = processClasses(classes, identifier);
        if (plugin.isPresent()) {
            logger.info("Use \"" + plugin.get() + "\" as \"" + identifier + "\"");
            return plugin.get();
        }
        for (File jar : files) {
            classes = loadJar(jar, cls);
            plugin = processClasses(classes, identifier);
            if (plugin.isPresent()) {
                return plugin.get();
            }
        }
        throw new PluginNotFound(identifier);
    }

    private <T extends Plugin> Optional<? extends T> processClasses(Set<Class<? extends T>> classes, String identifier)
            throws PluginException {
        Set<? extends T> instances = instantiateClasses(classes);
        return acceptIdentifier(instances, identifier);
    }

    private <T extends Plugin> Set<Class<? extends T>> loadFromSmartest(Class<T> cls) {
        Reflections reflections = new Reflections(PACKAGE);
        return reflections.getSubTypesOf(cls);
    }

    private <T extends Plugin> Optional<T> acceptIdentifier(Set<? extends T> instances, String identifier) {
        for (T instance : instances) {
            if (instance.accept(identifier)) {
                return Optional.of(instance);
            }
        }
        return Optional.empty();
    }

    private <T extends Plugin> Set<? extends T> instantiateClasses(Set<Class<? extends T>> classes) throws
            PluginException {
        Set<T> instances = new HashSet<>();
        for (Class<? extends T> aClass : classes) {
            try {
                instances.add(aClass.newInstance());
            }
            catch (InstantiationException | IllegalAccessException e) {
                logger.error(e);
                throw new LoadPluginException(e);
            }
        }
        return instances;
    }

    private File[] listJar() {
        File dir = new File(configuration.pluginPath().toAbsolutePath().toString());
        File[] files = dir.listFiles(FILENAME_FILTER);
        if (files == null) {
            logger.warn("Directory \"" + configuration.pluginPath() + "\" doesn't exists");
            files = new File[0];
        }
        return files;
    }

    private <T extends Plugin> Set<Class<? extends T>> loadJar(File file, Class<T> cls) throws PluginException {
        try {
            URL url = file.toURI().toURL();
            return reflectPlugin(cls, url);
        }
        catch (MalformedURLException e) {
            logger.error(e);
            throw new LoadPluginException(e);
        }
    }

    private <T extends Plugin> Set<Class<? extends T>> reflectPlugin(Class<T> cls, URL url) throws PluginException {
        try (URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{url})) {
            Reflections reflections = new Reflections(url, new SubTypesScanner(false), urlClassLoader);
            return reflections.getSubTypesOf(cls);
        }
        catch (IOException e) {
            logger.error(e);
            throw new SearchPluginException(e);
        }
    }

    public Set<Language> getAllLanguages() throws PluginException {
        return getAllCompatiblePlugins(configuration.language(), Language.class);
    }

    private <T extends Plugin> Set<T> getAllCompatiblePlugins(String identifier, Class<T> cls) throws PluginException {
        Set<T> pluginFound = new HashSet<>();

        File[] files = listJar();
        Set<Class<? extends T>> classes = loadFromSmartest(cls);
        logger.info("Default plugin found in Smartest : " + classes);
        logger.info("Plugin found in plugin directory : " + Arrays.asList(files));

        for (File jar : files) {
            classes.addAll(loadJar(jar, cls));
        }

        for (Class<? extends T> aClass :
                classes) {
            Optional<? extends T> toProcess = processClasses(Collections.singleton(aClass), identifier);

            toProcess.ifPresent(pluginFound::add);
        }

        return pluginFound;
    }
}
