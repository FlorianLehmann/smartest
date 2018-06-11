package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.plugin.Module;
import spoon.reflect.CtModel;

public interface MappingBuilder {
    SourceTestMapping build(Module module, CtModel model);
}
