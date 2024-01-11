package edu.hm.hafner.analysis.parser.violations;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.util.PathUtil;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static edu.hm.hafner.analysis.parser.violations.JUnitAdapter.*;

/**
 * Tests the class {@link JUnitAdapter}.
 *
 * @author Gyanesha Prajjwal
 */
class JUnitAdapterTest extends AbstractParserTest {
    private static final String ASSIGNMENT = "assignment1.xml";

    JUnitAdapterTest() {
        super("junit.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasFileName("com/example/jenkinstest/ExampleUnitTest.kt")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(0).getMessage())
                .startsWith("failTest4")
                .contains("java.lang.AssertionError");
        softly.assertThat(report.get(1))
                .hasFileName("com/example/jenkinstest/ExampleUnitTest.kt")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(1).getMessage())
                .startsWith("failTest5")
                .contains("java.lang.AssertionError");
        softly.assertThat(report.getCounter(TOTAL_TESTS)).isEqualTo(6);
        softly.assertThat(report.getCounter(PASSED_TESTS)).isEqualTo(4);
        softly.assertThat(report.getCounter(FAILED_TESTS)).isEqualTo(2);
        softly.assertThat(report.getCounter(SKIPPED_TESTS)).isEqualTo(0);
    }

    @Override
    protected JUnitAdapter createParser() {
        return new JUnitAdapter();
    }

    /**
     * Verifies that violations can be parsed from JUnit2.
     */
    @Test
    void shouldParseWithJUnit2() {
        Report report = parse("TEST-org.jenkinsci.plugins.jvctb.perform.JvctbPerformerTest.xml");
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(report.get(0))
                    .hasFileName("org/jenkinsci/plugins/jvctb/perform/JvctbPerformerTest.java")
                    .hasSeverity(Severity.WARNING_HIGH);
            softly.assertThat(report.get(0).getMessage())
                    .startsWith("testThatAll")
                    .contains("nondada");
        }
    }

    /**
     * Verifies that skipped tests will be counted.
     */
    @Test
    void shouldCountSkipped() {
        Report report = parse("junit-skipped.xml");
        assertThat(report).isEmpty();
        assertThat(report.getCounter(TOTAL_TESTS)).isEqualTo(5);
        assertThat(report.getCounter(SKIPPED_TESTS)).isEqualTo(1);
        assertThat(report.getCounter(PASSED_TESTS)).isEqualTo(4);
        assertThat(report.getCounter(FAILED_TESTS)).isEqualTo(0);
    }

    /**
     * Verifies that the report file name will be stored.
     */
    @Test
    void shouldStoreReportFileName() {
        Report report = parse(ASSIGNMENT);
        assertThat(report).hasSize(1);
        assertThat(report.getCounter(TOTAL_TESTS)).isEqualTo(1);
        assertThat(report.getCounter(SKIPPED_TESTS)).isEqualTo(0);
        assertThat(report.getCounter(PASSED_TESTS)).isEqualTo(0);
        assertThat(report.getCounter(FAILED_TESTS)).isEqualTo(1);
        assertThat(report).hasOriginReportFile(new PathUtil().getAbsolutePath(getResourceAsFile(ASSIGNMENT)));
    }

    /**
     * Verifies that report of iOS can be parsed.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-63527">Issue 63527</a>
     */
    @Test
    void issue63527() {
        Report report = parse("report.junit");
        assertThat(report).hasSize(1);
    }

    /**
     * Verifies that report of iOS can be parsed.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-64117">Issue 64117</a>
     */
    @Test
    void issue64117() {
        Report report = parse("issue64117.junit");

        assertThat(report).hasSize(1);
    }

    @Test
    void shouldParseArchitectureTests() {
        Report report = parse("TEST-Aufgabe3Test.xml");

        assertThat(report.getCounter(TOTAL_TESTS)).as("Total tests").isEqualTo(3);
        assertThat(report.getCounter(SKIPPED_TESTS)).as("Skipped tests").isEqualTo(0);
        assertThat(report.getCounter(FAILED_TESTS)).as("Failed tests").isEqualTo(2);
        assertThat(report.getCounter(PASSED_TESTS)).as("Passed tests").isEqualTo(1);
        assertThat(report).hasSize(2);
    }

    @Test
    void shouldParseExceptions() {
        shouldParseExceptions("TEST-edu.hm.hafner.analysis.parser.SonarQubeDiffParserTest.xml");
        shouldParseExceptions("TEST-edu.hm.hafner.analysis.parser.SonarQubeIssuesParserTest.xml");
    }

    private void shouldParseExceptions(final String fileName) {
        Report report = parse(fileName);

        assertThat(report.getCounter(TOTAL_TESTS)).as("Total tests").isEqualTo(5);
        assertThat(report.getCounter(SKIPPED_TESTS)).as("Skipped tests").isEqualTo(0);
        assertThat(report.getCounter(FAILED_TESTS)).as("Failed tests").isEqualTo(1);
        assertThat(report.getCounter(PASSED_TESTS)).as("Passed tests").isEqualTo(4);

        assertThat(report).hasSize(1);
    }
}
