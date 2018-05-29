package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;


import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.strategytech.Explorer;

import java.util.Optional;

public abstract class Plugin {

    Explorer explorerStrategy;

    public Plugin(){
        setupExplorer();
    }

    abstract void setupExplorer();

    public abstract boolean accept(String langageFromConfig, String testFrameworkFromConfig, Optional<String> managementFromConfig);

    public Explorer getExplorerStrategy() {
        return explorerStrategy;
    }
}
