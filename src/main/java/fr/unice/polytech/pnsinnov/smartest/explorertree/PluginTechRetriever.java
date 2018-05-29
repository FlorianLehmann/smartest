package fr.unice.polytech.pnsinnov.smartest.explorertree;

import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.Plugin;
import fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.PluginEnum;

import java.util.ArrayList;
import java.util.List;

public class PluginTechRetriever {

    public List<Plugin> retrieveAllPlugins(){
        List<Plugin> plugins = retrieveLocalPlugins();

        plugins.addAll(retrieveExternalPlugins());

        return plugins;
    }

    public List<Plugin> retrieveLocalPlugins(){
        List<Plugin> plugins = new ArrayList<>();

        for (PluginEnum tech :
                PluginEnum.values()) {
            plugins.add(tech.getAssociatedPlugin());
        }

        return plugins;
    }

    public List<Plugin> retrieveExternalPlugins() {
        List<Plugin> plugins = new ArrayList<>();

        //TODO We could even add strategies from exterior through dynamic class loading

        return plugins;
    }
}
