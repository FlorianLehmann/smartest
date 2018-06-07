package fr.unice.polytech.pnsinnov.smartest.integration;


import fr.smartest.plugin.TestReport;
import fr.unice.polytech.pnsinnov.smartest.Main;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class EndToEndTest extends SuperClone{

    private static String contentSchool;
    private static String contentStudent;

    private String schoolExpected = "fr.unice.polytech.pnsinnov.SchoolTest";
    private String studentExpected = "fr.unice.polytech.pnsinnov.StudentTest";

    private Path tmp2Path = Paths.get("src/test/resources/tmp2");

    private ByteArrayOutputStream out;

    @BeforeAll
    static void setupEndToEnd(){
        try {
            contentStudent = new String(Files.readAllBytes(new File(SuperClone.directory.getAbsolutePath(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java").toPath()));
            contentSchool = new String(Files.readAllBytes(new File(SuperClone.directory.getAbsolutePath(), "src/main/java/fr/unice/polytech/pnsinnov/School.java").toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void copyTmp(){
        try {
            FileUtils.copyDirectory(new File(Paths.get("src/test/resources/tmp").toAbsolutePath().toString()), new File(this.tmp2Path.toAbsolutePath().toString()));
        } catch (IOException e) {
            //Creation failed
        }

        out = new ByteArrayOutputStream();

        System.setOut(new PrintStream(out));
    }

    private ObjectId getCurrentGitHead(File gitFile){
        Git git = null;

        try {
            git = Git.open(gitFile);

            return git.getRepository().resolve(Constants.HEAD);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (git !=null){
                git.close();
            }
        }

        return null;
    }

    @Test
    void commitFailure(){
        ObjectId beforeCommit = getCurrentGitHead(new File(this.tmp2Path.toAbsolutePath().toString(), ".git"));

        try {
            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "false").getBytes(), false);

            Main.main(new String[]{"--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "commit", "-m", "bonjour" });

            assertTrue(out.toString().startsWith("Code has not been committed due to test failure"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectId afterCommit = getCurrentGitHead(new File(this.tmp2Path.toAbsolutePath().toString(), ".git"));

        assertEquals(beforeCommit.name(), afterCommit.name());
    }


    @Test
    void commitSuccess(){
        ObjectId beforeCommit = getCurrentGitHead(new File(this.tmp2Path.toAbsolutePath().toString(), ".git"));

        try {
            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "true && true").getBytes(), false);

            Main.main(new String[]{"--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "commit", "-m", "bonjour" });

            assertEquals("Changes has been committed successfully", flattenString(out.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectId afterCommit = getCurrentGitHead(new File(this.tmp2Path.toAbsolutePath().toString(), ".git"));

        assertNotEquals(beforeCommit.name(), afterCommit.name());

    }

    @Test
    void testMultipleChange(){
        try {
            Main.main(new String[]{"--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "test"});

            assertEquals("" + 0 + " tests has been executed", flattenString(out.toString()));

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "false").getBytes(), false);

            Main.main(new String[]{"--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "test"});

            String smartOutput = flattenString(out.toString());

            assertTrue(smartOutput.endsWith("" + 2 + " tests has been executed"));
            assertTrue(smartOutput.contains(schoolExpected + ": " + TestReport.Status.SUCCESSFUL));
            assertTrue(smartOutput.contains(studentExpected + ": " + TestReport.Status.FAILURE));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    @Test
    void testOneChange(){
        try {
            Main.main(new String[]{"--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "test"});

            assertEquals("" + 0 + " tests has been executed", flattenString(out.toString()));

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/School.java"), contentSchool.replaceFirst("new ArrayList<Student>\\(\\);", "new ArrayList<Student>();\n\t\tSystem.out.println(\"test\");").getBytes(), false);

            Main.main(new String[]{"--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "test"});

            String smartOutput = flattenString(out.toString());

            assertTrue(smartOutput.endsWith("" + 1 + " tests has been executed"));
            assertTrue(smartOutput.contains(schoolExpected + ": " + TestReport.Status.SUCCESSFUL));
            assertFalse(smartOutput.contains(studentExpected));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    @Test
    void listAllTestOneChange(){
        try {
            Main.main(new String[]{"--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "list-tests"});

            assertEquals("No change impacting tests were detected", flattenString(out.toString()));

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/School.java"), contentSchool.replaceFirst("new ArrayList<Student>\\(\\);", "new ArrayList<Student>();\n\t\tSystem.out.println(\"test\");").getBytes(), false);

            Main.main(new String[]{"--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "list-tests"});

            String smartOutput = flattenString(out.toString());

            assertTrue(smartOutput.contains(this.schoolExpected));
            assertFalse(smartOutput.contains(this.studentExpected));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    private String flattenString(String s) {
        return s.replaceAll("[\\r\\n]*", "");
    }

    @AfterEach
    void deleteTmp2() {
        try {
            FileUtils.deleteDirectory(new File(this.tmp2Path.toAbsolutePath().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
