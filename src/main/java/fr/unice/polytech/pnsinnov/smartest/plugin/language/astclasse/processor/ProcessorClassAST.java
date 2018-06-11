package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.processor;

import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.DatabaseAST;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;

import java.util.*;


public class ProcessorClassAST extends AbstractProcessor<CtClass> {

    private DatabaseAST database = DatabaseAST.getInstance();
    private Set<String> classNames = new HashSet<>();

    @Override
    public boolean isToBeProcessed(CtClass candidate) {
        return true;
    }

    @Override
    public void process(CtClass candidate) {
        classNames.add(candidate.getQualifiedName());

        if (!database.getGraphClassDependency().containsVertex(candidate.getQualifiedName()))
            database.getGraphClassDependency().addVertex(candidate.getQualifiedName());

        for (CtTypeReference<?> ctTypeReference : candidate.getReferencedTypes()) {
            if (!ctTypeReference.isPrimitive()) {
                if (!database.getGraphClassDependency().containsVertex(ctTypeReference.toString()))
                    database.getGraphClassDependency().addVertex(ctTypeReference.getQualifiedName());
                if (!candidate.getQualifiedName().equals(ctTypeReference.getQualifiedName()))
                    database.getGraphClassDependency().addEdge(candidate.getQualifiedName(), ctTypeReference.getQualifiedName());

                if (ctTypeReference.getActualTypeArguments().size()!=0) {
                    List<String> types = getGenerics(ctTypeReference);
                    for (String typeName: types) {
                        if (!database.getGraphClassDependency().containsVertex(typeName))
                            database.getGraphClassDependency().addVertex(typeName);
                        database.getGraphClassDependency().addEdge(candidate.getQualifiedName(), typeName);
                    }
                }

            }
        }


    }

    @Override
    public void processingDone() {
        super.processingDone();
        refineDependencies();
    }

    private void refineDependencies() {
        Set<String> vertices = new HashSet<>(database.getGraphClassDependency().vertexSet());
        for (String vertexName : vertices) {
            if (!classNames.contains(vertexName)) {
                database.getGraphClassDependency().removeVertex(vertexName);
            }
        }
    }

    private List<String> getGenerics(CtTypeReference<?> ctTypeReference) {
        if (ctTypeReference.getActualTypeArguments().size()==0) {
            return Collections.singletonList(ctTypeReference.getQualifiedName());
        } else {
            List<String> names = new ArrayList<>();
            for (CtTypeReference<?> ctType: ctTypeReference.getActualTypeArguments()) {
                if (ctType.getTypeDeclaration() != null)
                    names.add(ctType.getTypeDeclaration().getQualifiedName());
                names.addAll(getGenerics(ctType));
            }
            return names;
        }
    }
}