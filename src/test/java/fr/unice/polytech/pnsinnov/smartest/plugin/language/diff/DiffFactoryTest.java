package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DiffFactoryTest {

    private DiffFactory diffFactory;

    @BeforeEach
    public void defineContext() {
        diffFactory = new DiffFactory(new HashSet<>());
    }

    @Test
    public void shouldReturnDiffClass() throws InvalidScopeTests {
        assertTrue(diffFactory.build(Scope.CLASS) instanceof DiffClass);
    }

    @Test
    public void shouldReturnDiffMethod() throws InvalidScopeTests {
        assertTrue(diffFactory.build(Scope.METHOD) instanceof DiffMethod);
    }

}