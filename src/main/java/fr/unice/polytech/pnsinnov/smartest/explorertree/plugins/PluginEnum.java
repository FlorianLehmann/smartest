package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins;

public enum PluginEnum {

    MAVEN {
        public Plugin getAssociatedPlugin(){
            return new MvnPlugin();
        }
    };

    public abstract Plugin getAssociatedPlugin();

}
