package fr.unice.polytech.pnsinnov.smartest.plugin.language;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DirectoryExplorer {

    public List<File> explore(List<Path> directories) {
        List<File> javaFiles = new ArrayList<>();

        for (Path directory : directories)
            javaFiles.addAll(explore(directory));

        return javaFiles;
    }

    public List<File> explore(Path directory) {

        List<File> javaFiles = new ArrayList<>();
        File dir = new File(directory.toAbsolutePath().toString());

        String[] files = dir.list();

        if (files != null) {
            for (String file : files) {
                File tmpFile = new File(dir.getAbsolutePath() + "/" + file);
                if (tmpFile.isFile() && file.toLowerCase().endsWith(".java")) {
                    javaFiles.add(tmpFile);
                } else if (tmpFile.isDirectory()) {
                    javaFiles.addAll(explore(tmpFile.toPath()));
                }
            }
        }

        return javaFiles;
    }

}
