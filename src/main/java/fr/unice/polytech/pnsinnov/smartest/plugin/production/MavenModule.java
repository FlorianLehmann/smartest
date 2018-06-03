package fr.unice.polytech.pnsinnov.smartest.plugin.production;


import fr.smartest.plugin.Module;

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
        return null;
    }

    @Override
    public String getTestPath() {
        return null;
    }
}
