package fr.unice.polytech.pnsinnov.smartest.parser;



import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class DatabaseTest {

    private Database database;

    @Before
    public void defineContext() {
        database = Database.getInstance();
    }

    @Test
    public void shouldAddAClassAWithNoLink() {
        database.addClass("A");
        assertTrue(database.contain("A"));
        assertEquals(new HashSet<>(), database.getTestLinkToClass("A"));
    }

    @Test
    public void shouldAddAClassAWithALink() {
        database.addClass("A");
        database.linkClassToTest("A", "ATest");
        assertTrue(database.contain("A"));
        HashSet<String> tests = new HashSet<>();
        tests.add("ATest");
        assertEquals(tests, database.getTestLinkToClass("A"));
    }

    @Test
    public void shouldBeTheSameInstance() {
        Database d1 = Database.getInstance();
        Database d2 = Database.getInstance();
        assertTrue(d1 == d2);
    }

}