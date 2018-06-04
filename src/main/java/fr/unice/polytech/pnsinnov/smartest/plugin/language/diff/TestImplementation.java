package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

public class TestImplementation implements fr.smartest.plugin.Test {


    private Priority priority;
    private String Identifier;

    public TestImplementation(String identifier) {
        this.priority = Priority.MEDIUM;
        Identifier = identifier;
    }

    @Override
    public Priority getPriority() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return null;
    }

}
