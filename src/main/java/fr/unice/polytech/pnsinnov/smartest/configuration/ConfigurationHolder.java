package fr.unice.polytech.pnsinnov.smartest.configuration;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.nio.file.Path;

public class ConfigurationHolder implements Configuration {
    private static final Logger logger = LogManager.getLogger(JSONConfigReader.class);
    private final Path gitPath;
    private final Path pluginPath;
    private final Path projectPath;
    private final String language;
    private final String productionTool;
    private final String testFramework;
    private final String vcs;

    public ConfigurationHolder(Path gitPath, Path pluginPath, Path projectPath, String language,
                               String productionTool, String testFramework, String vcs) {
        this.gitPath = gitPath;
        this.pluginPath = pluginPath;
        this.projectPath = projectPath;
        this.language = language;
        this.productionTool = productionTool;
        this.testFramework = testFramework;
        this.vcs = vcs;
        logger.debug("Config found : " + toString());
    }

    @Override
    public Path gitPath() {
        return gitPath;
    }

    @Override
    public Path pluginPath() {
        return pluginPath;
    }

    @Override
    public Path projectPath() {
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

    @Override
    public String toString() {
        return "ConfigurationHolder{gitPath='" + gitPath + "\', pluginPath='" + pluginPath +
                "\', projectPath='" + projectPath + "\', language='" + language +
                "\', productionTool='" + productionTool + "\', testFramework='" + testFramework +
                "\', vcs='" + vcs + "\'}";
    }
}
