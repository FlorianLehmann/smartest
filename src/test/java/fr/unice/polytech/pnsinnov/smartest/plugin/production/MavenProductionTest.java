package fr.unice.polytech.pnsinnov.smartest.plugin.production;

import fr.smartest.plugin.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MavenProductionTest {

    private MavenProduction production;

    @BeforeEach
    public void setup(){
        production = new MavenProduction();
        production.setUp(Paths.get("").toAbsolutePath().toString());
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

}