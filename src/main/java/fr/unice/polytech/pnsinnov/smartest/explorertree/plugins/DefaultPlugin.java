package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers.DefaultExplorer;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners.DefaultRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public boolean isValidPath(String path) {
        return false;
    }

    @Override
    public Set<String> compareChanges(String path, String content) {
        return new HashSet<>();
    }

}
