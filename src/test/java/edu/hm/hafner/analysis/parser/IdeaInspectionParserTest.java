package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests {@link IdeaInspectionParser } parser class.
 *
 * @author Alex Lopashev, alexlopashev@gmail.com
 */
class IdeaInspectionParserTest extends AbstractParserTest {
    IdeaInspectionParserTest() {
        super("IdeaInspectionExample.xml");
    }

    @Override
    protected IdeaInspectionParser createParser() {
        return new IdeaInspectionParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Unused method parameters")
                .hasLineStart(42)
                .hasLineEnd(42)
                .hasModuleName("tests")
                .hasMessage(
                        "Parameter <code>intentionallyUnusedString</code> is not used  in either this method or any of its derived methods")
                .hasFileName("$PROJECT_DIR$/src/main/java/org/lopashev/Test.java");
    }

    /**
     * Parses a warning log with IDEA warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56235">Issue 56235</a>
     */
    @Test
    void issue56235() {
        Report warnings = parse("issue56235.xml");
        assertThat(warnings).hasSize(6);

        Iterator<Issue> iterator = warnings.iterator();

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Optional.isPresent() can be replaced with functional-style expression")
                    .hasLineStart(66)
                    .hasLineEnd(66)
                    .hasMessage("Can be replaced with single expression in functional style")
                    .hasFileName("$PROJECT_DIR$/src/main/java/edu/hm/hafner/analysis/RegexpParser.java");
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasCategory("Typo")
                    .hasLineStart(9)
                    .hasLineEnd(9)
                    .hasMessage("Typo: In word 'Dsurefire'")
                    .hasFileName("$PROJECT_DIR$/src/test/java/edu/hm/hafner/Example.jenkinsfile");
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Redundant suppression")
                    .hasLineStart(40)
                    .hasLineEnd(40)
                    .hasMessage("Redundant suppression")
                    .hasFileName("$PROJECT_DIR$/src/test/java/edu/hm/hafner/analysis/assertj/IssueAssert.java");
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Redundant 'throws' clause")
                    .hasLineStart(309)
                    .hasLineEnd(309)
                    .hasMessage(
                            "The declared exception <code>IOException</code> is never thrown in method implementations")
                    .hasFileName("$PROJECT_DIR$/src/main/java/edu/hm/hafner/analysis/ModuleDetector.java");
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasCategory("@NotNull/@Nullable problems")
                    .hasLineStart(53)
                    .hasLineEnd(53)
                    .hasMessage(
                            "Not 'edu.umd.cs.findbugs.annotations.Nullable' but 'org.jetbrains.annotations.Nullable' would be used for code generation.")
                    .hasFileName("$PROJECT_DIR$/src/main/java/edu/hm/hafner/analysis/IssueBuilder.java");
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Constant conditions & exceptions")
                    .hasLineStart(195)
                    .hasLineEnd(195)
                    .hasMessage(
                            "Method invocation <code>getCodeFragment</code> may produce <code>NullPointerException</code>")
                    .hasFileName(
                            "$PROJECT_DIR$/src/test/java/edu/hm/hafner/analysis/parser/dry/cpd/CpdParserTest.java");
        }
    }
}

