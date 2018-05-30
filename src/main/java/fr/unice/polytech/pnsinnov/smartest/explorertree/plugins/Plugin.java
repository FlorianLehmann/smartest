package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers.Explorer;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners.TRunner;

import java.util.Optional;

public abstract class Plugin {

    Explorer explorerStrategy;

    TRunner testRunner;

    public Plugin(){
        setupExplorer();
        setupRunner();
    }

    abstract void setupExplorer();

    abstract void setupRunner();

    public abstract boolean accept(String langageFromConfig, String testFrameworkFromConfig, Optional<String> managementFromConfig);

    public Explorer getExplorerStrategy() {
        return explorerStrategy;
    }

    public TRunner getTestRunner() {
        return testRunner;
    }
}
