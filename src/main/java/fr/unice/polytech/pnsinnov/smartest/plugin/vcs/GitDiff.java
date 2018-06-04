package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;


import fr.smartest.plugin.Diff;

public class GitDiff implements Diff {

    private String path;

    private Status diffStatus;

    public GitDiff(String path, Status diffStatus){
        this.path = path;
        this.diffStatus = diffStatus;
    }

    @Override
    public Status getStatus() {
        return diffStatus;
    }

    @Override
    public String getPath() {
        return path;
    }
}
