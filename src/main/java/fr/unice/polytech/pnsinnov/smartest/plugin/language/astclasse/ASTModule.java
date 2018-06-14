package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.diff.TestImpl;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.processor.ProcessorClassAST;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.operations.Operation;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.compiler.VirtualFile;

import java.io.Serializable;
import java.util.*;

public class ASTModule implements Serializable {

    private CtModel newAST;
    private Module module;
    private String scope;
    private SimpleDirectedGraph<String, DefaultEdge> graphClassDependency;

    public ASTModule(Module module, String scope) {
        this.module = module;
        this.scope = scope;
        this.graphClassDependency = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        this.newAST = buildCurrentAST();
    }

    private CtModel buildCurrentAST() {
        Launcher launcher = new Launcher();
        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource(module.getSrcPath().toString());
        launcher.addInputResource(module.getTestPath().toString());

        CtModel model = launcher.buildModel();
        if ("classDep".equalsIgnoreCase(scope)) {
            launcher.addProcessor(new ProcessorClassAST(graphClassDependency));
            launcher.process();
        }
        return model;
    }

    public Set<Test> getTestsRelatedToChanges(String scope, Set<fr.smartest.plugin.Diff> fileDiff) {
        Set<Test> toRun = new HashSet<>();
        List<CtClass> classes = newAST.getElements(new TypeFilter<>(CtClass.class));

        List<CtClass> ctSrcClasses = matchWithCurrentAst( classes, sourceOperations( computeSrcDiff(fileDiff) ) );
        List<CtClass> ctTestsClasses = matchWithCurrentAst( classes, testsOperations( computeTestDiff(fileDiff) ) );

        Set<CtClass> tests = null;
        if ("classDep".equalsIgnoreCase(scope)) {
            Set<String> classToTest = new HashSet<>();
            for (CtClass ctClass : ctSrcClasses) {
                classToTest.addAll(getClassDependencies(ctClass.getQualifiedName(), new HashSet<>()));
            }
            tests = extractTestsDep(classes, classToTest);
        } else {
            tests = extractTests(classes, ctSrcClasses);
        }
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

    private Set<CtClass> extractTestsDep(List<CtClass> classes, Set<String> ctSrcClasses) {
        Set<CtClass> toRun = new HashSet<>();
        for (String srcCls : ctSrcClasses) {
            for (CtClass cls : classes) {
                if (cls.getPosition().getFile().toPath().startsWith(module.getTestPath())) {
                    for (CtTypeReference ctTypeReference : cls.getReferencedTypes()) {
                        if (ctTypeReference.getQualifiedName().contains(srcCls)) {
                            toRun.add(cls);
                        }
                    }
                }
            }
        }
        return toRun;
    }

    private Set<String> getClassDependencies(String cls, Set<String> deps) {
        //System.out.println(cls);
        if (graphClassDependency.inDegreeOf(cls) == 0) {
            return Collections.singleton(cls);
        } else {
            if (!deps.contains(cls)) {
                deps.add(cls);
                for (DefaultEdge edge : graphClassDependency.incomingEdgesOf(cls)) {
                    String vertexName = graphClassDependency.getEdgeSource(edge);
                    deps.addAll(getClassDependencies(vertexName, deps));
                    deps.add(vertexName);
                }
            }
            return deps;
        }
    }

    private List<CtClass> matchWithCurrentAst(List<CtClass> classes, List<CtClass> ctOtherClasses) {
        List<CtClass> matchingClass = new ArrayList<>();
        for ( CtClass otherClass : ctOtherClasses ) {
            for ( CtClass cls : classes ) {
                if (cls.getQualifiedName().equals(otherClass.getQualifiedName())) {
                    matchingClass.add(cls);
                }
            }
        }
        return matchingClass;
    }

    private List<CtClass> sourceOperations(List<Operation> operations) {
        List<CtClass> classes = new ArrayList<>();
        operations.forEach(operation -> {
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
            if (operation.getSrcNode().getParent(CtClass.class) != null) {
                classes.add(operation.getSrcNode().getParent(CtClass.class));
            }
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
                    if (ctPackage1 != null && ctPackage2 != null) {
                        diffs.addAll(astComparator.compare(ctPackage2, ctPackage1).getAllOperations());
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
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
