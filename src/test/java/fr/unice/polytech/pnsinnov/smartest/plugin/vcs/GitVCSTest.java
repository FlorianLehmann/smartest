package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;

import fr.smartest.exceptions.VCSException;
import fr.smartest.plugin.Diff;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GitVCSTest extends SuperClone {

    private GitVCS gitVCS;

    private File toCreate;

    @BeforeEach
    public void setup(){
        gitVCS = new GitVCS();
        gitVCS.setUp(Paths.get(".git"), Paths.get(""));

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
            Set<Path> paths = new HashSet<>();

            for (Diff diff : this.gitVCS.diff()) {
                paths.add(diff.getPath());
            }
            assertTrue(paths.contains(toCreate.toPath().toAbsolutePath()));

            File file = new File("src/test/resources/unmodifiedTestFile.txt");

            if(file.exists()){
                assertFalse(paths.contains(file.getPath()));
            }
        } catch (VCSException e) {
            e.printStackTrace();
        }
    }

    @Test
    void commit() {
        Git git = null;

        this.gitVCS.setUp(Paths.get(SuperClone.directory.getAbsolutePath(), ".git"), Paths.get(SuperClone.directory
                .getAbsolutePath()));

        try {
            git = Git.open(new File(SuperClone.directory.getAbsolutePath(), ".git"));

            File file = new File(SuperClone.directory.getAbsolutePath(), "empty.txt");

            if(file.createNewFile()){
                git.add().addFilepattern(file.getName()).call();

                assertTrue(git.status().call().hasUncommittedChanges());

                assertEquals(1, git.status().call().getUncommittedChanges().size());

                this.gitVCS.commit("commit test");

                assertFalse(git.status().call().hasUncommittedChanges());

                RevWalk walk = new RevWalk(git.getRepository());

                ObjectId head = git.getRepository().resolve("HEAD^{tree}");

                RevCommit youngestCommit = walk.parseCommit(head);

                assertEquals(youngestCommit.getShortMessage(), "commit test");
            }
        } catch (IOException | GitAPIException | VCSException e) {
            //Commit failed..
        } finally {
            if (git != null){
                git.close();
            }
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