package fr.unice.polytech.pnsinnov.smartest.parser;

import fr.unice.polytech.pnsinnov.smartest.parser.processors.ProcessorClass;
import fr.unice.polytech.pnsinnov.smartest.parser.processors.ProcessorTests;
import spoon.Launcher;
import spoon.reflect.CtModel;

import java.util.List;

public class Parser {

    private String testsPath;
    private String sourceCodePath;

    public Parser(String sourceCodePath, String testsPath) {
        this.sourceCodePath = sourceCodePath;
        this.testsPath = testsPath;
    }

    public void sourceCodeParsing() {
        Launcher launcher = new Launcher();
        launcher.addInputResource(this.sourceCodePath);
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addProcessor(new ProcessorClass());
        launcher.buildModel();
        launcher.process();
    }

    public void testsParsing() {
        Launcher launcher = new Launcher();
        launcher.addInputResource(this.testsPath);
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addProcessor(new ProcessorTests());
        launcher.buildModel();
        launcher.process();
    }
}
