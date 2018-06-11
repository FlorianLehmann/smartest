package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.processor;

import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.DatabaseAST;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProcessorTestsAST extends AbstractProcessor<CtClass> {

    private DatabaseAST database = DatabaseAST.getInstance();

    @Override
    public boolean isToBeProcessed(CtClass candidate) {
        return true;
    }

    @Override
    public void process(CtClass candidate) {
        for (CtTypeReference<?> ctTypeReference: candidate.getReferencedTypes()) {
            if (database.getMapClassTests().containsKey(ctTypeReference.getQualifiedName())) {
                database.getMapClassTests().get(ctTypeReference.getQualifiedName()).add(candidate.getQualifiedName());
            }
            if (ctTypeReference.getActualTypeArguments().size()!=0) {
                List<String> types = getGenerics(ctTypeReference);
                for (String typeName : types) {
                    if (database.getMapClassTests().containsKey(typeName))
                        database.getMapClassTests().get(typeName).add(candidate.getQualifiedName());

                }
            }


        }
    }

    private List<String> getGenerics(CtTypeReference<?> ctTypeReference) {
        if (ctTypeReference.getActualTypeArguments().size()==0) {
            return Collections.singletonList(ctTypeReference.getQualifiedName());
        } else {
            List<String> names = new ArrayList<>();
            for (CtTypeReference ctType : ctTypeReference.getActualTypeArguments()) {
                if (ctType.getTypeDeclaration() != null) {
                    names.add(ctType.getTypeDeclaration().getQualifiedName());
                }
                names.addAll(getGenerics(ctType));
            }
            return names;
        }
    }
}
