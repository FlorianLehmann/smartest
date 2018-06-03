package fr.unice.polytech.pnsinnov.smartest.plugin.language;

import fr.smartest.plugin.Language;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.ProductionTool;
import fr.smartest.plugin.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JavaLangage implements Language {

    private ProductionTool productionTool;
    private List<String> srcDirPaths;
    private List<String> testDirPaths;

    public JavaLangage(ProductionTool productionTool) {
        this.productionTool = productionTool;
        srcDirPaths = new ArrayList<>();
        testDirPaths = new ArrayList<>();
    }

    @Override
    public void setUp(List<Module> modules) {
        for (Module module : productionTool.getModules()) {
            srcDirPaths.add(module.getSrcPath());
            testDirPaths.add(module.getTestPath());
        }
        throw new UnsupportedOperationException("create an AST");
    }

    @Override
    public Set<Test> getTestsRelatedToChanges() {
        return null;
    }

    @Override
    public void update(List<String> list) {

    }

    @Override
    public void save() {

    }

    @Override
    public boolean accept(String s) {
        return false;
    }

}
