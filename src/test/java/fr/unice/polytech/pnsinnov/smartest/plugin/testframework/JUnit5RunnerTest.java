package fr.unice.polytech.pnsinnov.smartest.plugin.testframework;

import fr.smartest.exceptions.ProductionToolException;
import fr.smartest.exceptions.TestFrameworkException;
import fr.smartest.plugin.TestReport;
import fr.unice.polytech.pnsinnov.smartest.SuperClone;
import fr.unice.polytech.pnsinnov.smartest.plugin.production.MavenProduction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static fr.smartest.plugin.Test.Priority;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JUnit5RunnerTest extends SuperClone {
    private JUnit5Runner jUnit5Runner;
    private MavenProduction mavenProduction;

    @BeforeEach
    void setUp() throws ProductionToolException {
        jUnit5Runner = new JUnit5Runner();
        mavenProduction = new MavenProduction();
        mavenProduction.setUp(directory.toPath());
        mavenProduction.compile();
    }

    @Test
    void findSimpleTestAndRunIt() throws TestFrameworkException {
        Set<fr.smartest.plugin.Test> junitTests = new HashSet<>();
        junitTests.add(new JunitTest(Priority.HIGH, "fr.unice.polytech.pnsinnov.SchoolTest"));
        Set<TestReport> run = jUnit5Runner.run(junitTests, mavenProduction.getModules());
        assertFalse(run.isEmpty());
        assertTrue(run.stream().allMatch(testReport -> testReport.getResult() == TestReport.Status.SUCCESSFUL));
    }
}