package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import fr.smartest.plugin.Language;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaLanguageAST implements Language {

    private static final Logger logger = LogManager.getLogger(JavaLanguageAST.class);

    private List<Module> modules;


    public JavaLanguageAST() {
        modules = new ArrayList<>();
    }

    @Override
    public void setUp(List<Module> modules) {
        this.modules = modules;



    }


    @Override
    public Set<Test> getTestsRelatedToChanges(String scope, Set<fr.smartest.plugin.Diff> fileDiff) {
        Set<Test> tests = new HashSet<>();
        for (Module module : modules) {
            tests.addAll(new ASTModule(module, scope).getTestsRelatedToChanges(scope, fileDiff));
        }
        return tests;
    }

    @Override
    public void save() {

    }

    @Override
    public boolean accept(String s) {
        return s.toLowerCase().equals("javaast");
    }

}
