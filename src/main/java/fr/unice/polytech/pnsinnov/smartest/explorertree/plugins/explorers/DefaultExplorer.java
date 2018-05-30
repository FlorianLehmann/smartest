package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.explorers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DefaultExplorer implements Explorer {
    @Override
    public List<String> findAllModules(File baseDir) {
        List<String> res = new ArrayList<>();

        res.add(baseDir.getAbsolutePath());

        return res;
    }
}
