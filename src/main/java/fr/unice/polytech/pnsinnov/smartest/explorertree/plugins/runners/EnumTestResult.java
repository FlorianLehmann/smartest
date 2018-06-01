package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;

public enum EnumTestResult {
    SUCCESSFUL("SUCCESSFUL"),
    FAILED("FAILED");

    private String result;

    EnumTestResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return result;
    }

}
