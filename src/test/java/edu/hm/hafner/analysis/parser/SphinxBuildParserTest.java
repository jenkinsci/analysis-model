package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link SphinxBuildParser}.
 */
class SphinxBuildParserTest extends AbstractParserTest {
    SphinxBuildParserTest() {
        super("sphinxbuild.txt");
    }

    private static final String SPHINX_BUILD_ERROR = "ERROR";
    private static final String SPHINX_BUILD_WARNING = "WARNING";

    /**
     * Parses a file that contains an optional text between path and line number.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-60033">Issue 60033</a>
     */
    @Test
    void issue60033() {
        Report warnings = parse("issue60033.txt");

        assertThat(warnings).hasSize(7);
        assertThat(warnings.getFiles()).containsExactly("C:/path/to/prj/foo/legacy.py");
    }

    @Test
    void issue63216() {
        Report warnings = parse("issue63216.txt");

        assertThat(warnings).hasSize(1);
        assertThat(warnings).hasOnlyAbsolutePaths("/src/be/doc/_sub/_classTest/05_test.rst");
        assertThat(warnings.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("document isn't included in any toctree")
                .hasFileName("/src/be/doc/_sub/_classTest/05_test.rst");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(7);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(SPHINX_BUILD_ERROR)
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("Unknown directive type \"blockdiag\".")
                .hasFileName("/src/be/doc/_sub/00_outline.rst");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("unknown document: ../builder/builder")
                .hasFileName("/src/infrastructure/jenkins-obs/jenkins-obs.rst");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(26)
                .hasLineEnd(26)
                .hasMessage(
                        "undefined label: building_documentation_with_the_sphinx_container (if the link has no caption the label must precede a section header)")
                .hasFileName("/src/infrastructure/sphinx/plantuml.rst");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("Could not obtain image size. :scale: option is ignored.")
                .hasFileName("/src/feblock/doc/3_3_6_2_WriteCommandCallbackIF.rst");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("document isn't included in any toctree")
                .hasFileName("/src/infrastructure/self-test/self-test-vault/src/self-test/index.rst");
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory(SPHINX_BUILD_ERROR)
                .hasLineStart(21)
                .hasLineEnd(21)
                .hasMessage("Unknown target name: \"threadid\".")
                .hasFileName("/src/be/doc/_sub/_classThread/04_Interface.rst");
        softly.assertThat(report.get(6))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(26)
                .hasLineEnd(26)
                .hasMessage("py:mod reference target not found: bar.foo.postprocessing")
                .hasFileName("legacy.py");
    }

    @Override
    protected SphinxBuildParser createParser() {
        return new SphinxBuildParser();
    }
}

