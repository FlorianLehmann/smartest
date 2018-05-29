package fr.unice.polytech.pnsinnov.smartest.commandline;

import picocli.CommandLine;

import java.io.PrintStream;

@CommandLine.Command(description = "Smartest.", name = "smartest", mixinStandardHelpOptions = true, version = "smartest 1.0")
public class Smartest implements Runnable {
    private final CommandLine commandLine;
    private final PrintStream out;

    public Smartest(PrintStream out) {
        this.out = out;
        commandLine = new CommandLine(this);
    }

    public void parse(String[] args) {
        commandLine.parseWithHandlers(new CommandLine.RunAll(), new CommandLine.DefaultExceptionHandler<>(), args);
    }

    public void run() {
        if (!commandLine.getParseResult().hasSubcommand() && !commandLine.isUsageHelpRequested()) {
            commandLine.usage(out);
        }
    }
}
