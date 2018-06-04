package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence;

import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Class;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Tree;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static Database instance = null;
    private List<Tree> classes;
    private List<Tree> tests;
    private List<String> className;

    private Database() {
        classes = new ArrayList<>();
        className = new ArrayList<>();
        tests = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void addFile(Tree file) {
        classes.add(file);
    }

    public void addClassName(String className) {
        this.className.add(className);
    }

    public List<Tree> getTree() {
        return classes;
    }

    public List<String> getClassName() {
        return className;
    }

    public void load() throws IOException, ClassNotFoundException {
        classes = (List<Tree>) read("tree.db");
        className = (List<String>) read("classes.db");
        tests = (List<Tree>) read("tests.db");
    }

    public void save() throws IOException {
        save(classes, "trees.db");
        save(className, "classes.db");
        save(className, "tests.db");
    }

    private void save(Object object, String name) throws IOException {
        new File("smartest").mkdirs();
        java.io.File file = new java.io.File("smartest/" + name);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
    }

    private Object read(String name) throws IOException, ClassNotFoundException {
        java.io.File file = new java.io.File("smartest/" + name);
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        return objectInputStream.readObject();
    }

    public boolean checkExistence() {
        return new File("smartest/trees.db").exists() ||
                new File("smartest/classes.db").exists() ||
                new File("smartest/tests.db").exists();
    }

    public void flush() {
        classes = new ArrayList<>();
        className = new ArrayList<>();
    }

    public List<Tree> getTests() {
        return tests;
    }
}
