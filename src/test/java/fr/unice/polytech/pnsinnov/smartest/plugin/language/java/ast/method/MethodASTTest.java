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
    private JavaMethod language;
    private Path workingDir = directory.toPath().getParent().resolve("copy2").toAbsolutePath();
    private Path src = workingDir.resolve(Paths.get("src", "main", "java", "fr", "unice", "polytech", "pnsinnov"))
            .toAbsolutePath();

    public MethodASTTest() {
        setCommit("49027274b793549855864cc9db2aca85f4cfaeb4");
    }

    @BeforeEach
    void setUp() throws IOException {
        FileUtils.copyDirectory(directory, workingDir.toFile());
        Configuration config = new JSONConfigReader().readConfig(workingDir.resolve("resources/config.smt"));
        mavenProduction = new MavenProduction();
        mavenProduction.setUp(workingDir);
        language = new JavaMethod();
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(workingDir.toFile());
    }

    @Test
    void addMethodToSchool() throws IOException, VCSException, GitAPIException {
        Path school = src.resolve("School.java");
        PrintWriter writer = new PrintWriter(school.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;import java.util.ArrayList;import java.util.List;public " +
                "class School {private List<Student> students;private Teacher tot;private String e;public School() " +
                "{this.students = new ArrayList<Student>();}public void enrollStudent(Student student) {students.add" +
                "(student);}public void fireStudent(Student student) {students.remove(student);}public boolean " +
                "allSmart() {for (Student student : students) {if (!student.isSmart()) {return false;}}return true;}}");
        writer.close();
        Git git = Git.open(workingDir.resolve(".git").toFile());
        git.add().addFilepattern(school.toString()).call();
        git.close();
        GitVCS gitVCS = new GitVCS();
        gitVCS.setUp(workingDir.resolve(".git").toAbsolutePath(), workingDir);
        language.setUp(mavenProduction.getModules());
        Set<fr.smartest.plugin.Test> tests = language.getTestsRelatedToChanges("method", gitVCS.diff());
        assertEquals(new HashSet<>(), tests);
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
        Set<fr.smartest.plugin.Test> tests = language.getTestsRelatedToChanges("method", gitVCS.diff());
        Set<fr.smartest.plugin.Test> expected = new HashSet<>();
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.StudentTest#shouldCreateAStudent"));
        assertEquals(expected, tests);
    }


    @Test
    void changeStudentWithDependencies() throws IOException, VCSException, GitAPIException {
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
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest#allSmart"));
        assertEquals(expected, tests);
    }

    @Test
    void changeStudentConstructorWithDependencies() throws IOException, VCSException, GitAPIException {
        boolean b = new Random().nextBoolean();
        Path studentPath = src.resolve("Student.java");
        PrintWriter writer = new PrintWriter(studentPath.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;public class Student {private String name;public Student" +
                "(String name) {this.name = name + '!';}public boolean isSmart() {return true;}}");
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
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest#allSmart"));
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest#shouldEnrollAStudent"));
        assertEquals(expected, tests);
    }


    @Test
    void changeSchoolConstructor() throws IOException, VCSException, GitAPIException {
        Path school = src.resolve("School.java");
        PrintWriter writer = new PrintWriter(school.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;import java.util.LinkedList;import java.util.List;public " +
                "class School {private List<Student> students;private Teacher tot;private String e;public School() " +
                "{this.students = new LinkedList<Student>();}public void enrollStudent(Student student) {students.add" +
                "(student);}public boolean allSmart() {for (Student student : students) {if (!student.isSmart()) " +
                "{return false;}}return true;}}");
        writer.close();
        Git git = Git.open(workingDir.resolve(".git").toFile());
        git.add().addFilepattern(school.toString()).call();
        git.close();
        GitVCS gitVCS = new GitVCS();
        gitVCS.setUp(workingDir.resolve(".git").toAbsolutePath(), workingDir);
        language.setUp(mavenProduction.getModules());
        Set<fr.smartest.plugin.Test> tests = language.getTestsRelatedToChanges("method", gitVCS.diff());
        Set<fr.smartest.plugin.Test> expected = new HashSet<>();
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest#shouldEnrollAStudent"));
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest#allSmart"));
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest#allCoursesAreUseful"));
        assertEquals(expected, tests);
    }

    @Test
    void changeTeacher() throws IOException, VCSException, GitAPIException {
        Path teacher = src.resolve("Teacher.java");
        PrintWriter writer = new PrintWriter(teacher.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;public class Teacher {Course a;public int factorial(int i)" +
                " {if (i < 0) {return 0;}if (i == 0) {return 0;}return i * factorial(i - 1);}}");
        writer.close();
        Git git = Git.open(workingDir.resolve(".git").toFile());
        git.add().addFilepattern(teacher.toString()).call();
        git.close();
        GitVCS gitVCS = new GitVCS();
        gitVCS.setUp(workingDir.resolve(".git").toAbsolutePath(), workingDir);
        language.setUp(mavenProduction.getModules());
        Set<fr.smartest.plugin.Test> tests = language.getTestsRelatedToChanges("method", gitVCS.diff());
        Set<fr.smartest.plugin.Test> expected = new HashSet<>();
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.TeacherTest#testFactorial"));
        assertEquals(expected, tests);
    }

    @Test
    void changeTeacherWithDependencies() throws IOException, VCSException, GitAPIException {
        Path teacher = src.resolve("Teacher.java");
        PrintWriter writer = new PrintWriter(teacher.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;public class Teacher {Course a;public int factorial(int i)" +
                " {if (i < 0) {return 0;}if (i == 0) {return 0;}return i * factorial(i - 1);}}");
        writer.close();
        Git git = Git.open(workingDir.resolve(".git").toFile());
        git.add().addFilepattern(teacher.toString()).call();
        git.close();
        GitVCS gitVCS = new GitVCS();
        gitVCS.setUp(workingDir.resolve(".git").toAbsolutePath(), workingDir);
        language.setUp(mavenProduction.getModules());
        Set<fr.smartest.plugin.Test> tests = language.getTestsRelatedToChanges("best", gitVCS.diff());
        Set<fr.smartest.plugin.Test> expected = new HashSet<>();
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.TeacherTest#testFactorial"));
        assertEquals(expected, tests);
    }
}