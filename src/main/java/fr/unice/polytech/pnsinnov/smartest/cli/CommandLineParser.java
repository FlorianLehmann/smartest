package fr.unice.polytech.pnsinnov.smartest.cli;

import fr.unice.polytech.pnsinnov.smartest.Context;
import fr.unice.polytech.pnsinnov.smartest.Smartest;
import fr.unice.polytech.pnsinnov.smartest.cli.command.Commit;
import fr.unice.polytech.pnsinnov.smartest.cli.command.ListTests;
import fr.unice.polytech.pnsinnov.smartest.cli.command.Test;
import fr.unice.polytech.pnsinnov.smartest.configuration.ConfigReader;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(description = "Smartest.", name = "smartest", mixinStandardHelpOptions = true,
        version = "smartest 2.0", subcommands = {Commit.class, ListTests.class, Test.class})
public class CommandLineParser implements Runnable {
    private final CommandLine commandLine;
    private final Context context;
    private final ConfigReader configReader;

    @CommandLine.Option(names = {"--config-path"}, description = "Set path to config.smt file.")
    private String configPath = "config.smt";

    public CommandLineParser(ConfigReader configReader) {
        this(configReader, new Context.ContextBuilder()
                .withInputStream(System.in)
                .withOutStream(System.out)
                .withErrStream(System.err)
                .build());
    }

    CommandLineParser(ConfigReader configReader, Context context) {
        this.context = context;
        this.configReader = configReader;
        this.commandLine = new CommandLine(this);
        addContextToCommands();
    }

    String getConfigPath() {
        return configPath;
    }

    private void addContextToCommands() {
        for (CommandLine cmd : commandLine.getSubcommands().values()) {
            cmd.<Command>getCommand().setContext(context);
        }
    }

    private void addSmartestToCommands(Smartest smartest) {
        for (CommandLine cmd : commandLine.getSubcommands().values()) {
            cmd.<Command>getCommand().setSmartest(smartest);
        }
    }

    void addSubCommand(String name, Command command) {
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
        Smartest smartest = new Smartest(configReader.readConfig(configPath));
        addSmartestToCommands(smartest);
        if (!result.hasSubcommand() && !commandLine.isUsageHelpRequested()) {
            commandLine.usage(context.out());
        }
    }
}
