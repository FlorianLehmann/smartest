package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.smartest.exceptions.PluginException;
import fr.smartest.plugin.TestReport;
import fr.unice.polytech.pnsinnov.smartest.cli.Command;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import picocli.CommandLine;

import java.util.Set;

@CommandLine.Command(name = "test", description = "Run tests on the selected scope.")
public class Test extends Command {
    private static final Logger logger = LogManager.getLogger(Test.class);

    @CommandLine.Option(names = {"-s", "--scope"}, description = "Module, Class, ...")
    private String scope = "Class";

    @Override
    public void run() {
        logger.info("test option has been selected on scope \"" + scope + "\"");
        try {
            Set<TestReport> testReports = smartest.test(scope);
            for (TestReport testReport : testReports) {
                context.out().println(testReport.getTest().getIdentifier() + ": " + testReport.getResult());
            }
            context.out().println(testReports.size() + " tests has been executed successfully");
        }
        catch (PluginException e) {
            context.err().print("An error occurred: ");
            context.err().println(e.getMessage());
        }
    }
}
