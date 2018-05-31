package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;

public enum EnumTestResult {
    SUCCESSFUL("SUCCESFUL"),
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
