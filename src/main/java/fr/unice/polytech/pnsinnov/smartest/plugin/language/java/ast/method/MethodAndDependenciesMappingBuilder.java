
package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.plugin.Module;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

public class MethodAndDependenciesMappingBuilder implements MappingBuilder {
    private static final Logger logger = LogManager.getLogger(MethodAndDependenciesMappingBuilder.class);

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
        List<CtExecutable> executables = model.getRootPackage()
                .filterChildren(new TypeFilter<>(CtExecutable.class)).list();
        for (CtExecutable executable : executables) {
            addPolymorphism(dependencies, executable);
        }
        for (CtExecutable executable : executables) {
            addDependencies(dependencies, executable);
        }
        return dependencies.inverse();
    }

    private void addDependencies(DependencyMap dependencies, CtExecutable executable) {
        if (!dependencies.containsKey(executable)) {
            List<CtExecutable> children = getChildren(executable);
            dependencies.put(executable, new HashSet<>(children));
            for (CtExecutable child : children) {
                addDependencies(dependencies, child);
                Set<CtExecutable> ctExecutables = dependencies.get(child);
                dependencies.get(executable).addAll(ctExecutables);
            }
        }
    }

    private void addPolymorphism(DependencyMap dependencies, CtExecutable executable) {
        if (executable == null) {
            return;
        }
        try {
            for (CtExecutableReference overridingExecutable : overrides(executable)) {
                CtExecutable declaration = overridingExecutable.getDeclaration();
                if (declaration != null) {
                    addDependencies(dependencies, declaration);
                    dependencies.get(declaration).add(executable);
                    addDependencies(dependencies, executable);
                    dependencies.get(declaration).addAll(dependencies.get(executable));
                    addPolymorphism(dependencies, declaration);
                }
            }
        }
        catch (Exception e) {
            logger.warn("Error while searching for polymorphism");
        }
    }

    private Set<CtExecutableReference> overrides(CtExecutable executable) {
        CtType parent = executable.getParent(CtType.class);
        CtTypeReference reference = parent.getReference();
        Set<CtExecutableReference> overridden = new HashSet<>();
        CtExecutableReference overridingExecutable = executable.getReference().getOverridingExecutable();
        if (overridingExecutable != null) {
            overridden.add(overridingExecutable);
        }
        Set<CtTypeReference> superInterfaces = new HashSet<>(reference.getSuperInterfaces());
        CtTypeReference superclass = reference.getSuperclass();
        if (superclass != null) {
            superInterfaces.add(superclass);
        }

        for (CtTypeReference superInterface : superInterfaces) {
            for (CtExecutableReference<?> ctExecutableReference : superInterface.getAllExecutables()) {
                if (executable.getReference().isOverriding(ctExecutableReference)) {
                    overridden.add(ctExecutableReference);
                }
            }
        }
        return overridden;
    }

    private List<CtExecutable> getChildren(CtExecutable ctExecutable) {
        if (ctExecutable != null) {
            return ctExecutable.filterChildren(new TypeFilter<>(CtExecutableReference.class))
                    .<CtExecutableReference, CtExecutable>map(CtExecutableReference::getDeclaration).list();
        }
        return new ArrayList<>();
    }

    private void addTestToMapping(Mapping mapping, DependencyMap dependencies, CtMethod test) {
        for (Map.Entry<CtExecutable, Set<CtExecutable>> entry : dependencies.entrySet()) {
            if (entry.getValue().contains(test)) {
                mapping.putIfAbsent(entry.getKey(), new HashSet<>());
                mapping.get(entry.getKey()).add(test);
            }
        }
    }

    private boolean isTest(Module module, CtMethod t) {
        return t != null && t.getPosition().getFile().toPath().startsWith(module.getTestPath());
    }

    private class Mapping extends HashMap<CtExecutable, Set<CtMethod>> {
    }
}
