package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MvnExplorer implements Explorer {

    private String pom = "/pom.xml";
    private String srcDir = "/src";
    private String defaultSrc = "src/main/java";
    private String defaultTest = "src/test/java";

    @Override
    public List<String> findAllModules(File baseDir) {
        List<String> res = new ArrayList<>();

        File pomfile = new File(baseDir.getAbsolutePath() + pom);

        File srcFile = new File(baseDir.getAbsolutePath() + srcDir);

        if(pomfile.exists() && srcFile.exists()){
            res.add(baseDir.getAbsolutePath());
        }

        File[] directories = baseDir.listFiles(File::isDirectory);

        assert directories != null;

        for (File directory : directories) {
            res.addAll(findAllModules(directory));
        }

        return res;
    }

    @Override
    public String getPathToSrc(String currentModule) {
        //TODO PARSER LE POM.XML ?
        return defaultSrc;
    }

    @Override
    public String getPathToTest(String currentModule) {
        //TODO PARSER LE POM.XML ?
        return defaultTest;
    }
}
