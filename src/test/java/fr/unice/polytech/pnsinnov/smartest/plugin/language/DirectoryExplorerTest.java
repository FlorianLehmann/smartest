package fr.unice.polytech.pnsinnov.smartest.plugin.language;


import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectoryExplorerTest {

    private List<String> files;

    @BeforeEach
    public void defineContext() throws IOException, InterruptedException {
        files = new ArrayList<>();
        files.add("tmp/foo/bar.java");
        files.add("tmp/Bar.java");
        files.add("tmp/foo/FooBar.Java");
        files.add("tmp/foo/FooBarFoo.JAVA");
        files.add("tmp/bar/FooFooBarFoo.JAVA");

        new File("tmp/foo/bar").mkdirs();
        new File("tmp/bar").mkdirs();

        for (String filepath : files) {
            new File(filepath).createNewFile();
        }

        new File("tmp/foo/FooBarBar.stt").createNewFile();
    }

    @Test
    public void shouldGetAllJavaFile() {
        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<File> javaFiles = directoryExplorer.explore(Paths.get("tmp"));

        assertEquals(5, javaFiles.size());
        for (File file : javaFiles) {
            URI uri = new File("").toPath().toAbsolutePath().toUri().relativize(file.toPath().toAbsolutePath().toUri());
            assertTrue(this.files.contains(uri.toString()));
        }
    }

    @Test
    public void shouldGetAllJavaFileInMultipleDir() {
        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<Path> directories = new ArrayList<>();
        directories.add(Paths.get("tmp/foo"));
        directories.add(Paths.get("tmp/bar"));
        List<File> javaFiles = directoryExplorer.explore(directories);

        assertEquals(4, javaFiles.size());
        for (File file : javaFiles) {
            URI uri = new File("").toPath().toAbsolutePath().toUri().relativize(file.toPath().toAbsolutePath().toUri());
            assertTrue(this.files.contains(uri.toString()));
        }
    }

    @Test
    public void shouldNotFindJavaFile() throws InterruptedException, IOException {

        new File("tmp/Bar.java").delete();
        FileUtils.deleteDirectory(new File("tmp/bar"));
        FileUtils.deleteDirectory(new File("tmp/foo"));

        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<File> javaFiles = directoryExplorer.explore(Paths.get("tmp"));

        assertEquals(0, javaFiles.size());
    }

    @Test
    public void shouldHandleInvalidDirectory() throws InterruptedException, IOException {
        FileUtils.deleteDirectory(new File("tmp"));

        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<File> javaFiles = directoryExplorer.explore(Paths.get("tmp"));

        assertEquals(0, javaFiles.size());
    }

    @AfterEach
    public void cleanContext() throws IOException, InterruptedException {
        FileUtils.deleteDirectory(new File("tmp"));
    }
}