package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;

import fr.smartest.exceptions.VCSException;
import fr.smartest.plugin.Diff;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GitVCSTest extends SuperClone{

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
            Set<String> paths = new HashSet<>();

            for (Diff diff:
                 this.gitVCS.diff()) {
                paths.add(diff.getPath());
            }
            assertTrue(paths.contains(toCreate.getPath().replace("\\", "/")));

            File file = new File("src/test/resources/unmodifiedTestFile.txt");

            if(file.exists()){
                assertFalse(paths.contains(file.getPath().replace("\\", "/")));
            }
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