package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model;

import java.io.Serializable;
import java.nio.file.Path;

public class Tree implements Serializable {

    private Class cls;
    private String path;

    public Tree(Class cls, Path path) {
        this.cls = cls;
        this.path = path.toAbsolutePath().toString();
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
