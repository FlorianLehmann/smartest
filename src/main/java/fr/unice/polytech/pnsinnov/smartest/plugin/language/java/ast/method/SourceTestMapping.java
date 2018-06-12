package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SourceTestMapping {
    private final Map<CtExecutableReference, Set<CtMethod>> mapping;

    public SourceTestMapping(Map<CtExecutableReference, Set<CtMethod>> mapping) {
        this.mapping = mapping;
    }

    public Set<CtMethod> findTestFor(CtExecutableReference executable) {
        return new HashSet<>(mapping.getOrDefault(executable, new HashSet<>()));
    }

    @Override
    public String toString() {
        return "SourceTestMapping{mapping=" + mapping + '}';
    }
}
