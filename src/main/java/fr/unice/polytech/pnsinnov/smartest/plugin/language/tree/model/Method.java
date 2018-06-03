package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model;

import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence.Database;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Method implements Serializable {

    private String name;
    private Set<Dependency> dependencies;

    public Method(String name) {
        this.name = name;
        this.dependencies = new HashSet<>();
    }

    public Method(String name, Set<Dependency> dependencies) {
        this.name = name;
        this.dependencies = dependencies;
    }

    public String getName() {
        return name;

    }

    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    public void refineDependency() {
        Set<Dependency> toKeep = new HashSet<>();
        for (Dependency dependency : this.dependencies) {
            for (String className : Database.getInstance().getClassName()) {
                if (dependency.getName().contains(className))
                    toKeep.add(dependency);
            }
        }
        this.dependencies = toKeep;
    }
}
