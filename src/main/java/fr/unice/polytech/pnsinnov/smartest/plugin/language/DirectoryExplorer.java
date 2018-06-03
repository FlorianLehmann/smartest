package fr.unice.polytech.pnsinnov.smartest.plugin.language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryExplorer {

    public List<File> explore(List<String> directories) {
        List<File> javaFiles = new ArrayList<>();

        for (String directory : directories)
            javaFiles.addAll(explore(directory));

        return javaFiles;
    }

    public List<File> explore(String directory) {

        List<File> javaFiles = new ArrayList<>();
        File dir = new File(directory);

        String[] files = dir.list();

        if (files != null) {
            for (String file : files) {
                File tmpFile = new File(dir.getAbsolutePath() + "/" + file);
                if (tmpFile.isFile() && file.toLowerCase().endsWith(".java")) {
                    javaFiles.add(tmpFile);
                } else if (tmpFile.isDirectory()) {
                    javaFiles.addAll(explore(tmpFile.getAbsolutePath()));
                }
            }
        }

        return javaFiles;
    }

}
