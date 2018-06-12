package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import spoon.reflect.reference.CtExecutableReference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DependencyMap extends HashMap<CtExecutableReference, Set<CtExecutableReference>> {
    public DependencyMap inverse() {
        DependencyMap reverse = new DependencyMap();
        for (Entry<CtExecutableReference, Set<CtExecutableReference>> entry : entrySet()) {
            for (CtExecutableReference ctExecutableReference : entry.getValue()) {
                reverse.putIfAbsent(ctExecutableReference, new HashSet<>());
                reverse.get(ctExecutableReference).add(entry.getKey());
            }
        }
        return reverse;
    }
}
