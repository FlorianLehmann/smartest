package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.plugin.Module;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodMappingBuilder implements MappingBuilder {
    @Override
    public SourceTestMapping build(Module module, CtModel model) {
        HashMap<CtExecutable, Set<CtMethod>> mapping = new HashMap<>();
        List<CtMethod> elements = model.getElements(new TypeFilter<>(CtMethod.class));
        for (CtMethod element : elements) {
            if (isTest(module, element)) {
                addTestToMapping(mapping, element);
            }
        }
        return new SourceTestMapping(mapping);
    }

    private void addTestToMapping(HashMap<CtExecutable, Set<CtMethod>> mapping, CtMethod test) {
        List<CtExecutableReference> exe = test.filterChildren(new TypeFilter<>(CtExecutableReference.class)).list();
        for (CtExecutableReference ctExecutableReference : exe) {
            mapping.putIfAbsent(ctExecutableReference.getDeclaration(), new HashSet<>());
            linkExecutableToTest(mapping, ctExecutableReference, test);
        }
    }

    protected void linkExecutableToTest(HashMap<CtExecutable, Set<CtMethod>> mapping,
                                        CtExecutableReference ctExecutableReference, CtMethod test) {
        mapping.get(ctExecutableReference.getDeclaration()).add(test);
    }

    private boolean isTest(Module module, CtMethod t) {
        return t != null && t.getPosition().getFile().toPath().startsWith(module.getTestPath());
    }
}
