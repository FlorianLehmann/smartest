package fr.unice.polytech.pnsinnov.smartest.commandline.command;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit implements Runnable {
    public void run() {
        // Run commit process

        try {
            Git git = Git.open(new File(Paths.get("").toAbsolutePath().toString(), ".git"));

            //git.add().addFilepattern("*").call();

            git.commit().setAll(true).setMessage("test the automatic commit").call();
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
