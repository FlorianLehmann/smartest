package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;


import fr.smartest.exceptions.CommitFailureException;
import fr.smartest.plugin.VCS;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GitVCS implements VCS {

    @Override
    public void commit(String message) throws CommitFailureException {
        Git git = null;

        try {
            //TODO AJOUTER LE PATH VERS LE GIT !
            git = Git.open(new File(Paths.get("").toAbsolutePath().toString(), ".git"));
            AddCommand add = git.add();

            for (String path : git.status().call().getUncommittedChanges()) {
                add.addFilepattern(path);
            }

            add.call();
            git.commit().setMessage(message).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("The \".git\" was not found in this folder");
        } finally {
            if (git != null){
                git.close();
            }
        }
    }

    @Override
    public List<String> diff() throws CommitFailureException {
        Git git = null;

        try {
            git = Git.open(new File(Paths.get("").toAbsolutePath().toString(), ".git"));

            //TODO REMPLACER PAR UN SET ?
            return new ArrayList<>(git.status().call().getUncommittedChanges());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } finally {
            if (git != null){
                git.close();
            }
        }

        return new ArrayList<>();
    }

    @Override
    public void checkout(String s) {
        //Not sure yet
    }

    @Override
    public void update() {
        //Not sure yet
    }

    @Override
    public boolean accept(String s) {
        return s.equals("git");
    }
}
