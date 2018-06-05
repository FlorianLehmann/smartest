package fr.unice.polytech.pnsinnov.smartest.plugin.production;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MavenModuleTest {

    private MavenModule mixedModule;
    private MavenModule noBuildModule;
    private MavenModule propModule;

    @BeforeEach
    public void setup(){
        mixedModule = new MavenModule(Paths.get("target/test-classes/pom"));
        noBuildModule = new MavenModule(Paths.get("target/test-classes/pomWithoutBuild"));
        propModule = new MavenModule(Paths.get("target/test-classes/pomWithProp"));
    }


    @Test
    void getSrcPath() {
        assertEquals(Paths.get("target/test-classes/pom/here/i/am").toAbsolutePath(), mixedModule.getSrcPath());
        assertEquals(Paths.get("target/test-classes/pomWithoutBuild/src/main/java").toAbsolutePath(), noBuildModule.getSrcPath());
        assertEquals(Paths.get("target/test-classes/pomWithProp/this/is/src").toAbsolutePath(), propModule.getSrcPath());
    }

    @Test
    void getTestPath() {
        assertEquals(Paths.get("target/test-classes/pom/here/is/test").toAbsolutePath(), mixedModule.getTestPath());
        assertEquals(Paths.get("target/test-classes/pomWithoutBuild/src/test/java").toAbsolutePath(), noBuildModule.getTestPath());
        assertEquals(Paths.get("target/test-classes/pomWithProp/this/is/test").toAbsolutePath(), propModule.getTestPath());
    }

    @Test
    void getCompiledSrcPath() {
        assertEquals(Paths.get("target/test-classes/pom/just/usual/target").toAbsolutePath(), mixedModule.getCompiledSrcPath());
        assertEquals(Paths.get("target/test-classes/pomWithoutBuild/target/classes").toAbsolutePath(), noBuildModule.getCompiledSrcPath());
        assertEquals(Paths.get("target/test-classes/pomWithProp/compiled/code/here").toAbsolutePath(), propModule.getCompiledSrcPath());
    }

    @Test
    void getCompiledTestPath() {
        assertEquals(Paths.get("target/test-classes/pom/launch/test/here").toAbsolutePath(), mixedModule.getCompiledTestPath());
        assertEquals(Paths.get("target/test-classes/pomWithoutBuild/target/test-classes").toAbsolutePath(), noBuildModule.getCompiledTestPath());
        assertEquals(Paths.get("target/test-classes/pomWithProp/launch/test/here").toAbsolutePath(), propModule.getCompiledTestPath());
    }

}