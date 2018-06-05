package fr.unice.polytech.pnsinnov.smartest.configuration;

import java.nio.file.Path;

public interface ConfigReader {
    Configuration readConfig(Path path);
}
