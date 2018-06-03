package fr.unice.polytech.pnsinnov.smartest.configuration;

public class ConfigurationHolder implements Configuration {
    private final String gitPath;
    private final String pluginPath;
    private final String projectPath;
    private final String language;
    private final String productionTool;
    private final String testFramework;
    private final String vcs;

    public ConfigurationHolder(String gitPath, String pluginPath, String projectPath, String language,
                               String productionTool, String testFramework, String vcs) {
        this.gitPath = gitPath;
        this.pluginPath = pluginPath;
        this.projectPath = projectPath;
        this.language = language;
        this.productionTool = productionTool;
        this.testFramework = testFramework;
        this.vcs = vcs;
    }

    @Override
    public String gitPath() {
        return gitPath;
    }

    @Override
    public String pluginPath() {
        return pluginPath;
    }

    @Override
    public String projectPath() {
        return projectPath;
    }

    @Override
    public String language() {
        return language;
    }

    @Override
    public String productionTool() {
        return productionTool;
    }

    @Override
    public String testFramework() {
        return testFramework;
    }

    @Override
    public String vcs() {
        return vcs;
    }
}
