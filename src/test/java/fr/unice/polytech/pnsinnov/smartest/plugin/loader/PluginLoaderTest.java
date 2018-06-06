package fr.unice.polytech.pnsinnov.smartest.plugin.loader;

import fr.smartest.exceptions.PluginException;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import fr.unice.polytech.pnsinnov.smartest.configuration.ConfigurationHolder;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginLoaderTest {
    @Test
    void pluginPathDoesNotExist() {
        Configuration configuration = new ConfigurationHolder(null, Paths.get(""), null, "Python", null, null, null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertThrows(PluginNotFound.class, pluginLoader::language);
    }

    @Test
    void loadJava() throws PluginException {
        Configuration configuration = new ConfigurationHolder(null, Paths.get(""), null, "Java", null, null, null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.language().accept("Java"));
    }


    @Test
    void loadJunit5() throws PluginException {
        Configuration configuration = new ConfigurationHolder(null, Paths.get(""), null, null, null, "Junit5", null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.testFramework().accept("Junit5"));
    }

    @Test
    void loadMaven() throws PluginException {
        Configuration configuration = new ConfigurationHolder(null, Paths.get(""), null, null, "Maven", null, null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.productionTool().accept("Maven"));
    }

    @Test
    void loadGit() throws PluginException {
        Configuration configuration = new ConfigurationHolder(null, Paths.get(""), null, null, null, null, "git");
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.vcs().accept("git"));
    }

    @Test
    void unknownPlugin() {
        Configuration configuration = new ConfigurationHolder(null, Paths.get(""), null, null, null, "Junit6", null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertThrows(PluginNotFound.class, pluginLoader::testFramework);
    }

    @Test
    void loadAll() throws PluginException {
        Configuration configuration = new ConfigurationHolder(null, Paths.get(""),
                null, "Java", "Maven", "Junit5", "git");
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.language().accept("Java"));
        assertTrue(pluginLoader.productionTool().accept("Maven"));
        assertTrue(pluginLoader.testFramework().accept("Junit5"));
        assertTrue(pluginLoader.vcs().accept("git"));
    }
}