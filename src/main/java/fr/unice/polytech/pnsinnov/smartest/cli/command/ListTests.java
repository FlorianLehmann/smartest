package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.smartest.exceptions.PluginException;
import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.cli.Command;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import picocli.CommandLine;

import java.util.Set;

@CommandLine.Command(name = "list-tests", description = "List the tests that cover the changes between commits.")
public class ListTests extends Command {
    private static final Logger logger = LogManager.getLogger(ListTests.class);

    @CommandLine.Option(names = {"-s", "--scope"}, description = "Module, Class, ...")
    private String scope = "Class";

    public void run() {
        logger.info("list-tests option has been selected on scope \"" + scope + "\"");
        try {
            Set<Test> tests = smartest.listTests(scope);
            if (tests.isEmpty()) {
                context.out().println("No change impacting tests were detected");
            }
            for (Test test : tests) {
                context.out().println(test.getIdentifier());
            }
        }
        catch (PluginException e) {
            context.err().print("An error occurred: ");
            context.err().println(e.getMessage());
        }
    }
}
