package fr.unice.polytech.pnsinnov.smartest.plugin.production;


import fr.smartest.exceptions.ProductionToolException;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.ProductionTool;
import org.apache.maven.shared.invoker.*;

import java.io.File;
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
        return retrieveAllModules(new File(baseDir.toAbsolutePath().toString()));
    }

    private List<Module> retrieveAllModules(File currentDir){
        List<Module> res = new ArrayList<>();

        File pomfile = new File(currentDir.getAbsolutePath() + PathPlugin.POM_FILE.getName());

        File srcFile = new File(currentDir.getAbsolutePath() + PathPlugin.SRC_DIRECTORY.getName());

        if(pomfile.exists() && srcFile.exists()){
            res.add(new MavenModule(currentDir.toPath()));
        }

        File[] directories = currentDir.listFiles(File::isDirectory);

        assert directories != null;

        for (File directory : directories) {
            res.addAll(retrieveAllModules(directory));
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
