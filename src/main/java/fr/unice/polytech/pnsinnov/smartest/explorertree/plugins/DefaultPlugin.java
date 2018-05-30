package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers.DefaultExplorer;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners.DefaultRunner;

import java.util.Optional;

public class DefaultPlugin extends Plugin {

    @Override
    void setupExplorer() {
        this.explorerStrategy = new DefaultExplorer();
    }

    @Override
    void setupRunner() {
        this.testRunner = new DefaultRunner();
    }

    @Override
    public boolean accept(String langageFromConfig, String testFrameworkFromConfig, Optional<String> managementFromConfig) {
        return false;
    }

}
