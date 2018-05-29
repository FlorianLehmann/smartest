package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;


import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.strategytech.MvnExplorer;

import java.util.Optional;

public class MvnPlugin extends Plugin{

    @Override
    void setupExplorer() {
        this.explorerStrategy = new MvnExplorer();
    }

    @Override
    public boolean accept(String langageFromConfig, String testFrameworkFromConfig, Optional<String> managementFromConfig) {
        //TODO ADD LOGIC

        return true;
    }
}
