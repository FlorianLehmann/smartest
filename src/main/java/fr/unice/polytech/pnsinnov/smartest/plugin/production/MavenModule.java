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

    private String outputSrcPath;

    private String outputTestPath;

    public MavenModule(String origin){
        this.path = origin;
        srcPath = null;
        testPath = null;
        outputSrcPath = null;
        outputTestPath = null;
    }

    @Override
    public String getSrcPath() {
        if(srcPath == null){
            MavenXpp3Reader reader = new MavenXpp3Reader();
            try {
                Model model = reader.read(new FileReader(new File(this.path + PathPlugin.POM_FILE.getName())));

                if(model.getBuild() == null || model.getBuild().getSourceDirectory() == null) {
                    this.srcPath = PathPlugin.DEFAULT_SRC.getName();
                } else if (model.getBuild().getSourceDirectory().startsWith("${")){
                    this.srcPath = model.getProperties().getProperty(retrieveProperty(model.getBuild().getSourceDirectory()));
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
        if(testPath == null) {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            try {
                Model model = reader.read(new FileReader(new File(this.path + PathPlugin.POM_FILE.getName())));

                if (model.getBuild() == null || model.getBuild().getTestSourceDirectory() == null || model.getProperties() == null) {
                    this.testPath = PathPlugin.DEFAULT_TST.getName();
                } else if (model.getBuild().getTestSourceDirectory().startsWith("${")) {
                    this.testPath = model.getProperties().getProperty(retrieveProperty(model.getBuild().getTestSourceDirectory()));
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
        if(outputSrcPath == null){
            MavenXpp3Reader reader = new MavenXpp3Reader();
            try {
                Model model = reader.read(new FileReader(new File(this.path + PathPlugin.POM_FILE.getName())));

                if(model.getBuild() == null || model.getBuild().getOutputDirectory() == null) {
                    this.outputSrcPath = PathPlugin.DEFAULT_SRC_OUTPUT.getName();
                } else if (model.getBuild().getOutputDirectory().startsWith("${")){
                    this.outputSrcPath = model.getProperties().getProperty(retrieveProperty(model.getBuild().getOutputDirectory()));
                } else {
                    this.outputSrcPath = model.getBuild().getOutputDirectory();
                }
            } catch (IOException | XmlPullParserException e) {
                this.outputSrcPath = PathPlugin.DEFAULT_SRC_OUTPUT.getName();
            }
        }

        return this.outputSrcPath;
    }

    @Override
    public String getCompiledTestPath() {
        if(outputTestPath == null){
            MavenXpp3Reader reader = new MavenXpp3Reader();
            try {
                Model model = reader.read(new FileReader(new File(this.path + PathPlugin.POM_FILE.getName())));

                if(model.getBuild() == null || model.getBuild().getTestOutputDirectory() == null) {
                    this.outputTestPath = PathPlugin.DEFAULT_TEST_OUTPUT.getName();
                } else if (model.getBuild().getTestOutputDirectory().startsWith("${")){
                    this.outputTestPath = model.getProperties().getProperty(retrieveProperty(model.getBuild().getTestOutputDirectory()));
                } else {
                    this.outputTestPath = model.getBuild().getTestOutputDirectory();
                }
            } catch (IOException | XmlPullParserException e) {
                this.outputTestPath = PathPlugin.DEFAULT_TEST_OUTPUT.getName();
            }
        }

        return this.outputTestPath;
    }

    private String retrieveProperty(String wanted){
        return wanted.split("\\$\\{")[1].split("}")[0];
    }
}
