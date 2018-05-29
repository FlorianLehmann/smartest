package fr.unice.polytech.pnsinnov.smartest.explorertree;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigReaderTest {

    private ConfigReader reader;

    @Before
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
