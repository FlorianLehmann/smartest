package fr.unice.polytech.pnsinnov.smartest.parser.processors;

import fr.unice.polytech.pnsinnov.smartest.parser.Database;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

public class ProcessorTests extends AbstractProcessor<CtClass> {

    private Database database = Database.getInstance();

    @Override
    public boolean isToBeProcessed(CtClass candidate) {
        return true;
    }

    @Override
    public void process(CtClass candidate) {
        List<CtTypedElement> elements = candidate.getElements(new TypeFilter<>(CtTypedElement.class));


        if (elements!= null) {
            for (CtTypedElement element: elements) {
                if (element.getType()!=null) {

                    String className = element.getType().getQualifiedName();
                    String classNameTest = candidate.getQualifiedName();

                    if (database.contain(className)) {
                        database.linkClassToTest(className, classNameTest);
                    }
                }
            }
        }

    }

}
