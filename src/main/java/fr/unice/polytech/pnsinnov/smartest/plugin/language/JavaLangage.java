package fr.unice.polytech.pnsinnov.smartest.plugin.language;

import fr.smartest.plugin.Language;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.factory.TreeFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JavaLangage implements Language {

    @Override
    public void setUp(List<Module> modules) {
        //vérifier s'il y a déjà un ast dans smartest
        // sinon faire la suite
        List<String> srcDirPaths = new ArrayList<>();
        List<String> testDirPaths = new ArrayList<>();
        for (Module module : modules) {
            srcDirPaths.add(module.getSrcPath());
            testDirPaths.add(module.getTestPath());
        }
        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<File> javaFiles = directoryExplorer.explore(srcDirPaths);
        TreeFactory treeFactory = new TreeFactory();
        try {
            treeFactory.generateTrees(javaFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //faire la meme chose pour les tests

        throw new UnsupportedOperationException("setUp");
    }

    @Override
    public Set<Test> getTestsRelatedToChanges() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void update(List<String> list) {
        throw new UnsupportedOperationException("update");
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("save");
    }

    @Override
    public boolean accept(String s) {
        return s.toLowerCase().equals("java");
    }

}
