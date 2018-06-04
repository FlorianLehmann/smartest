package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

public class Test implements fr.smartest.plugin.Test {


    private Priority priority;
    private String Identifier;

    public Test(String identifier) {
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
