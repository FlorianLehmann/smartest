package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.plugin.Diff;
import fr.smartest.plugin.Language;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.diff.TestImplementation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spoon.reflect.declaration.CtMethod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodAST implements Language {
    private static final Logger logger = LogManager.getLogger(MethodAST.class);
    private static final String IDENTIFIER = "MethodAST";
    private List<AST> modulesAST;

    @Override
    public void setUp(List<Module> modules) {
        logger.debug("Set up MethodAST with " + modules.size() + " modules");
        modulesAST = modules.stream().map(AST::new).collect(Collectors.toList());
    }

    @Override
    public Set<Test> getTestsRelatedToChanges(String scope, Set<Diff> diffs) {
        Set<Test> tests = new HashSet<>();
        for (AST ast : modulesAST) {
            Set<CtMethod> test = ast.getTestsRelatedToChanges(diffs);
            tests.addAll(test.stream()
                    .map(this::methodToIdentifier)
                    .map(TestImplementation::new)
                    .collect(Collectors.toSet()));
        }
        return tests;
    }

    private String methodToIdentifier(CtMethod method) {
        return method.getDeclaringType().getQualifiedName() + '#' + method.getSimpleName();
    }

    @Override
    public void save() {

    }


    @Override
    public boolean accept(String s) {
        return s != null && IDENTIFIER.equalsIgnoreCase(s.trim());
    }
}
