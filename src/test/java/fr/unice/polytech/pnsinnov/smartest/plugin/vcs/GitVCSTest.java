package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;

import fr.smartest.exceptions.VCSException;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GitVCSTest {

    private GitVCS gitVCS;

    private File toCreate;

    @BeforeEach
    public void setup(){
        gitVCS = new GitVCS();
        gitVCS.setUp(Paths.get("").toAbsolutePath().toString());

        try {
            toCreate = new File("src/test/resources/emptyTestFile.txt");
            if(!toCreate.createNewFile()){
                delete_and_create();
            }
        } catch (IOException e) {
            delete_and_create();
        }

        Git git = null;

        try {
            git = Git.open(new File(".git"));
            git.add().addFilepattern("src/test/resources/emptyTestFile.txt").call();
        } catch (GitAPIException | IOException e){
            //Pray for it
        } finally {
            if (git != null){
                git.close();
            }
        }
    }

    private void delete_and_create(){
        toCreate.delete();
        try {
            toCreate.createNewFile();
        } catch (IOException e1) {
            //Here, the file has to be created. Or external reason involved
        }
    }

    @Test
    void diff() {
        try {
            Set<String> changes = this.gitVCS.diff();

            assertTrue(changes.contains(toCreate.getPath().replace("\\", "/")));
        } catch (VCSException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown(){
        Git git = null;

        try {
            git = Git.open(new File(".git"));
            git.reset().addPath("src/test/resources/emptyTestFile.txt").call();
        } catch (GitAPIException | IOException e){
            //Pray for it
        } finally {
            if (git != null){
                git.close();
            }
        }

        toCreate.delete();
    }

}