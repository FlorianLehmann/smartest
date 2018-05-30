package fr.unice.polytech.pnsinnov.smartest.commandline.command;

import fr.unice.polytech.pnsinnov.smartest.commandline.Command;
import fr.unice.polytech.pnsinnov.smartest.parser.Parser;
import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import picocli.CommandLine;
import spoon.reflect.declaration.CtClass;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit extends Command {
    @CommandLine.Option(names = {"-m", "--message"}, required = true, description = "Use the given <msg> as the " +
            "commit message. If multiple -m options are given, their values are concatenated as separate paragraphs.")

    private String message;

    public void run() {
        Parser parser = new Parser(context.getProjectTree().getPathToSrc(), context.getProjectTree().getPathToTest());

        parser.sourceCodeParsing();
        parser.testsParsing();

        Set<String> toRun = new HashSet<>();

        Git git = null;

        try {
            git = Git.open(new File(Paths.get("").toAbsolutePath().toString(), ".git"));

            Repository repository = git.getRepository();

            ObjectId lastCommitId = repository.resolve(Constants.HEAD);

            RevWalk revWalk = new RevWalk(repository);
            RevCommit commit = revWalk.parseCommit(lastCommitId);

            for (String path : git.status().call().getUncommittedChanges()) {
                if(path.endsWith(".java")) {
                    toRun.addAll(compareChanges(path, getContent(git, commit, path)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } finally {
            if (git != null){
                git.close();
            }
        }

        if(context.getProjectTree().runAllTests(toRun)){
            this.commitAllChanges();
            context.out().println("Les tests sont passés, le code a été commit");
        } else {
            context.out().println("Les tests ont échoués");
        }

    }

    private Set<String> compareChanges(String path, String previous) {
        Set<String> toRun = new HashSet<>();

        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            return toRun;
        }

        String current = new String(encoded);

        Diff result = new AstComparator().compare(previous, current);

        result.getAllOperations().forEach(operation -> {
            try {
                toRun.add(operation.getSrcNode().getParent(CtClass.class).getQualifiedName());
            } catch (NullPointerException ignored){
                //Non-CtClass object detected, skipped (@interface, enum....)
            }
        });

        return toRun;
    }

    private String getContent(Git git, RevCommit commit, String path) throws IOException {
        try (TreeWalk treeWalk = TreeWalk.forPath(git.getRepository(), path, commit.getTree())) {
            ObjectId blobId = treeWalk.getObjectId(0);
            try (ObjectReader objectReader = git.getRepository().newObjectReader()) {
                ObjectLoader objectLoader = objectReader.open(blobId);
                byte[] bytes = objectLoader.getBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (NullPointerException e){
            return "public class " + path.split("/")[path.split("/").length - 1] + "{}";
        }
    }

    private void commitAllChanges() {
        Git git = null;

        try {
            git = Git.open(new File(Paths.get("").toAbsolutePath().toString(), ".git"));
            AddCommand add = git.add();

            for (String path : git.status().call().getUncommittedChanges()) {
                add.addFilepattern(path);
            }

            add.call();
            git.commit().setMessage(message).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            context.out().println("The \".git\" was not found in this folder");
        } finally {
            if (git != null){
                git.close();
            }
        }
    }
}
