package fr.unice.polytech.pnsinnov.smartest.plugin;

import fr.smartest.plugin.Language;
import fr.smartest.plugin.ProductionTool;
import fr.smartest.plugin.TestFramework;
import fr.smartest.plugin.VCS;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;

public class PluginLoader {
    private final Configuration configuration;

    public PluginLoader(Configuration configuration) {
        this.configuration = configuration;
    }

    public Language language() {
        return null;
    }

    public TestFramework testFramework() {
        return null;
    }

    public ProductionTool productionTool() {
        return null;
    }

    public VCS vcs() {
        return null;
    }
}
