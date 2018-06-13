package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import spoon.reflect.declaration.CtExecutable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DependencyMap extends HashMap<CtExecutable, Set<CtExecutable>> {
    public DependencyMap inverse() {
        DependencyMap reverse = new DependencyMap();
        for (Entry<CtExecutable, Set<CtExecutable>> entry : entrySet()) {
            for (CtExecutable ctExecutableReference : entry.getValue()) {
                reverse.putIfAbsent(ctExecutableReference, new HashSet<>());
                reverse.get(ctExecutableReference).add(entry.getKey());
            }
        }
        return reverse;
    }
}
