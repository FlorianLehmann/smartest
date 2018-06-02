package fr.unice.polytech.pnsinnov.smartest;

import fr.unice.polytech.pnsinnov.smartest.cli.CommandLineParser;

public class Main {
    public static void main(String[] args) {
        Smartest smartest = new Smartest();
        CommandLineParser commandLineParser = new CommandLineParser(smartest);
        commandLineParser.parse(args);
    }
}
