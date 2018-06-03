package fr.unice.polytech.pnsinnov.smartest.plugin;

import fr.smartest.exceptions.PluginException;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import fr.unice.polytech.pnsinnov.smartest.configuration.ConfigurationHolder;
import fr.unice.polytech.pnsinnov.smartest.plugin.loader.PluginLoader;
import fr.unice.polytech.pnsinnov.smartest.plugin.loader.PluginNotFound;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class PluginLoaderTest {
    @Test
    void pluginPathDoesNotExist() throws PluginException {
        Configuration configuration = new ConfigurationHolder(null, "unknownFolder", null, null, null, null, null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertThrows(PluginException.class, pluginLoader::language);
    }

    @Test
    void loadJava() throws PluginException {
        URL plugins = this.getClass().getClassLoader().getResource("plugins");
        assertNotNull(plugins);
        Configuration configuration = new ConfigurationHolder(null, plugins.getPath(), null, "Java", null, null, null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.language().accept("Java"));
    }


    @Test
    void loadJunit5() throws PluginException {
        URL plugins = this.getClass().getClassLoader().getResource("plugins");
        assertNotNull(plugins);
        Configuration configuration = new ConfigurationHolder(null, plugins.getPath(), null, null, null, "Junit5",
                null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.testFramework().accept("Junit5"));
    }

    @Test
    void loadMaven() throws PluginException {
        URL plugins = this.getClass().getClassLoader().getResource("plugins");
        assertNotNull(plugins);
        Configuration configuration = new ConfigurationHolder(null, plugins.getPath(), null, null, "Maven", null, null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.productionTool().accept("Maven"));
    }

    @Test
    void loadGit() throws PluginException {
        URL plugins = this.getClass().getClassLoader().getResource("plugins");
        assertNotNull(plugins);
        Configuration configuration = new ConfigurationHolder(null, plugins.getPath(), null, null, null, null, "git");
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.vcs().accept("git"));
    }

    @Test
    void unknownPlugin() {
        URL plugins = this.getClass().getClassLoader().getResource("plugins");
        assertNotNull(plugins);
        Configuration configuration = new ConfigurationHolder(null, plugins.getPath(), null, null, null, "Junit6",
                null);
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertThrows(PluginNotFound.class, pluginLoader::testFramework);
    }

    @Test
    void loadAll() throws PluginException {
        URL plugins = this.getClass().getClassLoader().getResource("plugins");
        assertNotNull(plugins);
        Configuration configuration = new ConfigurationHolder(null, plugins.getPath(), null, "Java", "Maven",
                "Junit5", "git");
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.language().accept("Java"));
        assertTrue(pluginLoader.productionTool().accept("Maven"));
        assertTrue(pluginLoader.testFramework().accept("Junit5"));
        assertTrue(pluginLoader.vcs().accept("git"));
    }
}