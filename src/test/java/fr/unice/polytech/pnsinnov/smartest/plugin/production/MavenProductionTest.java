package fr.unice.polytech.pnsinnov.smartest.plugin.production;

import fr.smartest.exceptions.ProductionToolException;
import fr.smartest.plugin.Module;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MavenProductionTest extends SuperClone {

    private MavenProduction production;

    @BeforeEach
    public void setup(){
        production = new MavenProduction();
        production.setUp(directory.toPath());
    }

    @Test
    void getModules() {

        assertEquals(1, production.getModules().size());

        if(production.getModules().size() >= 1){
            Module module = production.getModules().get(0);

            assertEquals(Paths.get("src/test/resources/tmp/src/main/java").toAbsolutePath(), module.getSrcPath());
            assertEquals(Paths.get("src/test/resources/tmp/src/test/java").toAbsolutePath(), module.getTestPath());
        }

    }

    @Test
    void compile() throws ProductionToolException {
        production.compile();

        File srcFolder = new File(directory.getAbsolutePath(), "src/main/java/fr/unice/polytech/pnsinnov");
        File srcOutPut = new File(directory.getAbsolutePath(), "target/classes/fr/unice/polytech/pnsinnov");

        assertTrue(srcOutPut.exists());

        assert srcFolder.list() != null;

        assertEquals(srcFolder.list().length, srcOutPut.list().length);

        List<String> srcF = new ArrayList<>();
        for (String src : srcFolder.list()) {
            srcF.add(src.replace(".java", ".class"));
        }
        List<String> srcO = new ArrayList<>(Arrays.asList(srcOutPut.list()));
        assertTrue(srcF.containsAll(srcO));
        assertTrue(srcO.containsAll(srcF));

    }

}