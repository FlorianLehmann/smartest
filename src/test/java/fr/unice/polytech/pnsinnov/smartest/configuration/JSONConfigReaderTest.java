package fr.unice.polytech.pnsinnov.smartest.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONConfigReaderTest {

    private JSONConfigReader reader;

    @BeforeEach
    public void setup(){
        reader = new JSONConfigReader();
    }

    @Test
    void readConfig() {
        Configuration normalConfig = reader.readConfig("target/test-classes//configuration/configTest.smt");

        assertEquals("this_is_java", normalConfig.language());
        assertEquals("this_is_plugin", normalConfig.pluginPath());
        assertEquals("this_is_maven", normalConfig.productionTool());
        assertEquals("this_is_junit5", normalConfig.testFramework());
        assertEquals("this_is_git", normalConfig.vcs());
        assertEquals("this_is_path_to_vcs", normalConfig.gitPath());
        assertEquals("this_is_.", normalConfig.projectPath());

        Configuration emptyConfig = reader.readConfig("target/test-classes//configuration/emptyconfigTest.smt");

        assertEquals("java", emptyConfig.language());
        assertEquals("plugins", emptyConfig.pluginPath());
        assertEquals("maven", emptyConfig.productionTool());
        assertEquals("junit5", emptyConfig.testFramework());
        assertEquals("git", emptyConfig.vcs());
        assertEquals(".git", emptyConfig.gitPath());
        assertEquals(".", emptyConfig.projectPath());

        Configuration partial = reader.readConfig("target/test-classes//configuration/configTestMissing.smt");

        assertEquals("this_is_java", partial.language());
        assertEquals("this_is_plugin", partial.pluginPath());
        assertEquals("this_is_maven", partial.productionTool());
        assertEquals("junit5", partial.testFramework());
        assertEquals("git", partial.vcs());
        assertEquals(".git", partial.gitPath());
        assertEquals(".", partial.projectPath());
    }

}