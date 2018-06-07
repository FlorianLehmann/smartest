package fr.unice.polytech.pnsinnov.smartest;


import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;

public class SuperClone {

    public static File directory;

    @BeforeAll
    public static void cloneGit(){
        directory = new File("src/test/resources/tmp");
        if(!directory.mkdir()){
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                //Oopses
            }
        }

        Git git = null;

        try {
            git = Git.cloneRepository().setDirectory(directory).setURI("https://mjollnir.unice.fr/bitbucket/scm/pnse/sample_project.git").call();
        } catch (GitAPIException e){
            //Pray for it
        } finally {
            if (git != null){
                git.close();
            }
        }

    }

    @AfterAll
    public static void deleteAll() throws IOException {
        FileUtils.deleteDirectory(directory);
    }

}
