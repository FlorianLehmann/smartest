package fr.unice.polytech.pnsinnov.smartest.plugin.testframework;

import fr.smartest.plugin.Test;

public class JunitTest implements Test {
    private final Priority priority;
    private final String identifier;

    public JunitTest(Priority priority, String identifier) {
        this.priority = priority;
        this.identifier = identifier;
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
    public String toString() {
        return "JunitTest{priority=" + priority + ", identifier='" + identifier + "\'}";
    }
}
