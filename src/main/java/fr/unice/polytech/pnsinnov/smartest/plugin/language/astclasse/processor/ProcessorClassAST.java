package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.processor;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;


public class ProcessorClassAST extends AbstractProcessor<CtClass> {

    private SimpleDirectedGraph<String, DefaultEdge> graphClassDependency;
    private Set<String> classNames = new HashSet<>();

    public ProcessorClassAST(SimpleDirectedGraph<String, DefaultEdge> graphClassDependency) {
        this.graphClassDependency = graphClassDependency;
    }

    @Override
    public boolean isToBeProcessed(CtClass candidate) {
        return true;
    }

    @Override
    public void process(CtClass candidate) {
        classNames.add(candidate.getQualifiedName());
        //System.out.println(candidate.getQualifiedName());

        if (!graphClassDependency.containsVertex(candidate.getQualifiedName()))
            graphClassDependency.addVertex(candidate.getQualifiedName());

        for (CtTypeReference<?> ctTypeReference : candidate.getReferencedTypes()) {
            if (!ctTypeReference.isPrimitive() && candidate.getParent(new TypeFilter<>(CtClass.class)) == null) {
                if (!graphClassDependency.containsVertex(ctTypeReference.toString()))
                    graphClassDependency.addVertex(ctTypeReference.getQualifiedName());
                if (!candidate.getQualifiedName().equals(ctTypeReference.getQualifiedName()))
                    graphClassDependency.addEdge(candidate.getQualifiedName(), ctTypeReference.getQualifiedName());

                /*if (ctTypeReference.getActualTypeArguments().size()!=0) {
                    List<String> types = getGenerics(ctTypeReference);
                    for (String typeName: types) {
                        if (!database.getGraphClassDependency().containsVertex(typeName))
                            database.getGraphClassDependency().addVertex(typeName);
                        database.getGraphClassDependency().addEdge(candidate.getQualifiedName(), typeName);
                    }
                }*/

            }
        }


    }

    @Override
    public void processingDone() {
        super.processingDone();
        refineDependencies();
    }

    private void refineDependencies() {
        Set<String> vertices = new HashSet<>(graphClassDependency.vertexSet());
        for (String vertexName : vertices) {
            if (!classNames.contains(vertexName)) {
                graphClassDependency.removeVertex(vertexName);
            }
        }
    }

    private List<String> getGenerics(CtTypeReference<?> ctTypeReference) {
        if (ctTypeReference.getActualTypeArguments().size() == 0) {
            return Collections.singletonList(ctTypeReference.getQualifiedName());
        } else {
            List<String> names = new ArrayList<>();
            for (CtTypeReference<?> ctType : ctTypeReference.getActualTypeArguments()) {
                if (ctType.getTypeDeclaration() != null)
                    names.add(ctType.getTypeDeclaration().getQualifiedName());
                names.addAll(getGenerics(ctType));
            }
            return names;
        }
    }
}