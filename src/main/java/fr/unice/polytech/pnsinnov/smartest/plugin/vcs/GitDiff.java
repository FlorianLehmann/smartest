package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;


import fr.smartest.plugin.Diff;

import java.nio.file.Path;

public class GitDiff implements Diff {

    private Path path;

    private Status diffStatus;

    public GitDiff(Path path, Status diffStatus){
        this.path = path;
        this.diffStatus = diffStatus;
    }

    @Override
    public Status getStatus() {
        return diffStatus;
    }

    @Override
    public Path getPath() {
        return path;
    }
}
