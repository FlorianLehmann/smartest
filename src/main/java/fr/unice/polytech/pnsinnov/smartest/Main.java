package fr.unice.polytech.pnsinnov.smartest;

import fr.unice.polytech.pnsinnov.smartest.cli.CommandLineParser;
import fr.unice.polytech.pnsinnov.smartest.configuration.ConfigReader;

public class Main {
    public static void main(String[] args) {
        ConfigReader configReader = null;
        CommandLineParser commandLineParser = new CommandLineParser(configReader);
        commandLineParser.parse(args);
    }
}
