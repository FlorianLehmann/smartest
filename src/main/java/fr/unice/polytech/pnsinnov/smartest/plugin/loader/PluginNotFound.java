package fr.unice.polytech.pnsinnov.smartest.plugin.loader;

import fr.smartest.exceptions.PluginException;

public class PluginNotFound extends PluginException {
    public PluginNotFound(String identifier) {
        super("Plugin not found : " + identifier);
    }
}
