package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.exceptions.VCSException;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
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
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaMethodTest extends SuperClone {
    private MavenProduction mavenProduction;
    private JavaMethod language;
    private Path workingDir = directory.toPath().getParent().resolve("copy3").toAbsolutePath();
    private Path src = workingDir.resolve(Paths.get("src", "main", "java", "fr", "unice", "polytech", "pnsinnov"))
            .toAbsolutePath();
    private GitVCS gitVCS;

    public JavaMethodTest() {
        setCommit("ff4484a03a57a585f9fb0edae31d3b2f1a3386d8");
    }

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        FileUtils.copyDirectory(directory, workingDir.toFile());
        mavenProduction = new MavenProduction();
        mavenProduction.setUp(workingDir);
        language = new JavaMethod();
        gitVCS = new GitVCS();
        gitVCS.setUp(workingDir.resolve(".git").toAbsolutePath(), workingDir);
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(workingDir.toFile());
    }

    @Test
    void noModification() throws VCSException {
        language.setUp(mavenProduction.getModules());
        assertTrue(language.getTestsRelatedToChanges("method_with_polymorphism", gitVCS.diff()).isEmpty());
    }

    @Test
    void testPolymorphismOnStudent() throws IOException, VCSException, GitAPIException {
        Path badStudent = src.resolve("BadStudent.java");
        PrintWriter writer = new PrintWriter(badStudent.toFile(), "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;public class BadStudent extends Student {public BadStudent" +
                "(String name) {super(name);}@Override public boolean isSmart() {return true;}}");
        writer.close();
        Git git = Git.open(workingDir.resolve(".git").toFile());
        git.add().addFilepattern(badStudent.toString()).call();
        git.close();
        language.setUp(mavenProduction.getModules());
        Set<fr.smartest.plugin.Test> expected = new HashSet<>();
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.BadStudentTest#isNotSmart"));
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest#allSmart"));
        expected.add(new TestImplementation("fr.unice.polytech.pnsinnov.StudentTest#shouldCreateAStudent"));
        assertEquals(expected, language.getTestsRelatedToChanges("method_with_polymorphism", gitVCS.diff()));
    }
}