package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.smartest.exceptions.CommitFailureException;
import fr.smartest.exceptions.PluginException;
import fr.unice.polytech.pnsinnov.smartest.cli.Command;
import picocli.CommandLine;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit extends Command {
    @CommandLine.Option(names = {"-m", "--message"}, required = true,
            description = "Use the given <msg> as the commit message.")
    private String message;

    @CommandLine.Option(names = {"-s", "--scope"}, description = "Module, Class, ...")
    private String scope = "Class";

    public void run() {
        try {
            smartest.commit(scope, message);
            context.out().println("Changes has been committed successfully");
        }
        catch (CommitFailureException e) {
            context.out().print("Changes has not been committed due to: ");
            context.out().println(e.getMessage());
        }
        catch (PluginException e) {
            context.err().print("An error occurred: ");
            context.err().println(e.getMessage());
        }
    }
}
