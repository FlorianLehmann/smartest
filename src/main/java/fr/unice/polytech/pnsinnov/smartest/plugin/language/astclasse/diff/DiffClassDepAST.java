package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.diff;

import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.ASTFactory;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.ASTModule;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.DatabaseAST;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.ModuleImpl;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.processor.ProcessorClassAST;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.processor.ProcessorTestsAST;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import org.jgrapht.graph.DefaultEdge;
import spoon.reflect.declaration.CtClass;

import java.io.IOException;
import java.util.*;

public class DiffClassDepAST {

    private DatabaseAST database = DatabaseAST.getInstance();

    public Set<Test> getTestRelatedTochanges(List<ModuleImpl> modules) throws IOException {

        Set<Test> toRun = new HashSet<>();

        for (ModuleImpl module : modules) {
            database.setMapClassTests(new HashMap<>());
            ASTModule oldast = database.getAST(module);
            ASTModule newast = new ASTFactory(module).build();

            AstComparator astComparator = new AstComparator();
            Diff diff = astComparator.compare(oldast.getSrc().getModel().getRootPackage(), newast.getSrc().getModel().getRootPackage());
            diff.getAllOperations().forEach(operation -> {
                if (operation.getSrcNode().getParent(CtClass.class)!=null)
                    database.addClassName(operation.getSrcNode().getParent(CtClass.class).getQualifiedName());
            });

            //on applique le proc sur l'ast soucre
            newast.getSrc().getModel().processWith(new ProcessorClassAST());
            Set<String> klassNames = new HashSet<>(database.getMapClassTests().keySet());
            for (String cls : klassNames) {
                Set<String> deps = getClassDependencies(cls);
                for (String clsName:deps) {
                    database.addClassName(clsName);
                }
            }

            newast.getTests().getModel().processWith(new ProcessorTestsAST());
            for (String name : database.getMapClassTests().keySet()) {
                for (String test : database.getMapClassTests().get(name)) {
                    toRun.add(new TestImpl(test));
                }
            }

            astComparator = new AstComparator();
            diff = astComparator.compare(oldast.getTests().getModel().getRootPackage(), newast.getTests().getModel().getRootPackage());
            diff.getAllOperations().forEach(operation -> {
                if (operation.getSrcNode().getParent(CtClass.class)!=null)
                    toRun.add(new TestImpl(operation.getSrcNode().getParent(CtClass.class).getQualifiedName()));
            });
        }
        return toRun;

    }

    private Set<String> getClassDependencies(String cls) {
        if (database.getGraphClassDependency().inDegreeOf(cls)==0) {
            return Collections.singleton(cls);
        } else {
            Set<String> deps = new HashSet<>();
            deps.add(cls);
            for (DefaultEdge edge: database.getGraphClassDependency().incomingEdgesOf(cls)) {
                String vertexName = database.getGraphClassDependency().getEdgeSource(edge);
                deps.add(vertexName);
                deps.addAll(getClassDependencies(vertexName));
            }
            return deps;
        }
    }

}
