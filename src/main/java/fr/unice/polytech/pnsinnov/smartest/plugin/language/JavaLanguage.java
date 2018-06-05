package fr.unice.polytech.pnsinnov.smartest.plugin.language;

import fr.smartest.plugin.Language;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.diff.Diff;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.diff.DiffFactory;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.diff.InvalidScopeTests;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.diff.Scope;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.factory.TreeFactory;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence.Database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaLanguage implements Language {

    private List<Module> modules;

    @Override
    public void setUp(List<Module> modules) {
        this.modules = modules;
        if (!Database.getInstance().checkExistence()) {
            List<String> srcDirPaths = new ArrayList<>();
            List<String> testDirPaths = new ArrayList<>();
            for (Module module : modules) {
                //TODO MODIFIER AVEC LES PATHS
                srcDirPaths.add(module.getSrcPath().toAbsolutePath().toString());
                testDirPaths.add(module.getTestPath().toAbsolutePath().toString());
            }
            DirectoryExplorer directoryExplorer = new DirectoryExplorer();
            List<File> javaFiles = directoryExplorer.explore(srcDirPaths);
            TreeFactory treeFactory = new TreeFactory(Database.getInstance().getTree());
            try {
                treeFactory.generateTrees(javaFiles, javaFiles);
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<File> javaTestFiles = directoryExplorer.explore(testDirPaths);
            treeFactory = new TreeFactory(Database.getInstance().getTests());

            List<File> common = new ArrayList<>();
            common.addAll(javaFiles);
            common.addAll(javaTestFiles);
            try {
                treeFactory.generateTrees(javaTestFiles, common);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<Test> getTestsRelatedToChanges(String scope, Set<fr.smartest.plugin.Diff> fileDiff) {
        Diff diff = null;
        try {
            diff = new DiffFactory(fileDiff).build(Scope.valueOf(scope.toUpperCase()));
        } catch (InvalidScopeTests invalidScopeTests) {
            invalidScopeTests.printStackTrace();
            return new HashSet<>();
        }
        try {
            return diff.getTestsRelatedToChanges(modules);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    @Override
    public void save() {
        try {
            Database.getInstance().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean accept(String s) {
        return s.toLowerCase().equals("java");
    }

}
