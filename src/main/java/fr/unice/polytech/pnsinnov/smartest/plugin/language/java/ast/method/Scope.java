package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.plugin.Module;
import spoon.reflect.CtModel;

public enum Scope {
    METHOD(new MethodMappingBuilder()),
    METHOD_AND_DEPENDENCIES(new MethodAndDependenciesMappingBuilder());

    private final MappingBuilder mappingBuilder;

    Scope(MappingBuilder mappingBuilder) {
        this.mappingBuilder = mappingBuilder;
    }

    public static Scope value(String identifier) {
        try {
            return valueOf(identifier.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            return METHOD_AND_DEPENDENCIES;
        }
    }

    public SourceTestMapping build(Module module, CtModel model) {
        return mappingBuilder.build(module, model);
    }
}
