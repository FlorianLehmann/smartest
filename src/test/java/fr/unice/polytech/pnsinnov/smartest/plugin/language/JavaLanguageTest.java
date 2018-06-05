package fr.unice.polytech.pnsinnov.smartest.plugin.language;

import fr.smartest.plugin.Diff;
import fr.smartest.plugin.Module;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.diff.TestImplementation;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaLanguageTest extends SuperClone {

    private JavaLanguage javaLanguage;

    @BeforeEach
    public void defineContext() {
        javaLanguage = new JavaLanguage();
        Database.getInstance().flush();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateTree() {
        Module module = Mockito.mock(Module.class);
        List<Module> modules = new ArrayList<>();
        Mockito.when(module.getSrcPath()).thenReturn(SuperClone.directory.getAbsolutePath() + "/src/main/java");
        Mockito.when(module.getTestPath()).thenReturn(SuperClone.directory.getAbsolutePath() + "/src/test/java");
        modules.add(module);
        javaLanguage.setUp(modules);

        List<String> actualClassName = new ArrayList<>();
        List<String> expectedClassName = Arrays.asList("fr.unice.polytech.pnsinnov.School", "fr.unice.polytech.pnsinnov.Main", "fr.unice.polytech.pnsinnov.Student");
        Database.getInstance().getTree().forEach(tree -> actualClassName.add(tree.getCls().getName()));
        assertTrue(actualClassName.containsAll(expectedClassName));
        assertTrue(expectedClassName.containsAll(actualClassName));

        List<String> actualTestsClassName = new ArrayList<>();
        List<String> expectedTestsClassName = Arrays.asList("fr.unice.polytech.pnsinnov.SchoolTest", "fr.unice.polytech.pnsinnov.StudentTest");
        Database.getInstance().getTests().forEach(tree -> actualTestsClassName.add(tree.getCls().getName()));
        assertTrue(actualTestsClassName.containsAll(expectedTestsClassName));
        assertTrue(expectedTestsClassName.containsAll(actualTestsClassName));

    }

    @Test
    public void shouldNotGetTestsRelatedToAddedFile() throws IOException {
        shouldCreateTree();
        Diff diff = Mockito.mock(Diff.class);
        Mockito.when(diff.getStatus()).thenReturn(Diff.Status.ADDED);
        Mockito.when(diff.getPath()).thenReturn(SuperClone.directory.getAbsolutePath() + "/src/main/java/fr/unice/polytech/pnsinnov/Foo.java");

        // create the new file
        File fileAdded = new File(SuperClone.directory.getAbsolutePath() + "/src/main/java/fr/unice/polytech/pnsinnov/Foo.java");
        fileAdded.createNewFile();
        PrintWriter writer = new PrintWriter(fileAdded, "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;\n" +
                "\n" +
                "public class Foo {\n" +
                "\n" +
                "   private Student student;" +
                "\n" +
                "}\n");
        writer.close();

        Set<Diff> diffs = new HashSet<>();
        diffs.add(diff);

        assertEquals(0, javaLanguage.getTestsRelatedToChanges("CLASS", diffs).size());

    }

    @Test
    public void shouldGetTestsRelatedToModifiedFile() throws FileNotFoundException, UnsupportedEncodingException {
        Module module = Mockito.mock(Module.class);
        List<Module> modules = new ArrayList<>();
        Mockito.when(module.getSrcPath()).thenReturn(SuperClone.directory.getAbsolutePath() + "/src/main/java");
        Mockito.when(module.getTestPath()).thenReturn(SuperClone.directory.getAbsolutePath() + "/src/test/java");
        modules.add(module);
        javaLanguage.setUp(modules);

        List<String> actualClassName = new ArrayList<>();
        List<String> expectedClassName = Arrays.asList("fr.unice.polytech.pnsinnov.School", "fr.unice.polytech.pnsinnov.Main", "fr.unice.polytech.pnsinnov.Student");
        Database.getInstance().getTree().forEach(tree -> actualClassName.add(tree.getCls().getName()));

        List<String> actualTestsClassName = new ArrayList<>();
        List<String> expectedTestsClassName = Arrays.asList("fr.unice.polytech.pnsinnov.SchoolTest", "fr.unice.polytech.pnsinnov.StudentTest");
        Database.getInstance().getTests().forEach(tree -> actualTestsClassName.add(tree.getCls().getName()));

        Diff diff = Mockito.mock(Diff.class);
        Mockito.when(diff.getStatus()).thenReturn(Diff.Status.MODIFIED);
        Mockito.when(diff.getPath()).thenReturn(SuperClone.directory.getAbsolutePath() + "/src/main/java/fr/unice/polytech/pnsinnov/Student.java");

        Set<Diff> diffs = new HashSet<>();
        diffs.add(diff);

        Set<TestImplementation> testImplementations = new HashSet<>();
        testImplementations.add(new TestImplementation("fr.unice.polytech.pnsinnov.StudentTest"));
        testImplementations.add(new TestImplementation("fr.unice.polytech.pnsinnov.SchoolTest"));
        assertEquals(2, javaLanguage.getTestsRelatedToChanges("CLASS", diffs).size());
        assertEquals(testImplementations ,javaLanguage.getTestsRelatedToChanges("CLASS", diffs));
    }

}