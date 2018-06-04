package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassTest {

    private Class cls;
    private Set<Dependency> dependencies;

    @BeforeEach
    public void defineContext() {
        dependencies = new HashSet<>();
        dependencies.add(new Dependency("fr.unice.Foo"));
        dependencies.add(new Dependency("fr.unice.Bar"));
        List<Method> methods = new ArrayList<>();
        Method method1 = new Method("sqrt");
        method1.getDependencies().add(new Dependency("fr.unice.Math"));
        Method method2 = new Method("inc");
        methods.add(method1);
        methods.add(method2);
        cls = new Class("testClass", methods, dependencies);
    }


    @Test
    public void shouldNotFindAnyDependencies() {
        assertEquals(0, new Class("Class", new ArrayList<>(), new HashSet<>()).getAllDependencies().size());
    }

    @Test
    public void shouldFindAllDependenciesLinkToTheClass() {
        assertEquals("testClass", cls.getName());
        assertEquals(3, cls.getAllDependencies().size());
        assertTrue(cls.getAllDependencies().contains(new Dependency("fr.unice.Foo")));
        assertTrue(cls.getAllDependencies().contains(new Dependency("fr.unice.Bar")));
        assertTrue(cls.getAllDependencies().contains(new Dependency("fr.unice.Math")));
    }

    @Test
    public void shouldFindFieldDependency() {
        assertEquals("testClass", cls.getName());
        assertEquals(2, cls.getDependencies().size());
        assertTrue(cls.getDependencies().contains(new Dependency("fr.unice.Foo")));
        assertTrue(cls.getDependencies().contains(new Dependency("fr.unice.Bar")));
    }

}