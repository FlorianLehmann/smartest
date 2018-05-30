package fr.unice.polytech.pnsinnov.smartest.parser;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTest {

    private Database database;

    @BeforeEach
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