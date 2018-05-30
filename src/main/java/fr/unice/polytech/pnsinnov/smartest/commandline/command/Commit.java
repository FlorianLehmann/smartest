package fr.unice.polytech.pnsinnov.smartest.commandline.command;

import fr.unice.polytech.pnsinnov.smartest.commandline.Command;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

@CommandLine.Command(name = "commit", description = "Run tests then record changes to the repository.")
public class Commit extends Command {
    @CommandLine.Option(names = {"-m", "--message"}, required = true, description = "Use the given <msg> as the " +
            "commit message. If multiple -m options are given, their values are concatenated as separate paragraphs.")

    private String message;

    public void run() {

        if(context.getPlugin().getTestRunner().runAllTests(new ArrayList<>())){
            this.commitAllChanges();
        } else {
            context.out().println("Les tests ont échoués");
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (git != null){
                git.close();
            }
        }
    }
}
