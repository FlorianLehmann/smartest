package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.factory.TreeFactory;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Tree;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence.Database;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DiffClass implements Diff {

    private List<File> fileDiff;

    public DiffClass(Set<String> fileDiff) {
        this.fileDiff = new ArrayList<>();
        fileDiff.forEach(path -> this.fileDiff.add(new File(path)));
    }

    @Override
    public Set<Test> getTestsRelatedToChanges() throws IOException {
        Database database = Database.getInstance();
        List<Tree> tests = database.getTests();
        List<Tree> oldSrcClass = database.getTree();

        List<Tree> newSrcClass = new ArrayList<>();
        new TreeFactory(newSrcClass).generateTrees(fileDiff);

        throw new UnsupportedEncodingException();
    }
}
