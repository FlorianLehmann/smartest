package fr.unice.polytech.pnsinnov.smartest;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;

public class SuperClone {

    private static File directory;

    @BeforeAll
    public static void cloneGit(){
        directory = new File("tmp");
        if(!directory.mkdir()){
            deleteFolder(directory);
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

    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files != null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    @AfterAll
    public static void deleteAll(){
        deleteFolder(directory);
    }

}
