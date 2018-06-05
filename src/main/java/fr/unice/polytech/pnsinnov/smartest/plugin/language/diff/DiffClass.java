package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.factory.TreeFactory;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Dependency;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Tree;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence.Database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiffClass implements Diff {

    private Set<fr.smartest.plugin.Diff> fileDiff;

    public DiffClass(Set<fr.smartest.plugin.Diff> fileDiff) {
        this.fileDiff = fileDiff;
    }

    @Override
    public Set<fr.smartest.plugin.Test> getTestsRelatedToChanges(List<Module> modules) throws IOException {
        Database database = Database.getInstance();
        List<Tree> tests = database.getTests();
        List<Tree> oldSrcClass = database.getTree();
        List<String> oldClassNames = database.getClassName();

        List<Tree> newSrcClass = new ArrayList<>();
        new TreeFactory(newSrcClass).generateTrees(getSourceFileModifiedOrAdded(modules), getSourceFileModifiedOrAdded(modules));
        // on ne différencie pas les tests du code source

        List<String> classNames = new ArrayList<>();
        for (Tree tree : newSrcClass) {
            classNames.add(tree.getCls().getName());
        }


        Set<fr.smartest.plugin.Test> toRun = new HashSet<>();
        for (Tree tree:tests) {
            for (String name :classNames) {
                if (tree.getCls().getAllDependencies().contains(new Dependency(name))) {
                    toRun.add(new TestImplementation(tree.getCls().getName()));
                }
            }
        }

        //WARNING il ajoute le nom dans les classes
        database.setClassName(oldClassNames);
        //fileDiff.forEach(diff -> System.out.println(diff.getPath()));

        return toRun;

    }

    private List<File> getSourceFileModifiedOrAdded(List<Module> modules) {
        List<File> files = new ArrayList<>();
        fileDiff.forEach(fileDiff -> {
            if (fileDiff.getStatus().equals(fr.smartest.plugin.Diff.Status.ADDED) ||
                    fileDiff.getStatus().equals(fr.smartest.plugin.Diff.Status.MODIFIED)) {
                /*for (Module module : modules) {
                    if (fileDiff.getPath().contains(module.getSrcPath())) {
                        System.out.println("TEST" + fileDiff.getPath());*/
                        files.add(new File(fileDiff.getPath()));
                    /*}
                }*/
            }
        });
        return files;
    }
}
