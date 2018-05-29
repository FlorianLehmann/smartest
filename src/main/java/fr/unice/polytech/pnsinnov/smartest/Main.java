package fr.unice.polytech.pnsinnov.smartest;

import fr.unice.polytech.pnsinnov.smartest.commandline.Smartest;

public class Main {
    public static void main(String[] args) {

        /*
        ConfigReader reader = new ConfigReader("./resources/config.smt");

        List<Plugin> plugins = new PluginTechRetriever().retrieveAllPlugins();

        Plugin toUse = new DefaultPlugin();

        for (Plugin plugin :
             plugins) {
            if(plugin.accept(reader.getLangageFromConfig(), reader.getTestFrameworkFromConfig(), reader.getManagementFromConfig())){
                toUse = plugin;
                break;
            }
        }

        TreeModule module = new TreeModule.TreeModuleBuilder().withProjectBase("C:\\Users\\Enzo\\Desktop\\Nouveau dossier\\main").withPlugin(toUse).build();

         */

        new Smartest(System.out).parse(args);
    }
}
