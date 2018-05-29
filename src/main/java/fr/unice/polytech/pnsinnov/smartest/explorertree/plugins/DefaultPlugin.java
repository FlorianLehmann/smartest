package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.strategytech.DefaultExplorer;

import java.util.Optional;

public class DefaultPlugin extends Plugin {

    @Override
    void setupExplorer() {
        this.explorerStrategy = new DefaultExplorer();
    }

    @Override
    public boolean accept(String langageFromConfig, String testFrameworkFromConfig, Optional<String> managementFromConfig) {
        return false;
    }

}
