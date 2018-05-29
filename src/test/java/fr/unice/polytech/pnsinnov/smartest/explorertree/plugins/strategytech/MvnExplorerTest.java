package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.strategytech;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MvnExplorerTest {
    @Test
    public void findAllModules() {
        MvnExplorer mvnExplorer = new MvnExplorer();

        assertEquals(1, mvnExplorer.findAllModules(new File(Paths.get("").toAbsolutePath().toString())).size());
    }

}