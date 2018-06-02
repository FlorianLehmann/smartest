package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.unice.polytech.pnsinnov.smartest.commandline.Command;
import picocli.CommandLine;

@CommandLine.Command(name = "test", description = "Run tests on the selected scope.")
public class Test extends Command {
    @CommandLine.Option(names = {"-s", "--scope"}, description = "Module, Class, ...")
    private String scope = "Class";

    @Override
    public void run() {
    }
}
