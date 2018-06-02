package fr.unice.polytech.pnsinnov.smartest;

import fr.unice.polytech.pnsinnov.smartest.cli.CommandLineParser;

public class Main {
    public static void main(String[] args) {
        CommandLineParser commandLineParser = new CommandLineParser();
        commandLineParser.parse(args);
    }
}
