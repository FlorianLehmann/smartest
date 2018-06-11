package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

public class MethodAndDependenciesMappingBuilder extends MethodMappingBuilder {
    @Override
    protected void linkExecutableToTest(HashMap<CtExecutable, Set<CtMethod>> mapping,
                                        CtExecutableReference executable, CtMethod test) {
        Map<CtExecutableReference, Set<CtExecutableReference>> dependencies = new HashMap<>();
        for (CtExecutableReference ctExecutableReference : findDependencies(dependencies, executable)) {
            mapping.putIfAbsent(ctExecutableReference.getDeclaration(), new HashSet<>());
            mapping.get(ctExecutableReference.getDeclaration()).add(test);
        }
    }

    private Set<CtExecutableReference> findDependencies(Map<CtExecutableReference, Set<CtExecutableReference>> dep,
                                                        CtExecutableReference executable) {
        if (!dep.containsKey(executable)) {
            dep.putIfAbsent(executable, new HashSet<>());
            dep.get(executable).add(executable);
            if (executable.getDeclaration() != null) {
                List<CtExecutableReference> children = executable.getDeclaration().filterChildren(
                        new TypeFilter<>(CtExecutableReference.class)).list();
                for (CtExecutableReference child : children) {
                    dep.get(executable).addAll(findDependencies(dep, child));
                }
            }
        }
        return dep.get(executable);
    }
}
