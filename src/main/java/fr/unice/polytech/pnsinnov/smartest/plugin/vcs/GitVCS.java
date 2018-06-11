package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;


import fr.smartest.exceptions.VCSException;
import fr.smartest.plugin.Diff;
import fr.smartest.plugin.VCS;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class GitVCS implements VCS {

    private Path gitPath;
    private Path projectPath;

    @Override
    public void setUp(Path gitPath, Path projectPath) {
        this.gitPath = gitPath;
        this.projectPath = projectPath;
    }

    @Override
    public void commit(String message) throws VCSException {
        Git git = null;

        try {
            git = Git.open(new File(gitPath.toAbsolutePath().toString()));
            AddCommand add = git.add();

            for (String path : git.status().call().getUncommittedChanges()) {
                add.addFilepattern(path);
            }

            add.call();
            git.commit().setMessage(message).call();
        }
        catch (GitAPIException e) {
            throw new GitException(e);
        }
        catch (IOException e) {
            throw new GitNotFoundException(e);
        }
        finally {
            if (git != null) {
                git.close();
            }
        }
    }

    @Override
    public Set<Diff> diff() throws VCSException {
        try (Git git = Git.open(new File(gitPath.toAbsolutePath().toString()))) {
            Repository repository = git.getRepository();
            RevCommit head = getHead(repository);

            HashSet<Diff> diffs = new HashSet<>();

            for (String path : git.status().call().getAdded()) {
                diffs.add(createDiff(repository, head, path, Diff.Status.ADDED));
            }

            for (String path : git.status().call().getRemoved()) {
                diffs.add(createDiff(repository, head, path, Diff.Status.REMOVED));
            }

            for (String path : git.status().call().getModified()) {
                diffs.add(createDiff(repository, head, path, Diff.Status.MODIFIED));
            }
            return diffs;
        }
        catch (IOException e) {
            throw new GitNotFoundException(e);
        }
        catch (GitAPIException e) {
            throw new GitException(e);
        }
    }

    private Diff createDiff(Repository repository, RevCommit head, String path, Diff.Status status) throws IOException {
        return new GitDiff(projectPath.resolve(path).toAbsolutePath(),
                status, getOldContent(repository, head, path));
    }

    private RevCommit getHead(Repository repository) throws IOException {
        try (RevWalk revWalk = new RevWalk(repository)) {
            ObjectId lastCommitId = repository.resolve(Constants.HEAD);
            RevCommit head = revWalk.parseCommit(lastCommitId);
            revWalk.dispose();
            return head;
        }
    }

    private String getOldContent(Repository repository, RevCommit commit, String path) throws IOException {
        try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, commit.getTree())) {
            ObjectId blobId = treeWalk.getObjectId(0);
            try (ObjectReader objectReader = repository.newObjectReader()) {
                ObjectLoader objectLoader = objectReader.open(blobId);
                byte[] bytes = objectLoader.getBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }
        catch (NullPointerException e) {
            return "";
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
