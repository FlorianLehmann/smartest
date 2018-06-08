package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import spoon.support.SerializationModelStreamer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class ASTStorage implements Serializable{

    private byte[] src;
    private byte[] tests;

    public ASTStorage(ASTModule astModule) throws IOException {
        ByteArrayOutputStream src = new ByteArrayOutputStream();
        ByteArrayOutputStream tests = new ByteArrayOutputStream();
        new SerializationModelStreamer().save(astModule.getSrc(), src);
        this.src = src.toByteArray();
        new SerializationModelStreamer().save(astModule.getTests(), tests);
        this.tests = tests.toByteArray();
    }

    public byte[] getSrc() {
        return src;
    }

    public byte[] getTests() {
        return tests;
    }
}
