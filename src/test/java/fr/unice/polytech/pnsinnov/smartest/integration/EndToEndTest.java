package fr.unice.polytech.pnsinnov.smartest.integration;


import fr.smartest.plugin.TestReport;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import fr.unice.polytech.pnsinnov.smartest.plugin.production.PathPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.shared.invoker.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EndToEndTest extends SuperClone{

    private static String contentSchool;
    private static String contentStudent;

    private String schoolExpected = "fr.unice.polytech.pnsinnov.SchoolTest";
    private String studentExpected = "fr.unice.polytech.pnsinnov.StudentTest";

    private Path tmp2Path = Paths.get("src/test/resources/tmp2");

    @BeforeAll
    static void setupEndToEnd(){
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(Paths.get("").toAbsolutePath().toString(), PathPlugin.POM_FILE.getName()));
        request.setGoals(Arrays.asList("clean", "package", "-DskipTests"));
        request.setOutputHandler(s -> {});
        request.setBatchMode(true);
        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            //Not compiled...
        }

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

        String smartOutput;

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "smartest-jar-with-dependencies.jar", "--config-path=\"../src/test/resources/tmp2/resources/config.smt\"", "commit", "-m", "\"bonjour\"");
            pb.directory(new File(Paths.get("target").toAbsolutePath().toString()));

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "false").getBytes(), false);

            Process p = pb.start();

            smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

            assertTrue(smartOutput.startsWith("Code has not been committed due to test failure"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectId afterCommit = getCurrentGitHead(new File(this.tmp2Path.toAbsolutePath().toString(), ".git"));

        assertEquals(beforeCommit.name(), afterCommit.name());
    }


    @Test
    void commitSuccess(){
        ObjectId beforeCommit = getCurrentGitHead(new File(this.tmp2Path.toAbsolutePath().toString(), ".git"));

        String smartOutput;

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "smartest-jar-with-dependencies.jar", "--config-path=\"../src/test/resources/tmp2/resources/config.smt\"", "commit", "-m", "\"bonjour\"");
            pb.directory(new File(Paths.get("target").toAbsolutePath().toString()));

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "true && true").getBytes(), false);

            Process p = pb.start();

            smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

            assertEquals("Changes has been committed successfully", smartOutput);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectId afterCommit = getCurrentGitHead(new File(this.tmp2Path.toAbsolutePath().toString(), ".git"));

        assertNotEquals(beforeCommit.name(), afterCommit.name());

    }

    @Test
    void testMultipleChange(){
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "smartest-jar-with-dependencies.jar", "--config-path=\"../src/test/resources/tmp2/resources/config.smt\"", "test");
            pb.directory(new File(Paths.get("target").toAbsolutePath().toString()));
            Process p = pb.start();

            String smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

            assertEquals("" + 0 + " tests has been executed", smartOutput);

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "false").getBytes(), false);

            p = pb.start();

            smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

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
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "smartest-jar-with-dependencies.jar", "--config-path=\"../src/test/resources/tmp2/resources/config.smt\"", "test");
            pb.directory(new File(Paths.get("target").toAbsolutePath().toString()));
            Process p = pb.start();

            String smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

            assertEquals("" + 0 + " tests has been executed", smartOutput);

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/School.java"), contentSchool.replaceFirst("new ArrayList<Student>\\(\\);", "new ArrayList<Student>();\n\t\tSystem.out.println(\"test\");").getBytes(), false);

            p = pb.start();

            smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

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
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "smartest-jar-with-dependencies.jar", "--config-path=\"../src/test/resources/tmp2/resources/config.smt\"", "list-tests");
            pb.directory(new File(Paths.get("target").toAbsolutePath().toString()));
            Process p = pb.start();

            String smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

            assertEquals("No change impacting tests were detected", smartOutput);

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/School.java"), contentSchool.replaceFirst("new ArrayList<Student>\\(\\);", "new ArrayList<Student>();\n\t\tSystem.out.println(\"test\");").getBytes(), false);

            p = pb.start();

            smartOutput = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());

            assertTrue(smartOutput.contains(this.schoolExpected));
            assertFalse(smartOutput.contains(this.studentExpected));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    @Test
    void listAllTestOverJar(){
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "target/smartest-jar-with-dependencies.jar", "--config-path=\"src/test/resources/tmp2/resources/config.smt\"", "list-tests");
            Process p = pb.start();

            String smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

            assertEquals("No change impacting tests were detected", smartOutput);

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "false").getBytes(), false);

            p = pb.start();

            smartOutput = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());

            assertTrue(smartOutput.contains(this.schoolExpected));
            assertTrue(smartOutput.contains(this.studentExpected));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    @Test
    void listAllTestRootJar(){
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "smartest-jar-with-dependencies.jar", "--config-path=\"../src/test/resources/tmp2/resources/config.smt\"", "list-tests");
            pb.directory(new File(Paths.get("target").toAbsolutePath().toString()));
            Process p = pb.start();

            String smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

            assertEquals("No change impacting tests were detected", smartOutput);

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "false").getBytes(), false);

            p = pb.start();

            smartOutput = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());

            assertTrue(smartOutput.contains(this.schoolExpected));
            assertTrue(smartOutput.contains(this.studentExpected));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    @Test
    void listAllTestFromProject(){
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "../../../../target/smartest-jar-with-dependencies.jar", "--config-path=\"resources/config.smt\"", "list-tests");
            pb.directory(new File(Paths.get("src/test/resources/tmp2").toAbsolutePath().toString()));
            Process p = pb.start();

            String smartOutput = flattenString(IOUtils.toString(p.getInputStream(), Charset.defaultCharset()));

            assertEquals("No change impacting tests were detected", smartOutput);

            FileUtils.writeByteArrayToFile(new File(tmp2Path.toAbsolutePath().toString(), "src/main/java/fr/unice/polytech/pnsinnov/Student.java"), contentStudent.replaceFirst("true", "false").getBytes(), false);

            p = pb.start();

            smartOutput = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());

            assertTrue(smartOutput.contains(this.schoolExpected));
            assertTrue(smartOutput.contains(this.studentExpected));
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    private String flattenString(String s) {
        return s.replaceAll("[\\r\\n]*", "");
    }

    @AfterEach
    void deleteTmp2(){
        try {
            FileUtils.deleteDirectory(new File(this.tmp2Path.toAbsolutePath().toString()));
        } catch (IOException e) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e1) {
                //Won't be deleted..
            }

            deleteTmp2();
        }
    }
}
