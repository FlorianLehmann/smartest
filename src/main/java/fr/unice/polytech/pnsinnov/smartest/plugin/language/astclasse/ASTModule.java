package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.diff.TestImpl;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.SerializationModelStreamer;
import spoon.support.compiler.VirtualFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ASTModule implements Serializable {

    private CtModel newAST;
    private Module module;

    public ASTModule(Module module) {
        this.module = module;
        this.newAST = buildCurrentAST();
    }

    private CtModel buildCurrentAST() {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource(module.getSrcPath().toString());
        launcher.addInputResource(module.getTestPath().toString());
        return launcher.buildModel();
    }

    public Set<Test> getTestsRelatedToChanges(String scope, Set<fr.smartest.plugin.Diff> fileDiff) {
        Set<Test> toRun = new HashSet<>();
        List<CtClass> classes = newAST.getElements(new TypeFilter<>(CtClass.class));

        List<CtClass> ctSrcClasses = matchWithCurrentAst( classes, sourceOperations( computeSrcDiff(fileDiff) ) );
        List<CtClass> ctTestsClasses = matchWithCurrentAst( classes, testsOperations( computeTestDiff(fileDiff) ) );

        Set<CtClass> tests = extractTests(classes, ctSrcClasses);
        tests.forEach(test -> toRun.add(new TestImpl(test.getQualifiedName())));
        ctTestsClasses.forEach(test -> toRun.add(new TestImpl(test.getQualifiedName())));

        return toRun;
    }

    private Set<CtClass> extractTests(List<CtClass> classes, List<CtClass> ctSrcClasses) {
        Set<CtClass> toRun = new HashSet<>();
        for (CtClass srcCls: ctSrcClasses) {
            for (CtClass cls : classes) {
                if (cls.getPosition().getFile().toPath().startsWith(module.getTestPath())) {
                    for (CtTypeReference ctTypeReference : cls.getReferencedTypes()) {
                        if (ctTypeReference.getQualifiedName().contains(srcCls.getQualifiedName())) {
                            toRun.add(cls);
                        }
                    }
                }
            }
        }
        return toRun;
    }

    private List<CtClass> matchWithCurrentAst(List<CtClass> classes, List<CtClass> ctOtherClasses) {
        List<CtClass> matchingClass = new ArrayList<>();
        for ( CtClass otherClass : ctOtherClasses ) {
            for ( CtClass cls : classes ) {
                if (cls.equals(otherClass)) {
                    matchingClass.add(cls);
                }
            }
        }
        return matchingClass;
    }

    private List<CtClass> sourceOperations(List<Operation> operations) {
        List<CtClass> classes = new ArrayList<>();
        operations.forEach(operation -> {
            //if (isInSrc(operation))
            if (operation.getSrcNode().getParent(CtClass.class)!=null) {
                classes.add(operation.getSrcNode().getParent(CtClass.class));
            }
        });
        return classes;
    }

    private boolean isInSrc(Operation operation) {
        return operation.getSrcNode().getPosition().getFile().toPath().startsWith(module.getSrcPath());
    }

    private boolean isInSrc(fr.smartest.plugin.Diff diff) {
        return diff.getPath().startsWith(module.getSrcPath());
    }

    private List<CtClass> testsOperations(List<Operation> operations) {
        List<CtClass> classes = new ArrayList<>();
        operations.forEach(operation -> {
            //if (!isInSrc(operation))
            if (operation.getSrcNode().getParent(CtClass.class)!=null)
                classes.add(operation.getSrcNode().getParent(CtClass.class));
        });
        return classes;
    }




    private List<Operation> computeSrcDiff(Set<fr.smartest.plugin.Diff> fileDiff) {
        List<Operation> diffs = new ArrayList<>();
        AstComparator astComparator = new AstComparator();

        for (fr.smartest.plugin.Diff diff : fileDiff) {
            if (diff != null && isInModule(diff) && isInSrc(diff)) {
                CtPackage ctPackage1 = createCompareModel(diff.getNewContent());
                CtPackage ctPackage2 = createCompareModel(diff.getOldContent());
                try {
                    if (ctPackage1 != null && ctPackage2 != null)
                        diffs.addAll(astComparator.compare(ctPackage2, ctPackage1).getAllOperations());
                } catch (Exception e) {

                }
            }
        }
        return diffs;
    }

    private List<Operation> computeTestDiff(Set<fr.smartest.plugin.Diff> fileDiff) {
        List<Operation> diffs = new ArrayList<>();
        AstComparator astComparator = new AstComparator();

        for (fr.smartest.plugin.Diff diff : fileDiff) {
            if (isInModule(diff) && !isInSrc(diff)) {
                diffs.addAll(astComparator.compare(createCompareModel(diff.getNewContent()), createCompareModel(diff.getOldContent())).getAllOperations());
            }
        }
        return diffs;
    }

    private CtPackage createCompareModel(String content) {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource(new VirtualFile(content));
        return launcher.buildModel().getRootPackage();
    }

    private boolean isInModule(fr.smartest.plugin.Diff diff) {
        return diff.getPath().startsWith(module.getSrcPath()) || diff.getPath().startsWith(module.getTestPath());
    }

}
