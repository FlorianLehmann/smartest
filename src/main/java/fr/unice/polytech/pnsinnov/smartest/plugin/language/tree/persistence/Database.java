package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence;

import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Tree;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static Database instance = null;
    private Map<String, Tree> tree;
    private List<String> className;

    private Database() {
        tree = new HashMap<>();
        className = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void addFile(String path, Tree file) {
        tree.put(path, file);
    }

    public void addClassName(String className) {
        this.className.add(className);
    }

    public Map<String, Tree> getTree() {
        return tree;
    }

    public List<String> getClassName() {
        return className;
    }

    public void load() throws IOException, ClassNotFoundException {
        tree = (Map<String, Tree>) read("tree.db");
        className = (List<String>) read("classes.db");
    }

    public void save() throws IOException {
        save(tree, "trees.db");
        save(className, "classes.db");
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

    public void flush() {
        tree = new HashMap<>();
        className = new ArrayList<>();
    }
}
