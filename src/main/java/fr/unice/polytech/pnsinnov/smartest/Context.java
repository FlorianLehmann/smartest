package fr.unice.polytech.pnsinnov.smartest;

import java.io.InputStream;
import java.io.PrintStream;

public class Context {
    private final Smartest smartest;
    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;

    private Context(ContextBuilder builder) {
        this.in = builder.in;
        this.out = builder.out;
        this.err = builder.err;
        this.smartest = builder.smartest;
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

    public Smartest smartest() {
        return smartest;
    }

    public static class ContextBuilder {

        private InputStream in;
        private PrintStream out;
        private PrintStream err;
        private Smartest smartest;

        public ContextBuilder withInputStream(InputStream in) {
            this.in = in;
            return this;
        }

        public ContextBuilder withOutStream(PrintStream out) {
            this.out = out;
            return this;
        }

        public ContextBuilder withErrStream(PrintStream err) {
            this.err = err;
            return this;
        }

        public ContextBuilder withSmartest(Smartest smartest) {
            this.smartest = smartest;
            return this;
        }

        public Context build() {
            return new Context(this);
        }
    }
}
