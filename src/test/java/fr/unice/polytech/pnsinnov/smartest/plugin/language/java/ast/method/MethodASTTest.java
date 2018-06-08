package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.exceptions.VCSException;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import fr.unice.polytech.pnsinnov.smartest.configuration.JSONConfigReader;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.diff.TestImplementation;
import fr.unice.polytech.pnsinnov.smartest.plugin.production.MavenProduction;
import fr.unice.polytech.pnsinnov.smartest.plugin.vcs.GitVCS;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodASTTest extends SuperClone {
    private MavenProduction mavenProduction;
    private MethodAST language;
    private Path workingDir = SuperClone.directory.toPath().getParent().resolve("copy2").toAbsolutePath();
    private Path src = workingDir.resolve(Paths.get("src", "main", "java", "fr", "unice", "polytech", "pnsinnov"))
            .toAbsolutePath();

    @BeforeEach
    void setUp() throws IOException {
        FileUtils.copyDirectory(SuperClone.directory, workingDir.toFile());
        Configuration config = new JSONConfigReader().readConfig(workingDir.resolve("resources/config.smt"));
        mavenProduction = new MavenProduction();
        mavenProduction.setUp(workingDir);
        language = new MethodAST();
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(workingDir.toFile());
    }

    @Test
    void changeSchool() throws IOException, VCSException, GitAPIException {
        Path school = src.resolve("School.java");
        PrintWriter writer = new PrintWriter(school.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;import java.util.LinkedList;import java.util.List;public " +
                "class School {private List<Student> students;public School() {this.students = new " +
                "LinkedList<Student>();}public void enrollStudent(Student student) {students.add(student);}" +
                "public void fireStudent(Student student) {students.remove(student);}}");
        writer.close();
        Git git = Git.open(workingDir.resolve(".git").toFile());
        git.add().addFilepattern(school.toString()).call();
        git.close();
        GitVCS gitVCS = new GitVCS();
        gitVCS.setUp(workingDir.resolve(".git").toAbsolutePath(), workingDir);
        language.setUp(mavenProduction.getModules());
        Set<fr.smartest.plugin.Test> tests = language.getTestsRelatedToChanges("best", gitVCS.diff());
        Set<fr.smartest.plugin.Test> expected = new HashSet<>();
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest#shouldEnrollAStudent"));
        assertEquals(expected, tests);
    }

    @Test
    void changeStudent() throws IOException, VCSException, GitAPIException {
        boolean b = new Random().nextBoolean();
        Path studentPath = src.resolve("Student.java");
        PrintWriter writer = new PrintWriter(studentPath.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;public class Student {private String name;public Student" +
                "(String name) {this.name = name;}public boolean isSmart() {return false;}}");
        writer.close();
        Git git = Git.open(workingDir.resolve(".git").toFile());
        git.add().addFilepattern(studentPath.toString()).call();
        git.close();
        GitVCS gitVCS = new GitVCS();
        gitVCS.setUp(workingDir.resolve(".git"), workingDir);
        language.setUp(mavenProduction.getModules());
        Set<fr.smartest.plugin.Test> tests = language.getTestsRelatedToChanges("best", gitVCS.diff());
        Set<fr.smartest.plugin.Test> expected = new HashSet<>();
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.StudentTest#shouldCreateAStudent"));
        assertEquals(expected, tests);
    }
}