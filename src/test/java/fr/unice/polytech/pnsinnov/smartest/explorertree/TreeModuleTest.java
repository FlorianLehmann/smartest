package fr.unice.polytech.pnsinnov.smartest.explorertree;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.PluginEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TreeModuleTest {

    private TreeModule module;

    @BeforeEach
    public void setup(){
        module = new TreeModule.TreeModuleBuilder().withProjectBase(Paths.get("").toAbsolutePath().toString()).withPlugin(PluginEnum.MAVEN.getAssociatedPlugin()).build();
    }

    @Test
    void getPathsToModules() {
        assertEquals(1, module.getPathsToModules().size());
    }

}