package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link SphinxBuildParser}.
 */
public class SphinxBuildParserTest extends AbstractParserTest {
    SphinxBuildParserTest() {
        super("sphinxbuild.txt");
    }

    private static final String SPHINX_BUILD_ERROR = "ERROR";
    private static final String SPHINX_BUILD_WARNING = "WARNING";

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(6);

        softly.assertThat(issues.get(0))
                .hasPriority(Priority.HIGH)
                .hasCategory(SPHINX_BUILD_ERROR)
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("Unknown directive type \"blockdiag\".")
                .hasFileName("/src/be/doc/_sub/00_outline.rst");
        softly.assertThat(issues.get(1))
                .hasPriority(Priority.NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("unknown document: ../builder/builder")
                .hasFileName("/src/infrastructure/jenkins-obs/jenkins-obs.rst");
        softly.assertThat(issues.get(2))
                .hasPriority(Priority.NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(26)
                .hasLineEnd(26)
                .hasMessage(
                        "undefined label: building_documentation_with_the_sphinx_container (if the link has no caption the label must precede a section header)")
                .hasFileName("/src/infrastructure/sphinx/plantuml.rst");
        softly.assertThat(issues.get(3))
                .hasPriority(Priority.NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("Could not obtain image size. :scale: option is ignored.")
                .hasFileName("/src/feblock/doc/3_3_6_2_WriteCommandCallbackIF.rst");
        softly.assertThat(issues.get(4))
                .hasPriority(Priority.NORMAL)
                .hasCategory(SPHINX_BUILD_WARNING)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("document isn't included in any toctree")
                .hasFileName("/src/infrastructure/self-test/self-test-vault/src/self-test/index.rst");
        softly.assertThat(issues.get(5))
                .hasPriority(Priority.HIGH)
                .hasCategory(SPHINX_BUILD_ERROR)
                .hasLineStart(21)
                .hasLineEnd(21)
                .hasMessage("Unknown target name: \"threadid\".")
                .hasFileName("/src/be/doc/_sub/_classThread/04_Interface.rst");
    }

    @Override
    protected AbstractParser createParser() {
        return new SphinxBuildParser();
    }
}

