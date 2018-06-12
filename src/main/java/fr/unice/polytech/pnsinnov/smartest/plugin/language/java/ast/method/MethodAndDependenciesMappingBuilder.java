package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.plugin.Module;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

public class MethodAndDependenciesMappingBuilder implements MappingBuilder {
    @Override
    public SourceTestMapping build(Module module, CtModel model) {
        Mapping mapping = new Mapping();
        DependencyMap dependencies = buildDependencyMap(model);
        List<CtMethod> methods = model.getElements(new TypeFilter<>(CtMethod.class));
        for (CtMethod method : methods) {
            if (isTest(module, method)) {
                addTestToMapping(mapping, dependencies, method);
            }
        }
        return new SourceTestMapping(mapping);
    }

    private DependencyMap buildDependencyMap(CtModel model) {
        DependencyMap dependencies = new DependencyMap();
        List<CtExecutableReference> executableReferences = model.getRootPackage().filterChildren(
                new TypeFilter<>(CtExecutableReference.class)).list();
        for (CtExecutableReference executableReference : executableReferences) {
            addDependencies(dependencies, executableReference);
        }
        return dependencies.inverse();
    }

    private void addDependencies(DependencyMap dependencies, CtExecutableReference executableReference) {
        if (!dependencies.containsKey(executableReference)) {
            List<CtExecutableReference> children = getChildren(executableReference);
            dependencies.put(executableReference, new HashSet<>(children));
            for (CtExecutableReference child : children) {
                addDependencies(dependencies, child);
                dependencies.get(executableReference).addAll(dependencies.get(child));
            }
        }
    }

    private List<CtExecutableReference> getChildren(CtExecutableReference ctExecutableReference) {
        if (ctExecutableReference.getDeclaration() != null) {
            return ctExecutableReference.getDeclaration().filterChildren(
                    new TypeFilter<>(CtExecutableReference.class)).list();
        }
        return new ArrayList<>();
    }

    private void addTestToMapping(Mapping mapping, DependencyMap dependencies, CtMethod test) {
        List<CtExecutableReference> exe = test.filterChildren(new TypeFilter<>(CtExecutableReference.class)).list();
        for (CtExecutableReference ctExecutableReference : exe) {
            linkExecutableToTest(mapping, dependencies, ctExecutableReference, test);
        }
    }

    private void linkExecutableToTest(Mapping mapping, DependencyMap dependencies,
                                      CtExecutableReference ctExecutableReference, CtMethod test) {
        if (ctExecutableReference == null) {
            return;
        }
        mapping.putIfAbsent(ctExecutableReference, new HashSet<>());
        if (mapping.get(ctExecutableReference).contains(test)) {
            return;
        }
        mapping.get(ctExecutableReference).add(test);
        Set<CtExecutableReference> executables = dependencies.getOrDefault(ctExecutableReference, new HashSet<>());
        for (CtExecutableReference executableReference : executables) {
            linkExecutableToTest(mapping, dependencies, executableReference, test);
            mapping.get(ctExecutableReference).addAll(mapping.get(executableReference));
        }
    }

    private boolean isTest(Module module, CtMethod t) {
        return t != null && t.getPosition().getFile().toPath().startsWith(module.getTestPath());
    }

    private class Mapping extends HashMap<CtExecutableReference, Set<CtMethod>> {
    }
}
