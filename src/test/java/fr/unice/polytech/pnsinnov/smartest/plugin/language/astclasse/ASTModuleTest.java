package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import fr.smartest.plugin.Diff;
import fr.smartest.plugin.Module;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.diff.TestImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ASTModuleTest extends SuperClone {

    private ASTModule astModule;

    @BeforeEach
    public void defineContext() {


    }

    @Test
    public void shouldGetTestRelatedToModifiedTest() throws IOException {
        Module module = Mockito.mock(Module.class);
        Mockito.when(module.getSrcPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/main/java"));
        Mockito.when(module.getTestPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/test/java"));
        Diff diff = Mockito.mock(Diff.class);
        Mockito.when(diff.getStatus()).thenReturn(Diff.Status.ADDED);
        Mockito.when(diff.getPath()).thenReturn(Paths.get(directory.getAbsolutePath(),
                "src/test/java/fr/unice/polytech/pnsinnov/FooTest.java"));
        Mockito.when(diff.getPath()).thenReturn(Paths.get(directory.getAbsolutePath(),
                "src/test/java/fr/unice/polytech/pnsinnov/FooTest.java"));

        Mockito.when(diff.getOldContent()).thenReturn("package fr.unice.polytech.pnsinnov; public class FooTest { }");
        Mockito.when(diff.getNewContent()).thenReturn("package fr.unice.polytech.pnsinnov; public class FooTest { private Student student; }");

        // create the new file
        File fileAdded = new File(Paths.get(directory.getAbsolutePath(), "/src/test/java/fr/unice/polytech/pnsinnov/FooTest.java").toAbsolutePath().toString());
        fileAdded.createNewFile();
        PrintWriter writer = new PrintWriter(fileAdded, "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;\n" +
                "public class FooTest {\n" +
                "   private Student student;" +
                "}\n");
        writer.close();

        Set<Diff> diffs = new HashSet<>();
        diffs.add(diff);

        astModule = new ASTModule(module, "class");

        assertEquals(1, astModule.getTestsRelatedToChanges("", diffs).size());
        List<fr.smartest.plugin.Test> tests = new ArrayList<>(astModule.getTestsRelatedToChanges("", diffs));
        assertEquals("fr.unice.polytech.pnsinnov.FooTest", tests.get(0).getIdentifier());
        fileAdded.delete();
    }

    @Test
    public void shouldGetTestRelatedToAddedTest() throws IOException {
        Module module = Mockito.mock(Module.class);
        Mockito.when(module.getSrcPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/main/java"));
        Mockito.when(module.getTestPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/test/java"));
        Diff diff = Mockito.mock(Diff.class);
        Mockito.when(diff.getStatus()).thenReturn(Diff.Status.ADDED);
        Mockito.when(diff.getPath()).thenReturn(Paths.get(directory.getAbsolutePath(),
                "src/test/java/fr/unice/polytech/pnsinnov/FooTest.java"));
        Mockito.when(diff.getPath()).thenReturn(Paths.get(directory.getAbsolutePath(),
                "src/test/java/fr/unice/polytech/pnsinnov/FooTest.java"));

        Mockito.when(diff.getOldContent()).thenReturn("");
        Mockito.when(diff.getNewContent()).thenReturn("package fr.unice.polytech.pnsinnov; public class FooTest { private Student student; }");

        // create the new file
        File fileAdded = new File(Paths.get(directory.getAbsolutePath(), "/src/test/java/fr/unice/polytech/pnsinnov/FooTest.java").toAbsolutePath().toString());
        fileAdded.createNewFile();
        PrintWriter writer = new PrintWriter(fileAdded, "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;\n" +
                "public class FooTest {\n" +
                "   private Student student;" +
                "}\n");
        writer.close();

        Set<Diff> diffs = new HashSet<>();
        diffs.add(diff);

        astModule = new ASTModule(module, "class");

        assertEquals(1, astModule.getTestsRelatedToChanges("", diffs).size());
        List<fr.smartest.plugin.Test> tests = new ArrayList<>(astModule.getTestsRelatedToChanges("", diffs));
        assertEquals("fr.unice.polytech.pnsinnov.FooTest", tests.get(0).getIdentifier());
        fileAdded.delete();
    }

    @Test
    public void shouldNotCrashIfFileIsRemoved() throws IOException {
        Module module = Mockito.mock(Module.class);
        Mockito.when(module.getSrcPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/main/java"));
        Mockito.when(module.getTestPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/test/java"));

        Diff diff = Mockito.mock(Diff.class);
        Mockito.when(diff.getStatus()).thenReturn(Diff.Status.ADDED);
        Mockito.when(diff.getPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/test/java/fr/unice/polytech/pnsinnov/FooTest.java"));
        Mockito.when(diff.getOldContent()).thenReturn("package fr.unice.polytech.pnsinnov; public class FooTest { }");
        Mockito.when(diff.getNewContent()).thenReturn(new String(" "));

        // create the new file
        File fileAdded = new File(Paths.get(directory.getAbsolutePath(), "/src/test/java/fr/unice/polytech/pnsinnov/FooTest.java").toAbsolutePath().toString());
        fileAdded.createNewFile();
        PrintWriter writer = new PrintWriter(fileAdded, "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;\n" +
                "public class FooTest {\n" +
                "   private Student student;" +
                "}\n");
        writer.close();

        Set<Diff> diffs = new HashSet<>();
        diffs.add(diff);

        astModule = new ASTModule(module, "class");

        assertEquals(0, astModule.getTestsRelatedToChanges("", diffs).size());
        fileAdded.delete();
    }

    @Test
    public void shouldGetTwoTestsRelatedToModifiedFile() throws IOException {
        Module module = Mockito.mock(Module.class);
        Mockito.when(module.getSrcPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/main/java"));
        Mockito.when(module.getTestPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/test/java"));

        Diff diff = Mockito.mock(Diff.class);
        Mockito.when(diff.getStatus()).thenReturn(Diff.Status.ADDED);
        Mockito.when(diff.getPath()).thenReturn(Paths.get(directory.getAbsolutePath(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"));
        Mockito.when(diff.getOldContent()).thenReturn("package fr.unice.polytech.pnsinnov;\n" +
                "\n" +
                "public class Student {\n" +
                "\n" +
                "    private String name;\n" +
                "\n" +
                "    public Student(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "\n" +
                "    public boolean isSmart() {\n" +
                "        return true;\n" +
                "    }\n" +
                "}\n");
        Mockito.when(diff.getNewContent()).thenReturn("package fr.unice.polytech.pnsinnov;\n" +
                "\n" +
                "public class Student {\n" +
                "\n" +
                "    private String name;\n" +
                "\n" +
                "    public Student(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "\n" +
                "    public boolean isSmart() {\n" +
                "        return false;\n" +
                "    }\n" +
                "}\n");

        // create the new file
        File fileAdded = new File(Paths.get(directory.getAbsolutePath(), "/src/main/java/fr/unice/polytech/pnsinnov/Student.java").toAbsolutePath().toString());
        fileAdded.createNewFile();
        PrintWriter writer = new PrintWriter(fileAdded, "UTF-8");
        writer.println("package fr.unice.polytech.pnsinnov;\n" +
                "\n" +
                "public class Student {\n" +
                "\n" +
                "    private String name;\n" +
                "\n" +
                "    public Student(String name) {\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "\n" +
                "    public boolean isSmart() {\n" +
                "        return false;\n" +
                "    }\n" +
                "}\n");
        writer.close();

        Set<Diff> diffs = new HashSet<>();
        diffs.add(diff);

        astModule = new ASTModule(module, "class");
        List<fr.smartest.plugin.Test> tests = new ArrayList<>(astModule.getTestsRelatedToChanges("", diffs));

        assertEquals(2, tests.size());
        assertTrue(tests.contains(new TestImpl("fr.unice.polytech.pnsinnov.StudentTest")));
        assertTrue(tests.contains(new TestImpl("fr.unice.polytech.pnsinnov.SchoolTest")));
    }


}