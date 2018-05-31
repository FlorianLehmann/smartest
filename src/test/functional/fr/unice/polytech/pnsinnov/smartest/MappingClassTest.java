package fr.unice.polytech.pnsinnov.smartest;



import fr.unice.polytech.pnsinnov.smartest.parser.Database;
import fr.unice.polytech.pnsinnov.smartest.parser.Parser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MappingClassTest {

    private Parser parser;
    private Database database;
    private Runtime runtime;

    @BeforeAll
    public void defineContext() throws IOException, InterruptedException {
        database = Database.getInstance();
        runtime = Runtime.getRuntime();
        Process process = runtime.exec("mkdir -p tmp/test");
        process.waitFor();
        process = runtime.exec("git clone https://lf612146@mjollnir.unice.fr/bitbucket/scm/pnse/sample_project.git tmp/test");
        process.waitFor();
        parser = new Parser("tmp/test/src/main/java","tmp/test/src/test/java");
        parser.sourceCodeParsing();
        parser.testsParsing();
    }

    @Test
    public void shouldFindAllClass() throws IOException, InterruptedException {
        parser.sourceCodeParsing();
        assertTrue(database.contain("fr.unice.polytech.pnsinnov.Main"));
        assertTrue(database.contain("fr.unice.polytech.pnsinnov.School"));
        assertTrue(database.contain("fr.unice.polytech.pnsinnov.Student"));
    }

    @Test
    public void shouldLinkTestWithClass() {
        Set<String> mainLinks = new HashSet<>();
        Set<String> schoolLinks = new HashSet<>();
        schoolLinks.add("fr.unice.polytech.pnsinnov.SchoolTest");
        Set<String> studentLinks = new HashSet<>();
        studentLinks.add("fr.unice.polytech.pnsinnov.SchoolTest");
        studentLinks.add("fr.unice.polytech.pnsinnov.StudentTest");

        assertEquals(mainLinks ,    database.getTestLinkToClass("fr.unice.polytech.pnsinnov.Main"));
        assertEquals(schoolLinks ,  database.getTestLinkToClass("fr.unice.polytech.pnsinnov.School"));
        assertEquals(studentLinks , database.getTestLinkToClass("fr.unice.polytech.pnsinnov.Student"));
    }

    @AfterAll
    public void cleanContext() throws IOException, InterruptedException {
        Process process = runtime.exec("rm -rf tmp");
        process.waitFor();
    }

}
