package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model;


import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence.Database;

import javax.naming.InvalidNameException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Class implements Serializable {

    private String name;
    private List<Method> methods;
    private Set<Dependency> dependencies;

    public Class(String name, List<Method> methods, Set<Dependency> dependencies) {
        this.name = name;
        this.methods = methods;
        this.dependencies = dependencies;
    }

    public String getName() {
        return name;
    }

    public List<Method> getMethods() {
        return methods;
    }

    /**
     * @return dependency related to fields + methods
     */
    public Set<Dependency> getAllDependencies() {
        Set<Dependency> dependencies = new HashSet<>(this.dependencies);
        for (Method method : methods) {
            dependencies.addAll(method.getDependencies());
        }
        return dependencies;
    }

    /**
     * @return only dependencies related to fields of the class
     */
    public Set<Dependency> getDependencies() {
        return this.dependencies;
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
        methods.forEach(Method::refineDependency);
    }
}
