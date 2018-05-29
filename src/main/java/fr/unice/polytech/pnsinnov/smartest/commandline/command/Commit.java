package fr.unice.polytech.pnsinnov.smartest.commandline.command;

import fr.unice.polytech.pnsinnov.smartest.commandline.Command;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit extends Command {
    @CommandLine.Option(names = {"-m", "--message"}, required = true, description = "Use the given <msg> as the " +
            "commit message. If multiple -m options are given, their values are concatenated as separate paragraphs.")
    private String message;

    public void run() {
        try {
            Git git = Git.open(new File(Paths.get("").toAbsolutePath().toString(), ".git"));
            AddCommand add = git.add();
            for (String path : git.status().call().getUncommittedChanges()) {
                add.addFilepattern(path);
            }
            add.call();
            git.commit().setMessage(message).call();
            git.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AbortedByHookException e) {
            e.printStackTrace();
        } catch (ConcurrentRefUpdateException e) {
            e.printStackTrace();
        } catch (NoHeadException e) {
            e.printStackTrace();
        } catch (UnmergedPathsException e) {
            e.printStackTrace();
        } catch (NoFilepatternException e) {
            e.printStackTrace();
        } catch (NoMessageException e) {
            e.printStackTrace();
        } catch (WrongRepositoryStateException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}
