package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.strategytech;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MvnExplorer implements Explorer {

    private String pom = "pom.xml";
    private String srcDir = "src";

    @Override
    public List<String> findAllModules(File baseDir) {
        List<String> res = new ArrayList<>();

        File[] directories = baseDir.listFiles(File::isDirectory);

        assert directories != null;

        for (File directory : directories) {
            if(new File(baseDir.getAbsolutePath() + pom).exists() && directory.getName().equals(srcDir)){
                res.add(baseDir.getAbsolutePath());
            }

            res.addAll(findAllModules(directory));
        }

        return res;
    }
}
