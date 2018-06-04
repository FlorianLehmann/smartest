package fr.unice.polytech.pnsinnov.smartest.plugin.production;

public enum PathPlugin {

    DEFAULT_SRC("src/main/java"),
    DEFAULT_TST("src/test/java"),
    POM_FILE("/pom.xml"),
    SRC_DIRECTORY("/src"),
    DEFAULT_SRC_OUTPUT("target/classes"),
    DEFAULT_TEST_OUTPUT("target/test-classes");

    private String name;

    PathPlugin(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
