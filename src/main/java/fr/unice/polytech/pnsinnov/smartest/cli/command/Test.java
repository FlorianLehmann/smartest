package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.smartest.exceptions.PluginException;
import fr.unice.polytech.pnsinnov.smartest.cli.Command;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(name = "test", description = "Run tests on the selected scope.")
public class Test extends Command {
    private static final Logger logger = LogManager.getLogger(Test.class);

    @CommandLine.Option(names = {"-s", "--scope"}, description = "Module, Class, ...")
    private String scope = "Class";

    @Override
    public void run() {
        logger.info("commit option has been selected on scope \"" + scope + "\"");
        try {
            smartest.test(scope);
        }
        catch (PluginException e) {
            context.err().print("An error occurred: ");
            context.err().println(e.getMessage());
        }
    }
}
