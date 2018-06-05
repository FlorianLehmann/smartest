package fr.unice.polytech.pnsinnov.smartest.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONConfigReaderTest {

    private JSONConfigReader reader;

    @BeforeEach
    public void setup(){
        reader = new JSONConfigReader();
    }

    @Test
    void readConfig() {
        Configuration normalConfig = reader.readConfig(Paths.get("target/test-classes/configuration/configTest.smt")
                .toAbsolutePath());

        assertEquals("this_is_java", normalConfig.language());
        assertEquals(Paths.get("target/test-classes/configuration/this_is_plugin").toAbsolutePath(), normalConfig
                .pluginPath());
        assertEquals("this_is_maven", normalConfig.productionTool());
        assertEquals("this_is_junit5", normalConfig.testFramework());
        assertEquals("this_is_git", normalConfig.vcs());
        assertEquals(Paths.get("target/test-classes/configuration/this_is_path_to_vcs").toAbsolutePath(),
                normalConfig.gitPath());
        assertEquals(Paths.get("target/test-classes/configuration/this_is_.").toAbsolutePath(), normalConfig
                .projectPath());

        Configuration emptyConfig = reader.readConfig(Paths.get("target/test-classes/configuration/emptyconfigTest" +
                ".smt").toAbsolutePath());

        assertEquals("java", emptyConfig.language());
        assertEquals(Paths.get("target/test-classes/configuration/plugins").toAbsolutePath(), emptyConfig.pluginPath());
        assertEquals("maven", emptyConfig.productionTool());
        assertEquals("junit5", emptyConfig.testFramework());
        assertEquals("git", emptyConfig.vcs());
        assertEquals(Paths.get("target/test-classes/configuration/.git").toAbsolutePath(), emptyConfig.gitPath());
        assertEquals(Paths.get("target/test-classes/configuration/").toAbsolutePath(), emptyConfig.projectPath());

        Configuration partial = reader.readConfig(Paths.get("target/test-classes/configuration/configTestMissing" +
                ".smt").toAbsolutePath());

        assertEquals("this_is_java", partial.language());
        assertEquals(Paths.get("target/test-classes/configuration/this_is_plugin").toAbsolutePath(), partial
                .pluginPath());
        assertEquals("this_is_maven", partial.productionTool());
        assertEquals("junit5", partial.testFramework());
        assertEquals("git", partial.vcs());
        assertEquals(Paths.get("target/test-classes/configuration/.git").toAbsolutePath(), partial.gitPath());
        assertEquals(Paths.get("target/test-classes/configuration/").toAbsolutePath(), partial.projectPath());
    }

}