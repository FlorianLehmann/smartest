package fr.unice.polytech.pnsinnov.smartest.plugin.loader;

import com.jcraft.jsch.JSchException;
import fr.smartest.exceptions.PluginException;
import fr.unice.polytech.pnsinnov.smartest.configuration.Configuration;
import fr.unice.polytech.pnsinnov.smartest.configuration.ConfigurationHolder;
import fr.unice.polytech.pnsinnov.smartest.plugin.production.PathPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.*;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoadJarTest {
    private File directory;
    private String jar = "plugin-example-jar-with-dependencies.jar";
    private String git = "https://mjollnir.unice.fr/bitbucket/scm/pnse/plugin-example.git";
    private Path target;

    @BeforeAll
    public void cloneGit() throws IOException, GitAPIException, JSchException, InterruptedException,
            MavenInvocationException, URISyntaxException {
        directory = new File("src/test/resources/plugin-example");
        if (!directory.mkdir()) {
            FileUtils.deleteDirectory(directory);
            FileUtils.forceMkdir(directory);
        }

        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(git)
                .setDirectory(directory);
        Git call = cloneCommand.call();
        call.close();
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(directory.getAbsolutePath(), PathPlugin.POM_FILE.getName()));
        request.setGoals(Arrays.asList("clean", "package"));
        request.setOutputHandler(s -> {
        });
        request.setBatchMode(true);
        Invoker invoker = new DefaultInvoker();
        invoker.execute(request);
        target = Paths.get(directory.getAbsolutePath(), "target");
        FileUtils.forceDelete(new File(target.resolve("plugin-example-1.0-SNAPSHOT.jar").toUri()));
    }

    @Test
    void loadAllFromExampleJar() throws PluginException {
        Configuration configuration = null;
        configuration = new ConfigurationHolder(null, target, null, "Python", "Pypi", "Pytest", "svn");
        PluginLoader pluginLoader = new PluginLoader(configuration);
        assertTrue(pluginLoader.language().accept("Python"));
        assertTrue(pluginLoader.productionTool().accept("Pypi"));
        assertTrue(pluginLoader.testFramework().accept("Pytest"));
        assertTrue(pluginLoader.vcs().accept("svn"));
        assertFalse(pluginLoader.language().accept("Java"));
        assertFalse(pluginLoader.productionTool().accept("Maven"));
        assertFalse(pluginLoader.testFramework().accept("Junit5"));
        assertFalse(pluginLoader.vcs().accept("git"));
    }

    @AfterAll
    public void deleteAll() throws IOException {
        FileUtils.deleteDirectory(directory);
    }
}
