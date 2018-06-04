package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;


import fr.smartest.exceptions.VCSException;
import fr.smartest.plugin.VCS;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class GitVCS implements VCS {

    private String gitPath;

    @Override
    public void setUp(String s) {
        this.gitPath = s;
    }

    @Override
    public void commit(String message) throws VCSException {
        Git git = null;

        try {
            git = Git.open(new File(gitPath, ".git"));
            AddCommand add = git.add();

            for (String path : git.status().call().getUncommittedChanges()) {
                add.addFilepattern(path);
            }

            add.call();
            git.commit().setMessage(message).call();
        } catch (GitAPIException e) {
            throw new GitException(e);
        } catch (IOException e) {
            throw new GitNotFoundException(e);
        } finally {
            if (git != null){
                git.close();
            }
        }
    }

    @Override
    public Set<String> diff() throws VCSException {
        Git git = null;

        try {
            git = Git.open(new File(Paths.get("").toAbsolutePath().toString(), ".git"));

            return git.status().call().getUncommittedChanges();
        } catch (IOException e) {
            throw new GitNotFoundException(e);
        } catch (GitAPIException e) {
            throw new GitException(e);
        } finally {
            if (git != null){
                git.close();
            }
        }
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
