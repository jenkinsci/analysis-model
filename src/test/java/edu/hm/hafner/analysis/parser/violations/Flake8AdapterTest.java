package edu.hm.hafner.analysis.parser.violations;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link Flake8Adapter}.
 *
 * @author Ullrich Hafner
 */
class Flake8AdapterTest extends AbstractParserTest {
    Flake8AdapterTest() {
        super("flake8.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(12);
        softly.assertThat(report.get(3))
                .hasMessage("'db' imported but unused")
                .hasFileName("myproject/__init__.py")
                .hasType("F401")
                .hasLineStart(7)
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected Flake8Adapter createParser() {
        return new Flake8Adapter();
    }

    /**
     * Checks whether columns are supported.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-53786">Issue 53786</a>
     */
    @Test
    void shouldParseFileWithColumns() {
        var report = parse("flake8-issue53786");

        assertThat(report).hasSize(9);
        assertThat(report.get(8)).hasFileName("../devopsloft/application.py")
                .hasLineStart(42)
                .hasColumnStart(1)
                .hasType("E302")
                .hasMessage("expected 2 blank lines, found 1");
    }

    @Test
    void shouldParseWithCurrentDirPrefix() {
        var report = parse("flake8.log");

        assertThat(report).hasSize(2);
        try (var softly = new SoftAssertions()) {
            softly.assertThat(report.get(0)).hasFileName("./src/init.py")
                    .hasLineStart(254)
                    .hasColumnStart(58)
                    .hasType("W292")
                    .hasMessage("no newline at end of file");
            softly.assertThat(report.get(1)).hasFileName("./src/init.py")
                    .hasLineStart(66)
                    .hasColumnStart(121)
                    .hasType("E501")
                    .hasMessage("line too long (143 > 120 characters)");
        }
    }
}
