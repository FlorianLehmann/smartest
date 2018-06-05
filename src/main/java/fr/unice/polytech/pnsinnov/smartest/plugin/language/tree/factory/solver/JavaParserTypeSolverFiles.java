package fr.unice.polytech.pnsinnov.smartest.plugin.language.tree.factory.solver;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.github.javaparser.ParseStart.COMPILATION_UNIT;
import static com.github.javaparser.ParserConfiguration.LanguageLevel.BLEEDING_EDGE;
import static com.github.javaparser.Providers.provider;

public class JavaParserTypeSolverFiles implements TypeSolver {

    private final JavaParser javaParser;
    private final Cache<Path, Optional<CompilationUnit>> parsedFiles = CacheBuilder.newBuilder().softValues().build();
    private final Cache<String, SymbolReference<ResolvedReferenceTypeDeclaration>> foundTypes = CacheBuilder.newBuilder().softValues().build();
    private List<Path> files;
    private TypeSolver parent;

    public JavaParserTypeSolverFiles(List<Path> files, ParserConfiguration parserConfiguration) {
        this.files = files;
        javaParser = new JavaParser(parserConfiguration);
    }

    public JavaParserTypeSolverFiles(List<Path> files) {
        this(files,
                new ParserConfiguration()
                        .setLanguageLevel(BLEEDING_EDGE));
    }

    @Override
    public TypeSolver getParent() {
        return parent;
    }

    @Override
    public void setParent(TypeSolver parent) {
        this.parent = parent;
    }

    private Optional<CompilationUnit> parse(Path srcFile) {
        try {
            return parsedFiles.get(srcFile.toAbsolutePath(), () -> {
                try {
                    if (!Files.exists(srcFile)) {
                        return Optional.empty();
                    }
                    return javaParser.parse(COMPILATION_UNIT, provider(srcFile))
                            .getResult()
                            .map(cu -> cu.setStorage(srcFile));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Issue while parsing while type solving: " + srcFile.toAbsolutePath(), e);
                }
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
        // TODO support enums
        // TODO support interfaces
        try {
            return foundTypes.get(name, () -> {
                SymbolReference<ResolvedReferenceTypeDeclaration> result = tryToSolveTypeUncached(name);
                if (result.isSolved()) {
                    return SymbolReference.solved(result.getCorrespondingDeclaration());
                }
                return result;
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveTypeUncached(String name) {

        List<String> filesPath = new ArrayList<>();
        this.files.forEach(file -> filesPath.add(file.toString()));

        String path = "";
        for (String filepath : filesPath) {
            if (filepath.endsWith(name.replace('.', '/') + ".java")) {
                path = filepath;
                break;
            }
        }

        String[] tmp = name.split("\\.");
        String nameElement = tmp[tmp.length - 1];

        Path srcFile = Paths.get(path);
        {
            Optional<CompilationUnit> compilationUnit = parse(srcFile);
            if (compilationUnit.isPresent()) {
                Optional<com.github.javaparser.ast.body.TypeDeclaration<?>> astTypeDeclaration = Navigator.findType(compilationUnit.get(), nameElement);
                if (astTypeDeclaration.isPresent()) {
                    return SymbolReference.solved(JavaParserFacade.get(this).getTypeDeclaration(astTypeDeclaration.get()));
                }
            }
        }

        return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
    }

}


