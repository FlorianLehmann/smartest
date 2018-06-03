package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.factory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.factory.solver.JavaParserTypeSolverFiles;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Class;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Dependency;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Method;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.model.Tree;
import fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.persistence.Database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TreeFactory {

    public void generateTrees(List<File> files) throws IOException {

        List<Path> javaFiles = convertFilesToPath(files);
        defineSolver(javaFiles);

        for (Path path : javaFiles) {

            CompilationUnit cu = JavaParser.parse(Paths.get(path.toString()));

            cu.findAll(ClassOrInterfaceDeclaration.class).forEach(ae -> {
                ResolvedReferenceTypeDeclaration resolvedType = ae.resolve();

                Set<Dependency> dependencies = new HashSet<>();
                dependencies.addAll(lookForField(resolvedType));

                List<Method> methods = lookForMethods(resolvedType);

                Class cls = lookForClassName(resolvedType, dependencies, methods);

                lookForDependencyInsideMethod(cu, cls);

                Tree file = new Tree(cls);

                Database.getInstance().addFile(path.toString(), file);

            });

        }

        refineDependency();

    }

    private void refineDependency() {
        for (String test : Database.getInstance().getTree().keySet()) {
            Database.getInstance().getTree().get(test).getCls().refineDependency();
        }

    }

    private Class lookForClassName(ResolvedReferenceTypeDeclaration resolvedType, Set<Dependency> dependencies, List<Method> methods) {
        String className = resolvedType.getQualifiedName();
        Database.getInstance().addClassName(className);
        return new Class(className, methods, dependencies);
    }

    private void lookForDependencyInsideMethod(CompilationUnit cu, Class cls) {
        cu.findAll(Expression.class).forEach(ab -> {
            try {
                for (Method method : cls.getMethods()) {
                    ResolvedType resolvedType1 = ab.calculateResolvedType();
                    if (method.getName().equals(ab.getAncestorOfType(MethodDeclaration.class).get().getName().asString()) && !resolvedType1.isPrimitive() && !resolvedType1.isNull() && !resolvedType1.isVoid()) {
                        method.getDependencies().add(new Dependency(resolvedType1.describe()));
                    }
                }
            } catch (Exception e) {

            }
        });
    }

    private List<Method> lookForMethods(ResolvedReferenceTypeDeclaration resolvedType) {
        List<Method> methods = new ArrayList<>();
        for (ResolvedMethodDeclaration method : resolvedType.getDeclaredMethods()) {
            methods.add(new Method(method.getName()));
        }
        return methods;
    }

    private List<Path> convertFilesToPath(List<File> files) {
        List<Path> paths = new ArrayList<>();
        files.forEach(filepath -> paths.add(filepath.toPath()));
        return paths;
    }

    private void defineSolver(List<Path> paths) {
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolverFiles(paths));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
    }

    private List<Dependency> lookForField(ResolvedReferenceTypeDeclaration resolvedType) {
        List<Dependency> dependencies = new ArrayList<>();
        try {
            for (ResolvedFieldDeclaration field : resolvedType.getDeclaredFields()) {
                dependencies.add(new Dependency(field.getType().describe()));
            }
        } catch (Exception e) {

        }
        return dependencies;
    }

}
