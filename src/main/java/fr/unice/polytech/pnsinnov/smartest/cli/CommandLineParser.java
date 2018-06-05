package fr.unice.polytech.pnsinnov.smartest.cli;

import fr.unice.polytech.pnsinnov.smartest.Context;
import fr.unice.polytech.pnsinnov.smartest.Smartest;
import fr.unice.polytech.pnsinnov.smartest.cli.command.Commit;
import fr.unice.polytech.pnsinnov.smartest.cli.command.ListTests;
import fr.unice.polytech.pnsinnov.smartest.cli.command.Test;
import fr.unice.polytech.pnsinnov.smartest.configuration.ConfigReader;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CommandLine.Command(description = "Smartest.", name = "smartest", mixinStandardHelpOptions = true,
        version = "smartest 2.0", subcommands = {Commit.class, ListTests.class, Test.class})
public class CommandLineParser implements Runnable {
    private static final Logger logger = LogManager.getLogger(CommandLineParser.class);
    private final CommandLine commandLine;
    private final Context context;
    private final ConfigReader configReader;

    @CommandLine.Option(names = {"--config-path"}, description = "Set path to config.smt file.")
    private Path configPath = Paths.get("config.smt");

    @CommandLine.Option(names = {"--set-level"}, description = "Set to logger level.")
    private String level = Level.OFF.toString();

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

    Path getConfigPath() {
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
        LogManager.getRootLogger().setLevel(getLevel());
        logger.info("config-path=" + configPath);
        logger.info("logger-level=" + level);
        CommandLine.ParseResult result = commandLine.getParseResult();
        Smartest smartest = new Smartest(configReader.readConfig(this.configPath));
        addSmartestToCommands(smartest);
        if (!result.hasSubcommand() && !commandLine.isUsageHelpRequested()) {
            commandLine.usage(context.out());
        }
    }

    private Level getLevel() {
        return Level.toLevel(level);
    }
}
