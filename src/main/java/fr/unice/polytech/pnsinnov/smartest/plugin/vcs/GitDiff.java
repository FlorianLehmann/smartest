package fr.unice.polytech.pnsinnov.smartest.plugin.vcs;


import fr.smartest.plugin.Diff;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class GitDiff implements Diff {

    private Path path;

    private Status diffStatus;

    private String oldContent;

    private String newContent;

    public GitDiff(Path path, Status diffStatus, String oldContent){
        this.path = path;
        this.diffStatus = diffStatus;
        this.oldContent = oldContent;
        newContent = null;
    }

    @Override
    public Status getStatus() {
        return diffStatus;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getOldContent() {
        return this.oldContent;
    }

    @Override
    public String getNewContent() {
        if (newContent == null){
            try {
                FileUtils.readFileToString(new File(this.path.toAbsolutePath().toString()), Charset.defaultCharset());
            } catch (IOException e) {
                //TODO Faire qqchose ?
            }
        }

        return this.newContent;
    }
}
