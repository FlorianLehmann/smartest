package fr.unice.polytech.pnsinnov.smartest.parser.processors;

import fr.unice.polytech.pnsinnov.smartest.parser.Database;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtTypedElement;

import java.util.ArrayList;
import java.util.HashSet;

public class ProcessorClass extends AbstractProcessor<CtClass> {

    private Database database = Database.getInstance();

    @Override
    public boolean isToBeProcessed(CtClass candidate) {
        return true;
    }

    @Override
    public void process(CtClass candidate) {
        database.addClass(candidate.getQualifiedName());
    }

}
