package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import spoon.reflect.factory.Factory;
import spoon.support.SerializationModelStreamer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

public class ASTModule implements Serializable {

    private Factory src;
    private Factory tests;

    public ASTModule(Factory src, Factory tests) {
        this.tests = tests;
        this.src = src;
    }

    public ASTModule(ASTStorage astStorage) throws IOException {
        this.src = new SerializationModelStreamer().load(new ByteArrayInputStream(astStorage.getSrc()));
        this.tests = new SerializationModelStreamer().load(new ByteArrayInputStream(astStorage.getTests()));
    }

    public Factory getSrc() {
        return src;
    }

    public Factory getTests() {
        return tests;
    }


}
