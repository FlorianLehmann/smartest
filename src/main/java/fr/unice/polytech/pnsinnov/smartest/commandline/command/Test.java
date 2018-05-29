package fr.unice.polytech.pnsinnov.smartest.commandline.command;

import fr.unice.polytech.pnsinnov.smartest.commandline.Command;
import picocli.CommandLine;

@CommandLine.Command(name = "test", description = "Run tests on the selected scope.")
public class Test extends Command {
    @CommandLine.Option(names = {"-s", "--scope"}, required = true, description = "Module, Class, ...")
    private String scope;

    @Override
    public void run() {
        // Run test process on the given scope
    }
}
