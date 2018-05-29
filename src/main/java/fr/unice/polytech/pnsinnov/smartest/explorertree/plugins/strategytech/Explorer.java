package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.strategytech;

import java.io.File;
import java.util.List;

public interface Explorer {

    List<String> findAllModules(File baseDir);

}
