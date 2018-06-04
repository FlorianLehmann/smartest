package fr.unice.polytech.pnsinnov.smartest.plugin.production;

import fr.smartest.exceptions.ProductionToolException;
import fr.smartest.plugin.Module;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MavenProductionTest extends SuperClone {

    private MavenProduction production;

    @BeforeEach
    public void setup(){
        production = new MavenProduction();
        production.setUp(SuperClone.directory.getAbsolutePath());
    }

    @Test
    void getModules() {

        assertEquals(1, production.getModules().size());

        if(production.getModules().size() >= 1){
            Module module = production.getModules().get(0);

            assertEquals("src/main/java", module.getSrcPath());
            assertEquals("src/test/java", module.getTestPath());
        }

    }

    @Test
    void compile() throws ProductionToolException {
        production.compile();

        File srcFolder = new File(SuperClone.directory.getAbsolutePath(), "src/main/java/fr/unice/polytech/pnsinnov");
        File srcOutPut= new File(SuperClone.directory.getAbsolutePath(),"target/classes/fr/unice/polytech/pnsinnov");

        assertTrue(srcOutPut.exists());

        assert srcFolder.list() != null;

        assertEquals(srcFolder.list().length, srcOutPut.list().length);

        for (int i = 0; i < srcFolder.list().length; i++) {
            assertEquals(srcFolder.list()[i].replaceFirst(".java", ""), srcOutPut.list()[i].replace(".class", ""));
        }

    }

}