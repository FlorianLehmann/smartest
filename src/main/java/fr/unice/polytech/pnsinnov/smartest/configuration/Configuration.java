package fr.unice.polytech.pnsinnov.smartest.configuration;

import java.nio.file.Path;

public interface Configuration {
    Path gitPath();

    Path pluginPath();

    Path projectPath();

    String language();

    String productionTool();

    String testFramework();

    String vcs();
}
