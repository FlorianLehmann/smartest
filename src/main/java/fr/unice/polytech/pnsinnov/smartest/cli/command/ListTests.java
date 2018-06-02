package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.cli.Command;
import picocli.CommandLine;

@CommandLine.Command(name = "list-tests", description = "List the tests that cover the changes between commits.")
public class ListTests extends Command {
    @CommandLine.Option(names = {"-s", "--scope"}, description = "Module, Class, ...")
    private String scope = "Class";

    public void run() {
        for (Test test : context.smartest().listTests(scope)) {
            context.out().println(test.getIdentifier());
        }
    }
}
