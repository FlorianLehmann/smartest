package fr.unice.polytech.pnsinnov.smartest.cli;

import fr.unice.polytech.pnsinnov.smartest.commandline.Command;
import fr.unice.polytech.pnsinnov.smartest.commandline.Context;
import fr.unice.polytech.pnsinnov.smartest.commandline.command.Commit;
import fr.unice.polytech.pnsinnov.smartest.commandline.command.ListTests;
import fr.unice.polytech.pnsinnov.smartest.commandline.command.Test;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(description = "Smartest.", name = "smartest", mixinStandardHelpOptions = true,
        version = "smartest 2.0", subcommands = {Commit.class, ListTests.class, Test.class})
public class CommandLineParser implements Runnable {
    private final CommandLine commandLine;
    private final Context context;

    public CommandLineParser() {
        this(new Context.ContextBuilder()
                .withInputStream(System.in)
                .withOutStream(System.out)
                .withErrStream(System.err)
                .build());
    }

    CommandLineParser(Context context) {
        this.context = context;
        this.commandLine = new CommandLine(this);
        addContextToCommands();
    }

    private void addContextToCommands() {
        for (CommandLine cmd : commandLine.getSubcommands().values()) {
            cmd.<Command>getCommand().setContext(context);
        }
    }

    void addSubCommand(String name, Runnable command) {
        commandLine.addSubcommand(name, command);
    }

    public void parse(String... args) {
        commandLine.parseWithHandlers(
                new CommandLine.RunAll().useOut(context.out()).useErr(context.err()),
                new CommandLine.DefaultExceptionHandler<List<Object>>().useOut(context.out()).useErr(context.err()),
                args);
    }

    public void run() {
        CommandLine.ParseResult result = commandLine.getParseResult();
        if (!result.hasSubcommand() && !commandLine.isUsageHelpRequested()) {
            commandLine.usage(context.out());
        }
    }
}
