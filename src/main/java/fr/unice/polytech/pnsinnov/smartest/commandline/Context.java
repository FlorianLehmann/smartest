package fr.unice.polytech.pnsinnov.smartest.commandline;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.Plugin;

import java.io.InputStream;
import java.io.PrintStream;

public class Context {
    private InputStream in;
    private PrintStream out;
    private PrintStream err;
    private Plugin plugin;

    private Context(ContextBuilder builder){
        this.plugin = builder.plugin;
        this.in = builder.in;
        this.out = builder.out;
        this.err = builder.err;
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

    public static class ContextBuilder {

        InputStream in;
        PrintStream out;
        PrintStream err;
        Plugin plugin;

        public ContextBuilder withInputStream(InputStream in){
            this.in = in;
            return this;
        }

        public ContextBuilder withOutStream(PrintStream out){
            this.out = out;
            return this;
        }

        public ContextBuilder withErrStream(PrintStream err){
            this.err = err;
            return this;
        }

        public ContextBuilder withPlugin(Plugin plugin){
            this.plugin = plugin;
            return this;
        }

        public Context build(){
            return new Context(this);
        }


    }
}
