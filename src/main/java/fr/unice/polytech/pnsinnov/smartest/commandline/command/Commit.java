package fr.unice.polytech.pnsinnov.smartest.commandline.command;

import fr.unice.polytech.pnsinnov.smartest.commandline.Command;
import picocli.CommandLine;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit extends Command {
    public void run() {
        // Run commit process
    }
}
