package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence;

import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.File;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static Database instance = null;
    private Map<Path, File> tree;
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

    public void addFile(Path path, File file) {
        tree.put(path, file);
    }

    public void addClassName(String className) {
        this.className.add(className);
    }

    public Map<Path, File> getTree() {
        return tree;
    }

    public List<String> getClassName() {
        return className;
    }
}
