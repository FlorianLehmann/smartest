package fr.unice.polytech.pnsinnov.smartest.commandline.command;

import picocli.CommandLine;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit implements Runnable {
    public void run() {
        // Run commit process
    }
}
