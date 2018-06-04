package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.factory;

import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.DirectoryExplorer;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Tree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeFactoryTest extends SuperClone {

    private TreeFactory treeFactory;
    private List<Tree> trees;
    private List<File> files;
    private List<String> classNames;

    @BeforeEach
    public void defineContext() {
        trees = new ArrayList<>();
        treeFactory = new TreeFactory(trees);
        classNames = new ArrayList<>();
        classNames.add("fr.unice.polytech.pnsinnov.Main");
        classNames.add("fr.unice.polytech.pnsinnov.School");
        classNames.add("fr.unice.polytech.pnsinnov.Student");
        files = new DirectoryExplorer().explore(SuperClone.directory.getAbsolutePath() + "/src/main/java");
    }

    @Test
    public void shouldBuildTreesOnSimpleProject() throws IOException {
        treeFactory.generateTrees(files);
        assertEquals(3, trees.size());

        List<String> actualClassNames = new ArrayList<>();
        for (Tree tree : trees) {
            actualClassNames.add(tree.getCls().getName());
        }

        assertTrue(classNames.containsAll(actualClassNames));
    }

}