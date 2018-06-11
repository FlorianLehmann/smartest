package fr.unice.polytech.pnsinnov.smartest;


import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SuperClone {

    protected File directory = new File("src/test/resources/tmp");
    protected String gitURI = "https://mjollnir.unice.fr/bitbucket/scm/pnse/sample_project.git";
    protected String commit = "ee86a0a6224724b8cd7cd84e3478cf34df56be2e";

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void setGitURI(String gitURI) {
        this.gitURI = gitURI;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    @BeforeAll
    public void cloneGit() {
        if(!directory.mkdir()){
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                //Oopses
            }
        }

        try (Git git = Git.cloneRepository().setDirectory(directory).setURI(gitURI).call()) {
            git.checkout().setName(commit).call();
        }
        catch (GitAPIException e) {
            //Pray for it
        }
    }

    @AfterAll
    public void deleteAll() throws IOException {
        FileUtils.deleteDirectory(directory);
    }

}
