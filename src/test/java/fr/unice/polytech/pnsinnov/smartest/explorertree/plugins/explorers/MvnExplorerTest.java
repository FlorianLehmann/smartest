package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MvnExplorerTest {

    private MvnExplorer mvnExplorer;

    @BeforeEach
    public void setup(){
        mvnExplorer = new MvnExplorer();
    }

    @Test
    public void findAllModules() {
        assertEquals(1, mvnExplorer.findAllModules(new File(Paths.get("").toAbsolutePath().toString())).size());
    }

    @Test
    public void pathToSrc(){
        assertEquals("here/i/am", mvnExplorer.getPathToSrc("target/test-classes/pom"));
        assertEquals("src/main/java", mvnExplorer.getPathToSrc("target/test-classes/pomWithoutBuild"));
        assertEquals("this/is/src", mvnExplorer.getPathToSrc("target/test-classes/pomWithProp"));
    }

}