package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.smartest.exceptions.CommitFailureException;
import fr.smartest.exceptions.SmartestException;
import fr.smartest.plugin.TestReport;
import fr.unice.polytech.pnsinnov.smartest.cli.Command;
import fr.unice.polytech.pnsinnov.smartest.exceptions.TestFailureException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import picocli.CommandLine;

import java.util.Set;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit extends Command {
    private static final Logger logger = LogManager.getLogger(Commit.class);
    @CommandLine.Option(names = {"-m", "--message"}, required = true,
            description = "Use the given <msg> as the commit message.")
    private String message;

    @CommandLine.Option(names = {"-s", "--scope"}, description = "Module, Class, ...")
    private String scope = "Class";

    public void run() {
        logger.info("commit option has been selected with message " + message + " on scope \"" + scope + "\"");
        try {
            smartest.commit(scope, message);
            context.out().println("Changes has been committed successfully");
        }
        catch (TestFailureException e) {
            context.out().print(e.getMessage());
            printTestFailures(e.getFailures());
        }
        catch (CommitFailureException e) {
            context.out().print("Changes has not been committed due to: ");
            context.out().println(e.getMessage());
        }
        catch (SmartestException e) {
            context.err().print("An error occurred: ");
            context.err().println(e.getMessage());
        }
    }

    private void printTestFailures(Set<TestReport> failures) {
        for (TestReport failure : failures) {
            failure.getCause().ifPresent(context.out()::println);
        }
    }
}
