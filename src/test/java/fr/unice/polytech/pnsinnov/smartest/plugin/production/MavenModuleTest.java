package fr.unice.polytech.pnsinnov.smartest.plugin.production;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MavenModuleTest {

    private MavenModule mixedModule;
    private MavenModule noBuildModule;
    private MavenModule propModule;

    @BeforeEach
    public void setup(){
        mixedModule = new MavenModule("target/test-classes/pom");
        noBuildModule = new MavenModule("target/test-classes/pomWithoutBuild");
        propModule = new MavenModule("target/test-classes/pomWithProp");
    }


    @Test
    void getSrcPath() {
        assertEquals("here/i/am", mixedModule.getSrcPath());
        assertEquals("src/main/java", noBuildModule.getSrcPath());
        assertEquals("this/is/src", propModule.getSrcPath());
    }

    @Test
    void getTestPath() {
        assertEquals("here/is/test", mixedModule.getTestPath());
        assertEquals("src/test/java", noBuildModule.getTestPath());
        assertEquals("this/is/test", propModule.getTestPath());
    }

    @Test
    void getCompiledSrcPath() {
        assertEquals("just/usual/target", mixedModule.getCompiledSrcPath());
        assertEquals("target/classes", noBuildModule.getCompiledSrcPath());
        assertEquals("compiled/code/here", propModule.getCompiledSrcPath());
    }

    @Test
    void getCompiledTestPath() {
        assertEquals("launch/test/here", mixedModule.getCompiledTestPath());
        assertEquals("target/test-classes", noBuildModule.getCompiledTestPath());
        assertEquals("launch/test/here", propModule.getCompiledTestPath());
    }

}