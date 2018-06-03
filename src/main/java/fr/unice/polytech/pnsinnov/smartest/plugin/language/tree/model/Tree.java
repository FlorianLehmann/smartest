package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model;

import java.io.Serializable;

public class Tree implements Serializable {

    private Class cls;

    public Tree(Class cls) {
        this.cls = cls;
    }

    public Class getCls() {
        return cls;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("> Class : ").append(cls.getName()).append('\n');
        for (Dependency dependency : cls.getDependencies()) {
            str.append("    > Dependency : ").append(dependency.getName()).append('\n');
        }
        for (Method method : cls.getMethods()) {
            str.append("    > Method : ").append(method.getName()).append('\n');
            for (Dependency dependency : method.getDependencies()) {
                str.append("        > Dependency : ").append(dependency.getName()).append('\n');
            }
        }
        return str.toString();
    }
}
