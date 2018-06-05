package fr.unice.polytech.pnsinnov.smartest.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultConfigReader implements ConfigReader {
    @Override
    public Configuration readConfig(Path path) {
        return new ConfigurationHolder(
                Paths.get(".git").toAbsolutePath(),
                Paths.get("plugins").toAbsolutePath(),
                Paths.get("").toAbsolutePath(),
                "java",
                "maven",
                "junit5",
                "git"
        );
    }
}
