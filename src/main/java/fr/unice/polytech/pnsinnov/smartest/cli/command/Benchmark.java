package fr.unice.polytech.pnsinnov.smartest.cli.command;

import fr.smartest.exceptions.PluginException;
import fr.unice.polytech.pnsinnov.smartest.cli.Command;
import picocli.CommandLine;

import java.time.Duration;
import java.util.HashMap;

@CommandLine.Command(name = "bench", description = "Run tests with every compatible language plugin, then display performance results.")
public class Benchmark extends Command{
    @Override
    public void run() {
        try {
            HashMap<String, Duration> results = smartest.bench();

            context.out().println("Results for the " + results.keySet().size() + " plugins found");

            for (String key :
                    results.keySet()) {

                context.out().println("Plugin " + key + " executed in " + durationPrinter(results.get(key)));

            }
        } catch (PluginException e) {
            context.out().print("Error while running bench occured : ");
            context.out().println(e.getMessage());
        }
    }

    private String durationPrinter(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }
}
