package fr.unice.polytech.pnsinnov.smartest;

import fr.smartest.exceptions.PluginException;
import fr.smartest.exceptions.SmartestException;
import fr.smartest.exceptions.VCSException;
import fr.smartest.plugin.TestReport;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import fr.unice.polytech.pnsinnov.smartest.configuration.JSONConfigReader;
import fr.unice.polytech.pnsinnov.smartest.exceptions.TestFailureException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SmartestTest extends SuperClone {
    private Smartest smartest;
    private Path workingDir = directory.toPath().getParent().resolve("copy");
    private Path src = workingDir.resolve(Paths.get("src", "main", "java", "fr", "unice", "polytech", "pnsinnov"));

    @BeforeEach
    void setUp() throws IOException {
        FileUtils.copyDirectory(directory, workingDir.toFile());
        Configuration config = new JSONConfigReader().readConfig(workingDir.resolve("resources/config.smt"));
        smartest = new Smartest(config);
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(workingDir.toFile());
    }

    @Test
    void listTestsWithNoChanges() throws PluginException {
        Set<fr.smartest.plugin.Test> tests = smartest.listTests("Class");
        assertTrue(tests.isEmpty());
    }

    @Test
    void testsWithNoChanges() throws PluginException {
        Set<TestReport> tests = smartest.test("Class");
        assertTrue(tests.isEmpty());
    }

    @Test
    void commitWithNoChanges() throws SmartestException {
        assertThrows(VCSException.class, () -> smartest.commit("Class", "Message"));
    }

    @Test
    void listTestsDeleteUntestedFile() throws PluginException, IOException {
        Path mainPath = src.resolve("Main.java");
        FileUtils.forceDelete(new File(mainPath.toUri()));
        Set<fr.smartest.plugin.Test> tests = smartest.listTests("Class");
        assertTrue(tests.isEmpty());
    }

    @Test
    void testsWithDeleteUntestedFile() throws PluginException, IOException {
        Path mainPath = src.resolve("Main.java");
        FileUtils.forceDelete(new File(mainPath.toUri()));
        Set<TestReport> tests = smartest.test("Class");
        assertTrue(tests.isEmpty());
    }

    @Test
    void commitWithDeleteUntestedFile() throws SmartestException, IOException {
        Path mainPath = src.resolve("Main.java");
        FileUtils.forceDelete(new File(mainPath.toUri()));
        try {
            smartest.commit("Class", "Message");
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    void listTestsModifyTestedFile() throws PluginException, IOException {
        modifyStudentSoItIsNotSmart();
        Set<fr.smartest.plugin.Test> tests = smartest.listTests("Class");
        assertEquals(2, tests.size());
    }

    @Test
    void testsWithModifyTestedFile() throws PluginException, IOException {
        modifyStudentSoItIsNotSmart();
        Set<TestReport> tests = smartest.test("Class");
        assertEquals(2, tests.size());
    }

    @Test
    void commitWithModifyTestedFile() throws SmartestException, IOException {
        modifyStudentSoItIsNotSmart();
        assertThrows(TestFailureException.class, () -> smartest.commit("Class", "Message"));
    }

    @Test
    void listTestsModifySimplyTestedFile() throws PluginException, IOException {
        PaulIsAlwaysInTheSchool();
        Set<fr.smartest.plugin.Test> tests = smartest.listTests("Class");
        assertEquals(1, tests.size());
    }

    @Test
    void testsWithModifySimplyTestedFile() throws PluginException, IOException {
        PaulIsAlwaysInTheSchool();
        Set<TestReport> tests = smartest.test("Class");
        assertEquals(1, tests.size());
    }

    @Test
    void commitWithModifySimplyTestedFile() throws SmartestException, IOException {
        PaulIsAlwaysInTheSchool();
        try {
            smartest.commit("Class", "Message");
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    void commitSuccessfully() throws SmartestException, IOException {
        modifyStudentSoItIsNotSmart();
        modifyStudentSoItIsSmartAgain();
        try {
            smartest.commit("Class", "Message");
        }
        catch (Exception e) {
            fail();
        }
    }

    private void modifyStudentSoItIsSmartAgain() throws FileNotFoundException, UnsupportedEncodingException {
        Path studentPath = src.resolve("Student.java");
        PrintWriter writer = new PrintWriter(studentPath.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;public class Student {private String name;public Student" +
                "(String name) {this.name = name;}public boolean isSmart() {return true;}}");
        writer.close();
    }

    private void modifyStudentSoItIsNotSmart() throws FileNotFoundException, UnsupportedEncodingException {
        Path studentPath = src.resolve("Student.java");
        PrintWriter writer = new PrintWriter(studentPath.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;public class Student {private String name;public Student" +
                "(String name) {this.name = name;}public boolean isSmart() {return false;}}");
        writer.close();
    }

    private void PaulIsAlwaysInTheSchool() throws FileNotFoundException, UnsupportedEncodingException {
        Path school = src.resolve("School.java");
        PrintWriter writer = new PrintWriter(school.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;import java.util.ArrayList;import java.util.List;public " +
                "class School {private List<Student> students;public School() {this.students = new ArrayList<Student>" +
                "();this.students.add(new Student(\"Paul\"));}public void enrollStudent(Student student) {students" +
                ".add(student);}}");
        writer.close();
    }
}