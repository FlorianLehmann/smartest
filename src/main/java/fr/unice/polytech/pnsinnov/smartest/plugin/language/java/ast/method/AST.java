package fr.unice.polytech.pnsinnov.smartest.plugin.language.java.ast.method;

import fr.smartest.plugin.Diff;
import fr.smartest.plugin.Module;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.operations.Operation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AST {
    private static final Logger logger = LogManager.getLogger(AST.class);
    private final Module module;
    private final CtModel model;

    public AST(Module module) {
        this.module = module;
        model = createModel();
    }

    private CtModel createModel() {
        return buildModel(module.getSrcPath(), module.getTestPath());
    }

    private CtModel buildModel(Path... paths) {
        Launcher launcher = new Launcher();
        for (Path path : paths) {
            launcher.addInputResource(path.toString());
        }
        return launcher.buildModel();
    }


    public Set<CtMethod> getTestsRelatedToChanges(Set<Diff> diffs) {
        Set<CtMethod> tests = new HashSet<>();
        SourceTestMapping mapping = new SourceTestMapping.Builder(module, model).build();
        List<CtExecutable> executables = model.getRootPackage().getElements(new TypeFilter<>(CtExecutable.class));
        for (Diff diff : diffs) {
            if (isInModule(diff)) {
                tests.addAll(findTests(mapping, executables, diff));
            }
        }
        return tests;
    }

    private Set<CtMethod> findTests(SourceTestMapping mapping, List<CtExecutable> executables, Diff diff) {
        Set<CtMethod> tests = new HashSet<>();
        List<Operation> operations = new AstComparator()
                .compare(diff.getNewContent(), diff.getOldContent())
                .getAllOperations();
        for (Operation operation : operations) {
            CtExecutable parent = operation.getSrcNode().getParent(CtExecutable.class);
            for (CtExecutable executable : executables) {
                if (equalsExecutable(executable, parent)) {
                    tests.addAll(mapping.findTestFor(executable));
                }
            }
        }
        return tests;
    }

    private boolean equalsExecutable(CtExecutable executable1, CtExecutable executable2) {
        if (executable1 == null || executable2 == null) {
            return false;
        }
        CtType type1 = executable1.getParent(CtType.class);
        CtType type2 = executable2.getParent(CtType.class);
        CtExecutableReference reference1 = executable1.getReference();
        CtExecutableReference reference2 = executable2.getReference();
        return reference1.equals(reference2) && type1.getQualifiedName().equals(type2.getQualifiedName());
    }

    private boolean isInModule(Diff diff) {
        return diff.getPath().startsWith(module.getSrcPath()) || diff.getPath().startsWith(module.getTestPath());
    }
}
