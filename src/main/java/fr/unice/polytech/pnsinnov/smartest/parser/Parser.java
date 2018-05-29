package fr.unice.polytech.pnsinnov.smartest.parser;

import fr.unice.polytech.pnsinnov.smartest.parser.processors.ProcessorClass;
import fr.unice.polytech.pnsinnov.smartest.parser.processors.ProcessorTests;
import spoon.Launcher;
import spoon.processing.Processor;
import spoon.reflect.CtModel;

import java.util.List;

public class Parser {

    private String testsPath;
    private String sourceCodePath;

    public Parser(String sourceCodePath, String testsPath) {
        this.sourceCodePath = sourceCodePath;
        this.testsPath = testsPath;
    }

    public void parse(String path, Processor processor) {
        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addProcessor(processor);
        launcher.buildModel();
        launcher.process();
    }

    public void sourceCodeParsing() {
        parse(this.sourceCodePath, new ProcessorClass());
    }

    public void testsParsing() {
        parse(this.testsPath, new ProcessorTests());
    }
}
