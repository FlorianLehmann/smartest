package fr.unice.polytech.pnsinnov.smartest.plugin.language;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ASTFactoryTest {
    @Test
    public void defineContext() throws IOException {
        DirectoryExplorer directoryExplorer = new DirectoryExplorer();
        List<File> javaFiles = directoryExplorer.explore("/home/florian/smartest/src/main/java");
        List<Path> javaFilesPath = new ArrayList<>();
        javaFiles.forEach(filepath -> javaFilesPath.add(filepath.toPath()));
        /*for (File file :
                javaFiles) {
            System.out.println(file.getAbsolutePath());
        }*/
        //ASTFactory astFactory = new ASTFactory();
        //astFactory.generateAST(javaFiles);

        //CompilationUnit compilationUnit = JavaParser.parse(Paths.get("/home/florian/smartest/src/main/java/fr/unice/polytech/pnsinnov/smartest/Main.java"));
        //new MethodVisitor().visit(compilationUnit, null);

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolverFiles(javaFilesPath));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
        CompilationUnit cu = JavaParser.parse(Paths.get("/home/florian/smartest/src/main/java/fr/unice/polytech/pnsinnov/smartest/Main.java"));
        cu.findAll(VariableDeclarationExpr.class).forEach(ae -> { ResolvedType resolvedType = ae.calculateResolvedType();
        System.out.println(ae.toString() + "is " + resolvedType);});
    }

//    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
//
//        @Override
//        public void visit(MethodDeclaration n, Void arg) {
//            CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
//            combinedTypeSolver.add(new ReflectionTypeSolver());
//            //combinedTypeSolver.
//        /*for (File file : files) {
//            combinedTypeSolver.add(new JavaParserTypeSolver(file));
//        }
//        combinedTypeSolver.add(new JavaParser());*/
//            combinedTypeSolver.add(new JavaPP("src/main/java"));
//            ResolvedType type = JavaParserFacade.get(combinedTypeSolver).getType(n);
//            //System.out.println("TYPE : " + type.describe());
//        }
//
//    }
}
