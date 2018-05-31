package fr.unice.polytech.pnsinnov.smartest.explorertree;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigReaderTest {

    private ConfigReader reader;

    @BeforeEach
    public void setup(){
        reader = new ConfigReader("./resources/config.smt");
    }

    @Test
    public void getLangageFromConfigTest(){
        assertEquals("java", reader.getLangageFromConfig());
    }

    @Test
    public void getTestFrameworkFromConfigTest(){
        assertEquals("junit", reader.getTestFrameworkFromConfig());
    }

    @Test
    public void getManagementFromConfigTest(){
        assertTrue(reader.getManagementFromConfig().isPresent());

        assertEquals("maven", reader.getManagementFromConfig().get());
    }

}
