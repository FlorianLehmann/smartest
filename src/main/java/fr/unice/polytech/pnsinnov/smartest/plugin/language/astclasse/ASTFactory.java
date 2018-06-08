package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import spoon.Launcher;
import spoon.reflect.factory.Factory;

import java.io.IOException;

public class ASTFactory {

    private ModuleImpl module;

    public ASTFactory(ModuleImpl module) {
        this.module = module;
    }

    private Factory build(String path) throws IOException {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource(path);
        launcher.buildModel();
        return launcher.getFactory();
    }

    public ASTModule build() throws IOException {
        Factory src = build(module.getSrcPath());
        Factory tests = build(module.getTestPath());
        return new ASTModule(src, tests);
    }
}
