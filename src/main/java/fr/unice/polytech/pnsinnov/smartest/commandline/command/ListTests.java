package fr.unice.polytech.pnsinnov.smartest.commandline.command;

import picocli.CommandLine;

@CommandLine.Command(name = "list-tests", description = "List the tests that cover the changes between commits.")
public class ListTests implements Runnable {
    public void run() {
        // Run list tests process
    }
}
