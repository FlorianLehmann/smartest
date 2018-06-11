package fr.unice.polytech.pnsinnov.smartest.plugin.production;


import fr.smartest.exceptions.ProductionToolException;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.ProductionTool;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MavenProduction implements ProductionTool {

    private Path baseDir;

    @Override
    public void setUp(Path path) {
        baseDir = path;
    }

    @Override
    public List<Module> getModules() {
        return retrieveModulesWithPom(new File(baseDir.toAbsolutePath().toString()));
    }

    private List<Module> retrieveModulesWithPom(File currentDir){
        List<Module> res = new ArrayList<>();

        MavenXpp3Reader reader = new MavenXpp3Reader();

        try {
            FileReader fileReader = new FileReader(new File(currentDir, PathPlugin.POM_FILE.getName()));
            Model model = reader.read(fileReader);
            fileReader.close();

            res.add(new MavenModule(currentDir.toPath().toAbsolutePath()));

            for (String module :
                    model.getModules()) {
                res.addAll(retrieveModulesWithPom(new File(currentDir, module)));
            }
        }
        catch (IOException | XmlPullParserException e) {
            res = new ArrayList<>();
        }

        return res;
    }

    @Override
    public void compile() throws ProductionToolException{

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(baseDir.toAbsolutePath().toString(), PathPlugin.POM_FILE.getName()));
        request.setGoals(Arrays.asList("clean", "test-compile", "compile"));
        request.setOutputHandler(s -> {});
        request.setBatchMode(true);
        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            throw new MavenCompileException(e);
        }
    }

    @Override
    public boolean accept(String s) {
        return s.equalsIgnoreCase("maven");
    }
}
