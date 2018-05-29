package fr.unice.polytech.pnsinnov.smartest.commandline;

import fr.unice.polytech.pnsinnov.smartest.commandline.command.Commit;
import fr.unice.polytech.pnsinnov.smartest.commandline.command.ListTests;
import fr.unice.polytech.pnsinnov.smartest.commandline.command.Test;
import fr.unice.polytech.pnsinnov.smartest.explorertree.ConfigReader;
import fr.unice.polytech.pnsinnov.smartest.explorertree.PluginTechRetriever;
import fr.unice.polytech.pnsinnov.smartest.explorertree.TreeModule;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.DefaultPlugin;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.Plugin;
import picocli.CommandLine;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

@CommandLine.Command(description = "Smartest.", name = "smartest", mixinStandardHelpOptions = true, version = "smartest 1.0", subcommands = {Commit.class, ListTests.class, Test.class})
public class Smartest implements Runnable {
    private final CommandLine commandLine;
    private final Context context;

    public Smartest(InputStream in, PrintStream out, PrintStream err) {
        context = new Context().useIn(in).useOut(out).useErr(err);
        commandLine = new CommandLine(this);
    }

    public void parse(String[] args) {
        commandLine.parseWithHandlers(new CommandLine.RunAll().useOut(context.out()).useErr(context.err()), new CommandLine.DefaultExceptionHandler<>(), args);
    }

    public void run() {
        CommandLine.ParseResult result = commandLine.getParseResult();
        if (!result.hasSubcommand() && !commandLine.isUsageHelpRequested()) {
            commandLine.usage(context.out());
        }
        else {
            configuration();
        }
    }

    private void configuration() {
        ConfigReader reader = new ConfigReader("./resources/config.smt");
        List<Plugin> plugins = new PluginTechRetriever().retrieveAllPlugins();
        Plugin toUse = new DefaultPlugin();
        for (Plugin plugin : plugins) {
            if (plugin.accept(reader.getLangageFromConfig(), reader.getTestFrameworkFromConfig(), reader.getManagementFromConfig())) {
                toUse = plugin;
                break;
            }
        }
        TreeModule module = new TreeModule.TreeModuleBuilder().withProjectBase(".").withPlugin(toUse).build();
    }
}
