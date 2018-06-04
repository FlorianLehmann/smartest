package fr.unice.polytech.pnsinnov.smartest.plugin.production;

public enum PathPlugin {

    DEFAULT_SRC("src/main/java"),
    DEFAULT_TST("src/test/java"),
    POM_FILE("/pom.xml"),
    SRC_DIRECTORY("/src");

    private String name;

    PathPlugin(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
