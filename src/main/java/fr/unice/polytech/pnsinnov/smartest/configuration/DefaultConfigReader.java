package fr.unice.polytech.pnsinnov.smartest.configuration;

public class DefaultConfigReader implements ConfigReader {
    @Override
    public Configuration readConfig(String path) {
        return new ConfigurationHolder(".git", "plugins", ".", "java", "maven", "junit5", "git");
    }
}
