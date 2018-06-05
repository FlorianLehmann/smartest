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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaLanguage implements Language {

    private static final Logger logger = LogManager.getLogger(JavaLanguage.class);

    private List<Module> modules;

    @Override
    public void setUp(List<Module> modules) {
        this.modules = modules;
        if (!Database.getInstance().checkExistence()) {

            logger.info("Building trees");

            List<Path> srcDirPaths = new ArrayList<>();
            List<Path> testDirPaths = new ArrayList<>();
            for (Module module : modules) {
                srcDirPaths.add(module.getSrcPath());
                testDirPaths.add(module.getTestPath());
            }

            DirectoryExplorer directoryExplorer = new DirectoryExplorer();
            List<File> javaFiles = directoryExplorer.explore(srcDirPaths);
            TreeFactory treeFactory = new TreeFactory(Database.getInstance().getTree());
            treeFactory.generateTrees(javaFiles, javaFiles);


            List<File> javaTestFiles = directoryExplorer.explore(testDirPaths);
            treeFactory = new TreeFactory(Database.getInstance().getTests());

            List<File> common = new ArrayList<>();
            common.addAll(javaFiles);
            common.addAll(javaTestFiles);
            treeFactory.generateTrees(javaTestFiles, common);
        }
    }

    @Override
    public Set<Test> getTestsRelatedToChanges(String scope, Set<fr.smartest.plugin.Diff> fileDiff) {
        Diff diff = null;
        try {
            diff = new DiffFactory(fileDiff).build(Scope.valueOf(scope.toUpperCase()));
            return diff.getTestsRelatedToChanges(modules);
        } catch (InvalidScopeTests invalidScopeTests) {
            invalidScopeTests.printStackTrace();
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
