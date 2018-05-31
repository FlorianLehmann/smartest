package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers;

import java.io.File;
import java.util.List;

public interface Explorer {

    List<String> findAllModules(File baseDir);

    String getPathToSrc(String currentModule);

    String getPathToTest(String currentModule);
}
