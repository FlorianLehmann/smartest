package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.SpoonClassNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodWithPolymorphismBuilder extends MethodAndDependenciesMappingBuilder {
    private static final Logger logger = LogManager.getLogger(MethodWithPolymorphismBuilder.class);

    @Override
    protected DependencyMap buildDependencyMap(CtModel model) {
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

    private void addPolymorphism(DependencyMap dependencies, CtExecutable executable) {
        if (executable == null) {
            return;
        }
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
            try {
                for (CtExecutableReference<?> ctExecutableReference : superInterface.getAllExecutables()) {
                    if (executable.getReference().getSignature().equals(ctExecutableReference.getSignature())) {
                        overridden.add(ctExecutableReference);
                    }
                }
            }
            catch (SpoonClassNotFoundException e) {
                logger.warn(e);
            }
        }
        return overridden;
    }
}
