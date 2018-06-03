package fr.unice.polytech.pnsinnov.smartest.plugin.language;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectoryExplorerTest {

    private Runtime runtime;
    private List<String> files;

    @BeforeEach
    public void defineContext() throws IOException, InterruptedException {
        files = new ArrayList<>();
        files.add("tmp/foo/bar.java");
        files.add("tmp/Bar.java");
        files.add("tmp/foo/FooBar.Java");
        files.add("tmp/foo/FooBarFoo.JAVA");
        files.add("tmp/bar/FooFooBarFoo.JAVA");
        runtime = Runtime.getRuntime();
        Process process = runtime.exec("mkdir -p tmp/foo/bar tmp/bar");
        process.waitFor();
        for (String filepath : files) {
            process = runtime.exec("touch " + filepath);
            process.waitFor();
        }
        process = runtime.exec("touch tmp/foo/FooBarBar.stt");
        process.waitFor();
    }

    @Test
    public void shouldGetAllJavaFile() {
        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<File> javaFiles = directoryExplorer.explore("tmp");

        assertEquals(5, javaFiles.size());
        for (File file : javaFiles) {
            String filePath = file.getAbsolutePath().split(System.getProperty("user.dir") + "/")[1];
            assertTrue(this.files.contains(filePath));
        }
    }

    @Test
    public void shouldGetAllJavaFileInMultipleDir() {
        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<String> directories = new ArrayList<>();
        directories.add("tmp/foo");
        directories.add("tmp/bar");
        List<File> javaFiles = directoryExplorer.explore(directories);

        assertEquals(4, javaFiles.size());
        for (File file : javaFiles) {
            String filePath = file.getAbsolutePath().split(System.getProperty("user.dir") + "/")[1];
            assertTrue(this.files.contains(filePath));
        }
    }

    @Test
    public void shouldNotFindJavaFile() throws InterruptedException, IOException {
        Process process = runtime.exec("rm -rf tmp/Bar.java tmp/bar");
        process.waitFor();
        process = runtime.exec("rm -rf tmp/foo");
        process.waitFor();
        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<File> javaFiles = directoryExplorer.explore("tmp");

        assertEquals(0, javaFiles.size());
    }

    @Test
    public void shouldHandleInvalidDirectory() throws InterruptedException, IOException {
        Process process = runtime.exec("rm -rf tmp");
        process.waitFor();
        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<File> javaFiles = directoryExplorer.explore("tmp");

        assertEquals(0, javaFiles.size());
    }

    @AfterEach
    public void cleanContext() throws IOException, InterruptedException {
        runtime = Runtime.getRuntime();
        Process process = runtime.exec("rm -rf tmp");
        process.waitFor();
    }
}