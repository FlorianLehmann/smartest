package fr.unice.polytech.pnsinnov.smartest;

import fr.unice.polytech.pnsinnov.smartest.cli.CommandLineParser;
import fr.unice.polytech.pnsinnov.smartest.configuration.ConfigReader;
import fr.unice.polytech.pnsinnov.smartest.configuration.JSONConfigReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        ConfigReader configReader = new JSONConfigReader();
        CommandLineParser commandLineParser = new CommandLineParser(configReader);
        commandLineParser.parse(args);
    }
}
