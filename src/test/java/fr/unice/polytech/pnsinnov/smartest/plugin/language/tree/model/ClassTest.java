package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model;

import fr.smartest.exceptions.LanguageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest {

    private Class cls;

    @BeforeEach
    public void defineContext() {
        Set<Dependency> dependencies = new HashSet<>();
        dependencies.add(new Dependency("fr.unice.Foo"));
        dependencies.add(new Dependency("fr.unice.Bar"));
        List<Method> methods = new ArrayList<>();
        Method method1 = new Method("sqrt");
        method1.getDependencies().add(new Dependency("fr.unice.Math"));
        Method method2 = new Method("inc");
        cls = new Class("testClass", methods, dependencies);
    }


    @Test
    public void shouldNotFindAnyDependencies() {
        assertEquals(0, new Class("Class", new ArrayList<>(), new HashSet<>()).getAllDependencies().size());
    }

    @Test
    public void shouldFindTwoDependencies() {
        assertEquals("testClass", cls.getName());
        assertEquals(0, new Class("Class", new ArrayList<>(), new HashSet<>()).getAllDependencies().size());
    }

}