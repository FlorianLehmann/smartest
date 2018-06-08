package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import fr.smartest.plugin.Language;
import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.diff.DiffClassAST;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse.diff.DiffClassDepAST;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaLanguageAST implements Language {

    private static final Logger logger = LogManager.getLogger(JavaLanguageAST.class);

    private List<ModuleImpl> modules;

    private DatabaseAST database;

    public JavaLanguageAST() {
        database = DatabaseAST.getInstance();
        modules = new ArrayList<>();
    }

    @Override
    public void setUp(List<Module> modules) {
        modules.forEach(module -> this.modules.add(new ModuleImpl(module)));

        if (!database.checkExistence()) {
            for (ModuleImpl module : this.modules) {
                try {
                    buildAst(module);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                database.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                database.loadASTs();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void buildAst(ModuleImpl module) throws IOException {
        database.addAst(module, new ASTFactory(module).build());
    }

    @Override
    public Set<Test> getTestsRelatedToChanges(String scope, Set<fr.smartest.plugin.Diff> fileDiff) {
        try {
            if (scope.equalsIgnoreCase("class"))
                return new DiffClassAST().getTestRelatedTochanges(this.modules);
            else
                return new DiffClassDepAST().getTestRelatedTochanges(this.modules);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    @Override
    public void save() {
        try {
            database.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean accept(String s) {
        return s.toLowerCase().equals("javaast");
    }

}
