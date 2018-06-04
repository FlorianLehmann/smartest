package fr.unice.polytech.pnsinnov.smartest.plugin.production;


import fr.smartest.plugin.Module;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MavenModule implements Module{

    private final String path;

    private String srcPath;

    private String testPath;

    public MavenModule(String origin){
        this.path = origin;
        srcPath = null;
        testPath = null;
    }

    @Override
    public String getSrcPath() {
        if(srcPath == null){
            MavenXpp3Reader reader = new MavenXpp3Reader();
            try {
                System.out.println(new File(this.path + PathPlugin.POM_FILE.getName()).exists());

                Model model = reader.read(new FileReader(new File(this.path + PathPlugin.POM_FILE.getName())));

                if(model.getBuild() == null || model.getBuild().getSourceDirectory() == null) {
                    this.srcPath = PathPlugin.DEFAULT_SRC.getName();
                } else if (model.getBuild().getSourceDirectory().startsWith("${")){
                    this.srcPath = model.getProperties().getProperty(model.getBuild().getSourceDirectory().split("\\$\\{")[1].split("}")[0]);
                } else {
                    this.srcPath = model.getBuild().getSourceDirectory();
                }
            } catch (IOException | XmlPullParserException e) {
                this.srcPath = PathPlugin.DEFAULT_SRC.getName();
            }
        }

        return this.srcPath;
    }

    @Override
    public String getTestPath() {
        if(srcPath == null) {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            try {
                Model model = reader.read(new FileReader(new File(this.path + PathPlugin.POM_FILE.getName())));

                if (model.getBuild() == null || model.getBuild().getTestSourceDirectory() == null || model.getProperties() == null) {
                    this.testPath = PathPlugin.DEFAULT_TST.getName();
                } else if (model.getBuild().getTestSourceDirectory().startsWith("${")) {
                    this.testPath = model.getProperties().getProperty(model.getBuild().getTestSourceDirectory().split("\\$\\{")[1].split("}")[0]);
                } else {
                    this.testPath = model.getProperties().getProperty(model.getBuild().getTestSourceDirectory());
                }

            } catch (IOException | XmlPullParserException e) {
                this.testPath = PathPlugin.DEFAULT_TST.getName();
            }
        }

        return this.testPath;
    }

    @Override
    public String getCompiledSrcPath() {
        return null;
    }

    @Override
    public String getCompiledTestPath() {
        return null;
    }
}
