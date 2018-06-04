package fr.unice.polytech.pnsinnov.smartest.plugin.production;


import fr.smartest.plugin.Module;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MavenModule implements Module {

    private final String path;

    private String srcPath;

    private String testPath;

    private String outputSrcPath;

    private String outputTestPath;

    public MavenModule(String origin) {
        this.path = origin;
        srcPath = null;
        testPath = null;
        outputSrcPath = null;
        outputTestPath = null;
    }

    private String readBuild(BuildToDirectory buildToDirectory, PathPlugin defaultPath) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        String result;
        try {
            FileReader fileReader = new FileReader(new File(this.path + PathPlugin.POM_FILE.getName()));
            Model model = reader.read(fileReader);
            Build build = model.getBuild();
            if (model.getBuild() == null || buildToDirectory.convert(build) == null) {
                result = defaultPath.getName();
            }
            else if (buildToDirectory.convert(build).startsWith("${")) {
                result = model.getProperties().getProperty(retrieveProperty(buildToDirectory.convert(build)));
            }
            else {
                result = buildToDirectory.convert(build);
            }
            fileReader.close();
        }
        catch (IOException | XmlPullParserException e) {
            result = defaultPath.getName();
        }
        return result;
    }

    @Override
    public String getSrcPath() {
        if (srcPath == null) {
            srcPath = readBuild(Build::getSourceDirectory, PathPlugin.DEFAULT_SRC);
        }
        return this.srcPath;
    }

    @Override
    public String getTestPath() {
        if (testPath == null) {
            testPath = readBuild(Build::getTestSourceDirectory, PathPlugin.DEFAULT_TST);
        }
        return this.testPath;
    }

    @Override
    public String getCompiledSrcPath() {
        if (outputSrcPath == null) {
            outputSrcPath = readBuild(Build::getOutputDirectory, PathPlugin.DEFAULT_SRC_OUTPUT);
        }
        return this.outputSrcPath;
    }

    @Override
    public String getCompiledTestPath() {
        if (outputTestPath == null) {
            outputTestPath = readBuild(Build::getTestOutputDirectory, PathPlugin.DEFAULT_TEST_OUTPUT);
        }
        return this.outputTestPath;
    }

    private String retrieveProperty(String wanted) {
        return wanted.split("\\$\\{")[1].split("}")[0];
    }

    @FunctionalInterface
    private interface BuildToDirectory {
        String convert(Build build);
    }
}
