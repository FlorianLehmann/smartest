package fr.unice.polytech.pnsinnov.smartest.parser;

import java.util.*;

public class Database {

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

    public boolean contain(String cls) {
        return testsClassMapping.containsKey(cls);
    }

    public void addClass(String cls) {
        testsClassMapping.put(cls, new HashSet<>());
    }

    public void linkClassToTest(String cls, String clsTest) {
        testsClassMapping.get(cls).add(clsTest);
    }


}
