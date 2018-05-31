package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;


import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers.MvnExplorer;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners.JUnitRunner;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import spoon.reflect.declaration.CtClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MvnPlugin extends Plugin{

    @Override
    void setupExplorer() {
        this.explorerStrategy = new MvnExplorer();
    }

    @Override
    void setupRunner() {
        this.testRunner = new JUnitRunner();
    }

    @Override
    public boolean accept(String langageFromConfig, String testFrameworkFromConfig, Optional<String> managementFromConfig) {
        //TODO ADD LOGIC

        return true;
    }

    @Override
    public boolean isValidPath(String path) {
        return path.endsWith(".java");
    }

    @Override
    public Set<String> compareChanges(String path, String previous) {
        Set<String> toRun = new HashSet<>();

        if(previous == null){
            previous = "public class " + path.split("/")[path.split("/").length - 1] + "{}";
        }

        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            return toRun;
        }

        String current = new String(encoded);

        Diff result = new AstComparator().compare(previous, current);

        result.getAllOperations().forEach(operation -> {
            try {
                toRun.add(operation.getSrcNode().getParent(CtClass.class).getQualifiedName());
            } catch (NullPointerException ignored){
                //Non-CtClass object detected, skipped (@interface, enum....)
            }
        });

        return toRun;
    }
}
