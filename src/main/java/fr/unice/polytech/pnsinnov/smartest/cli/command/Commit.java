package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.unice.polytech.pnsinnov.smartest.commandline.Command;
import picocli.CommandLine;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit extends Command {
    @CommandLine.Option(names = {"-m", "--message"}, required = true,
            description = "Use the given <msg> as the commit message.")
    private String message;

    @CommandLine.Option(names = {"-s", "--scope"}, description = "Module, Class, ...")
    private String scope = "Class";

    public void run() {
    }
}
