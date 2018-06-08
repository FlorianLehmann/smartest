package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.diff;

import fr.smartest.plugin.Test;

import java.util.Objects;

public class TestImpl implements Test {

    private Priority priority;
    private String identifier;

    public TestImpl(String identifier) {
        this.identifier = identifier;
        this.priority = Priority.MEDIUM;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestImpl test = (TestImpl) o;
        return priority == test.priority &&
                Objects.equals(identifier, test.identifier);
    }

    @Override
    public int hashCode() {

        return Objects.hash(priority, identifier);
    }
}
