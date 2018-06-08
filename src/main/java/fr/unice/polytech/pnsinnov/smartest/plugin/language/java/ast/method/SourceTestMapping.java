package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.plugin.Module;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

public class SourceTestMapping {
    private final Map<CtExecutable, Set<CtMethod>> mapping;

    public SourceTestMapping(Map<CtExecutable, Set<CtMethod>> mapping) {
        this.mapping = mapping;
    }

    public Set<CtMethod> findTestFor(CtExecutable executable) {
        return new HashSet<>(mapping.getOrDefault(executable, new HashSet<>()));
    }

    @Override
    public String toString() {
        return "SourceTestMapping{mapping=" + mapping + '}';
    }

    public static class Builder {
        private final Module module;
        private final CtModel model;

        public Builder(Module module, CtModel model) {
            this.module = module;
            this.model = model;
        }

        public SourceTestMapping build() {
            HashMap<CtExecutable, Set<CtMethod>> mapping = new HashMap<>();
            List<CtMethod> elements = model.getElements(new TypeFilter<>(CtMethod.class));
            for (CtMethod element : elements) {
                if (isTest(element)) {
                    addTestToMapping(mapping, element);
                }
            }
            return new SourceTestMapping(mapping);
        }

        private void addTestToMapping(HashMap<CtExecutable, Set<CtMethod>> mapping, CtMethod test) {
            List<CtExecutableReference> list = test.filterChildren(new TypeFilter<>(CtExecutableReference.class))
                    .list();
            for (CtExecutableReference ctExecutableReference : list) {
                mapping.putIfAbsent(ctExecutableReference.getDeclaration(), new HashSet<>());
                mapping.get(ctExecutableReference.getDeclaration()).add(test);
            }
        }

        private boolean isTest(CtMethod t) {
            return t != null && t.getPosition().getFile().toPath().startsWith(module.getTestPath());
        }
    }
}
