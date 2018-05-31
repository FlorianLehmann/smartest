package fr.unice.polytech.pnsinnov.smartest.commandline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmartestTest {
    private Smartest smartest;
    private ByteArrayOutputStream outByteArray;

    @BeforeEach
    void setUp() {
        ByteArrayInputStream in = new ByteArrayInputStream("\n".getBytes());
        outByteArray = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outByteArray);
        PrintStream err = new PrintStream(new ByteArrayOutputStream());
        smartest = new Smartest(new Context.ContextBuilder().withInputStream(in).withOutStream(out).withErrStream(err).build());
    }

    @Test
    public void basicTest(){
        org.junit.Assert.assertFalse(false);
    }

    @Test
    void commit() {
        boolean[] committed = {false};
        smartest.getCommandLine().addSubcommand("commit", new FakeCommit(committed));
        smartest.parse(new String[]{"commit", "-m", "Initial commit"});
        assertTrue(committed[0]);
    }

    @CommandLine.Command(name = "commit", description = "Mock the commit feature")
    private class FakeCommit implements Runnable {
        private final boolean[] committed;

        @CommandLine.Option(names = {"-m", "--message"}, required = true, description = "Put an useless message here")
        private String message;

        public FakeCommit(boolean[] committed) {
            this.committed = committed;
        }

        @Override
        public void run() {
            committed[0] = true;
            assertEquals("Initial commit", message);
        }
    }
}