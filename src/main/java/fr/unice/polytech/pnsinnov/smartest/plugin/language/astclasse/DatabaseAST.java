package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DatabaseAST {

    private static DatabaseAST instance = null;
    private Map<ModuleImpl, ASTModule> asts;
    private Map<String, Set<String>> mapClassTests;
    private SimpleDirectedGraph<String, DefaultEdge> graphClassDependency;

    public DatabaseAST() {
        asts = new HashMap<>();
        mapClassTests = new HashMap<>();
        graphClassDependency = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
    }

    public static DatabaseAST getInstance() {
        if (instance == null)
            instance = new DatabaseAST();
        return instance;
    }

    public boolean checkExistence() {
        return new File("smartest/asts.db").exists();
    }

    public SimpleDirectedGraph<String, DefaultEdge> getGraphClassDependency() {
        return graphClassDependency;
    }

    public void loadASTs() throws IOException, ClassNotFoundException {
        File file = new File("smartest/asts.db");
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        Map<ModuleImpl, ASTStorage> aststmp = (Map<ModuleImpl, ASTStorage>) objectInputStream.readObject();
        asts = new HashMap<>();
        for (ModuleImpl module : aststmp.keySet()) {
            asts.put(module, new ASTModule(aststmp.get(module)));
        }
        objectInputStream.close();
    }

    public void addAst(ModuleImpl module, ASTModule astModule) {
        asts.put(module, astModule);
    }

    public void save() throws IOException {
        new File("smartest").mkdirs();
        File file = new File("smartest/asts.db");
        Map<ModuleImpl, ASTStorage> aststmp = new HashMap<>();
        for (ModuleImpl module : asts.keySet()) {
            aststmp.put(module, new ASTStorage(asts.get(module)));
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeObject(aststmp);
        objectOutputStream.close();
    }

    public void flush() {
        asts = new HashMap<>();
    }

    public boolean containsAST(ModuleImpl module) {
        return asts.containsKey(module);
    }

    public ASTModule getAST(ModuleImpl module) {
        return asts.get(module);
    }

    public Map<String, Set<String>> getMapClassTests() {
        return mapClassTests;
    }

    public void addClassName(String qualifiedName) {
        mapClassTests.put(qualifiedName, new HashSet<>());
    }

    public void setMapClassTests(Map<String, Set<String>> mapClassTests) {
        this.mapClassTests = mapClassTests;
    }
}
