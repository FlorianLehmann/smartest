package fr.unice.polytech.pnsinnov.smartest.configuration;


public enum ConfigKey {

    VCS_PATH("vcs_path", ".git"),
    PLUGIN_PATH("plugin_path", "plugins"),
    PROJECT_PATH("project_path", ""),
    LANGUAGE("language", "java"),
    PRODUCTION_TOOL("production_tool", "maven"),
    TEST_FRAMEWORK("test_framework", "junit5"),
    VCS("vcs", "git");

    private String configName;

    private String defaultValue;

    ConfigKey(String configName, String defaultValue){
        this.defaultValue = defaultValue;
        this.configName = configName;
    }

    public String getConfigName() {
        return configName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
