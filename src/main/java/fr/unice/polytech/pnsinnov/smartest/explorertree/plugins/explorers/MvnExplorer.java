package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers;


import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
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
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader(new File(Paths.get(currentModule).toAbsolutePath().toString() + pom)));

            if(model.getBuild() == null || model.getBuild().getSourceDirectory() == null) {
                return defaultSrc;
            } else if (model.getBuild().getSourceDirectory().startsWith("${")){
                return model.getProperties().getProperty(model.getBuild().getSourceDirectory().split("\\$\\{")[1].split("}")[0]);
            } else {
                return model.getBuild().getSourceDirectory();
            }
        } catch (IOException | XmlPullParserException e) {
            return defaultSrc;
        }
    }

    @Override
    public String getPathToTest(String currentModule) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(new FileReader(new File(Paths.get(currentModule).toAbsolutePath().toString() + pom)));

            if(model.getBuild() == null || model.getBuild().getTestSourceDirectory() == null || model.getProperties() == null){
                return defaultTest;
            } else if (model.getBuild().getTestSourceDirectory().startsWith("${")){
                return model.getProperties().getProperty(model.getBuild().getTestSourceDirectory().split("\\$\\{")[1].split("}")[0]);
            } else {
                return model.getProperties().getProperty(model.getBuild().getTestSourceDirectory());
            }

        } catch (IOException | XmlPullParserException e) {
            return defaultTest;
        }
    }
}
