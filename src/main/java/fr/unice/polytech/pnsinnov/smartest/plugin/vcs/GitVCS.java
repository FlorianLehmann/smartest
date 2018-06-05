package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;


import fr.smartest.exceptions.VCSException;
import fr.smartest.plugin.Diff;
import fr.smartest.plugin.VCS;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class GitVCS implements VCS {

    private Path gitPath;

    @Override
    public void setUp(Path path) {
        this.gitPath = path;
    }

    @Override
    public void commit(String message) throws VCSException {
        Git git = null;

        try {
            git = Git.open(new File(gitPath.toAbsolutePath().toString(), ".git"));
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
    public Set<Diff> diff() throws VCSException {
        Git git = null;

        try {
            git = Git.open(new File(gitPath.toAbsolutePath().toString(), ".git"));

            HashSet<Diff> diffs = new HashSet<>();

            for (String path :
                    git.status().call().getAdded()) {
                diffs.add(new GitDiff(Paths.get(path), Diff.Status.ADDED));
            }

            for (String path :
                    git.status().call().getRemoved()) {
                diffs.add(new GitDiff(Paths.get(path), Diff.Status.REMOVED));
            }

            for (String path :
                    git.status().call().getModified()) {
                diffs.add(new GitDiff(Paths.get(path), Diff.Status.MODIFIED));
            }

            return diffs;
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
