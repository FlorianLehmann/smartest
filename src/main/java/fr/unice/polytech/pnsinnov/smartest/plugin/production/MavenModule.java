package fr.unice.polytech.pnsinnov.smartest.plugin.production;


import fr.smartest.plugin.Module;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MavenModule implements Module {

    private final Path origin;

    private Path srcPath;

    private Path testPath;

    private Path outputSrcPath;

    private Path outputTestPath;

    public MavenModule(Path origin) {
        this.origin = origin;
        srcPath = null;
        testPath = null;
        outputSrcPath = null;
        outputTestPath = null;
    }

    private Path readBuild(BuildToDirectory buildToDirectory, PathPlugin defaultPath) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        String result;
        try {
            FileReader fileReader = new FileReader(new File(this.origin.toAbsolutePath().toString(), PathPlugin.POM_FILE.getName()));
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

        return Paths.get(this.origin.toAbsolutePath().toString(), result);
    }

    @Override
    public Path getSrcPath() {
        if (srcPath == null) {
            srcPath = readBuild(Build::getSourceDirectory, PathPlugin.DEFAULT_SRC);
        }
        return this.srcPath;
    }

    @Override
    public Path getTestPath() {
        if (testPath == null) {
            testPath = readBuild(Build::getTestSourceDirectory, PathPlugin.DEFAULT_TST);
        }
        return this.testPath;
    }

    @Override
    public Path getCompiledSrcPath() {
        if (outputSrcPath == null) {
            outputSrcPath = readBuild(Build::getOutputDirectory, PathPlugin.DEFAULT_SRC_OUTPUT);
        }
        return this.outputSrcPath;
    }

    @Override
    public Path getCompiledTestPath() {
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
