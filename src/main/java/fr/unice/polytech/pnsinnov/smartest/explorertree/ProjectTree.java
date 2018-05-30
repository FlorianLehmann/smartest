package fr.unice.polytech.pnsinnov.smartest.explorertree;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProjectTree {

    private List<String> pathsToModules;

    private Plugin plugin;

    private ProjectTree(TreeModuleBuilder builder){
        pathsToModules = new ArrayList<>();

        this.plugin = builder.wantedPlugin;

        findAllModules(new File(builder.projectBase));
    }

    private void findAllModules(File baseDir) {
        pathsToModules = plugin.getExplorerStrategy().findAllModules(baseDir);
    }

    public List<String> getPathsToModules() {
        return pathsToModules;
    }

    public boolean runAllTests(Set<String> tests) {
        return plugin.getTestRunner().runAllTests(tests);
    }

    public String getPathToSrc() {
        return plugin.getExplorerStrategy().getPathToSrc();
    }

    public String getPathToTest() {
        return plugin.getExplorerStrategy().getPathToTest();
    }

    public static class TreeModuleBuilder {

        Plugin wantedPlugin;

        String projectBase;

        public TreeModuleBuilder withProjectBase(String projectBase){
            this.projectBase = projectBase;

            return this;
        }

        public TreeModuleBuilder withPlugin(Plugin toUse) {
            this.wantedPlugin = toUse;

            return this;
        }

        public ProjectTree build(){
            return new ProjectTree(this);
        }
    }
}
