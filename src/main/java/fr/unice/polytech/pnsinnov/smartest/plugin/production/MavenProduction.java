package fr.unice.polytech.pnsinnov.smartest.plugin.production;


import fr.smartest.plugin.Module;
import fr.smartest.plugin.ProductionTool;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MavenProduction implements ProductionTool {

    private String pom = "/pom.xml";
    private String srcDir = "/src";
    private String defaultSrc = "src/main/java";
    private String defaultTest = "src/test/java";

    @Override
    public List<Module> getModules() {
        //PAS VALIDE, baseDir sera l'argument nouveau
        File baseDir = new File(Paths.get("").toAbsolutePath().toString());

        List<Module> res = new ArrayList<>();

        File pomfile = new File(baseDir.getAbsolutePath() + pom);

        File srcFile = new File(baseDir.getAbsolutePath() + srcDir);

        if(pomfile.exists() && srcFile.exists()){
            res.add(new MavenModule(baseDir.getAbsolutePath()));
        }

        File[] directories = baseDir.listFiles(File::isDirectory);

        assert directories != null;

        for (File directory : directories) {
            //TODO A FAIRE AVEC LA NOUVELLE LIB
            //res.addAll(getModules(directory));
        }

        return res;
    }

    @Override
    public void compile() {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File(Paths.get("pom.xml").toAbsolutePath().toString()));
        request.setGoals( Arrays.asList("clean", "compile"));

        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            System.out.println("The maven compilation failed");
        }
    }

    @Override
    public boolean accept(String s) {
        return s.equals("maven");
    }
}
