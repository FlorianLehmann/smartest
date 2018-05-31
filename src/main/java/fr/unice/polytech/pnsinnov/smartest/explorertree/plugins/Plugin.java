package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers.Explorer;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners.TRunner;

import java.util.Optional;
import java.util.Set;

public abstract class Plugin {

    Explorer explorerStrategy;

    TRunner testRunner;

    public Plugin(){
        setupExplorer();
        setupRunner();
    }

    abstract void setupExplorer();

    abstract void setupRunner();

    /**
     * Return true if the project is accepted by the plugin. False otherwise
     * @param langageFromConfig the langage of the project
     * @param testFrameworkFromConfig the test framework used in the project
     * @param managementFromConfig the management tool used in the project
     * @return true if the project is accepted. False otherwise
     */
    public abstract boolean accept(String langageFromConfig, String testFrameworkFromConfig, Optional<String> managementFromConfig);

    public Explorer getExplorerStrategy() {
        return explorerStrategy;
    }

    public TRunner getTestRunner() {
        return testRunner;
    }

    /**
     * Return true if the file from git is accepted and can be processed by the plugin. False otherwise
     * @param path the path to the file to be processed
     * @return if the file is accepted or not
     */
    public abstract boolean isValidPath(String path);

    /**
     * Method called when a file need to be processed by the plugin. For instance, when it has been modified.
     * The goal is to return a set of string, refering to the tests paths that need to be runned before a commit
     * @param path the path to the currently processed file
     * @param content the content of the older file version
     * @return the location of the tests that need to be processed before a commit regarding the current changes
     */
    public abstract Set<String> compareChanges(String path, String content);
}
