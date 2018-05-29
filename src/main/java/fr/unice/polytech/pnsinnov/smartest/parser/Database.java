package fr.unice.polytech.pnsinnov.smartest.parser;

import java.util.*;

public class Database implements DatabaseCodeAnalysis {

    private Map<String, Set<String>> testsClassMapping;

    private static Database instance = null;

    private Database() {
        testsClassMapping = new HashMap<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    @Override
    public Set<String> getTestLinkToClass(String cls) {
        return testsClassMapping.get(cls);
    }

    @Override
    public boolean contain(String cls) {
        return testsClassMapping.containsKey(cls);
    }

    @Override
    public void addClass(String cls) {
        testsClassMapping.put(cls, new HashSet<>());
    }

    @Override
    public void linkClassToTest(String cls, String clsTest) {
        testsClassMapping.get(cls).add(clsTest);
    }


}
