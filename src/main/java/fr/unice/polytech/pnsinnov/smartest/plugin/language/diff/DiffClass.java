package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

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
    public Set<fr.smartest.plugin.Test> getTestsRelatedToChanges() throws IOException {
        Database database = Database.getInstance();
        List<Tree> tests = database.getTests();
        List<Tree> oldSrcClass = database.getTree();

        List<Tree> newSrcClass = new ArrayList<>();
        new TreeFactory(newSrcClass).generateTrees(getFileModifiedOrAdded(), getFileModifiedOrAdded());
        //WARNING il ajoute le nom dans les classes
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

        return toRun;

    }

    private List<File> getFileModifiedOrAdded() {
        List<File> files = new ArrayList<>();
        fileDiff.forEach(fileDiff -> {
            if (fileDiff.getStatus().equals(fr.smartest.plugin.Diff.Status.ADDED) ||
                    fileDiff.getStatus().equals(fr.smartest.plugin.Diff.Status.MODIFIED)) {
                files.add(new File(fileDiff.getPath()));
            }
        });
        return files;
    }
}
