package fr.unice.polytech.pnsinnov.smartest.commandline;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.Plugin;

import java.io.InputStream;
import java.io.PrintStream;

public class Context {
    private InputStream in;
    private PrintStream out;
    private PrintStream err;
    private Plugin plugin;

    Context usePlugin(Plugin plugin){
        this.plugin = plugin;
        return this;
    }

    Context useIn(InputStream in) {
        this.in = in;
        return this;
    }

    Context useOut(PrintStream out) {
        this.out = out;
        return this;
    }

    Context useErr(PrintStream err) {
        this.err = err;
        return this;
    }

    public InputStream in() {
        return in;
    }

    public PrintStream out() {
        return out;
    }

    public PrintStream err() {
        return err;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
