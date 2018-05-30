package fr.unice.polytech.pnsinnov.smartest.explorertree;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.Plugin;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers.Explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TreeModule {

    private List<String> pathsToModules;

    private Explorer explorer;

    private TreeModule(TreeModuleBuilder builder){
        pathsToModules = new ArrayList<>();

        this.explorer = builder.wantedPlugin.getExplorerStrategy();

        findAllModules(new File(builder.projectBase));
    }

    private void findAllModules(File baseDir) {
        pathsToModules = explorer.findAllModules(baseDir);
    }

    public List<String> getPathsToModules() {
        return pathsToModules;
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

        public TreeModule build(){
            return new TreeModule(this);
        }
    }
}
