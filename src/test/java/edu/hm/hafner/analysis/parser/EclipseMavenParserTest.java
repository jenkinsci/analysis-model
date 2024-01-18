package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link EclipseMavenParser}.
 */
class EclipseMavenParserTest extends AbstractParserTest {
    EclipseMavenParserTest() {
        super("issue13969.txt");
    }

    @Override
    protected EclipseMavenParser createParser() {
        return new EclipseMavenParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(369)
                .hasLineEnd(369)
                .hasMessage(
                        "The method compare(List<String>, List<String>) from the type PmModelImporter is never used locally")
                .hasFileName(
                        "/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java")
                .hasCategory(Categories.OTHER);
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(391)
                .hasLineEnd(391)
                .hasMessage(
                        "The method getTableValues(PropertyRestrictionType) from the type PmModelImporter is never used locally")
                .hasFileName(
                        "/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java")
                .hasCategory(Categories.OTHER);
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(56)
                .hasLineEnd(56)
                .hasMessage("The value of the field PropertyImporterTest.ERROR_RESPONSE is not used")
                .hasFileName(
                        "/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core.test/src/com/faktorzehn/pa2msgpm/core/importer/PropertyImporterTest.java")
                .hasCategory(Categories.OTHER);
    }

    /**
     * Parses a warning log with previously undetected warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-21377">Issue 21377</a>
     */
    @Test
    void shouldNotFindAntIssues() {
        Report warnings = parse("eclipse.txt");

        assertThat(warnings).hasSize(0);
    }

    /**
     * Parses a warning log with previously undetected warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-21377">Issue 21377</a>
     */
    @Test
    void issue21377() {
        Report warnings = parse("issue21377.txt");

        assertThat(warnings).hasSize(1);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(13)
                    .hasLineEnd(13)
                    .hasMessage("The method getOldValue() from the type SomeType is deprecated")
                    .hasFileName("/path/to/job/job-name/module/src/main/java/com/example/Example.java")
                    .hasCategory(Categories.OTHER);
        }
    }

    /**
     * Parses an Eclipse warnings report that contains single line warnings only.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-55368">Issue 55368</a>
     */
    @Test
    void issue55368() {
        Report warnings = parse("issue55368.txt");

        assertThat(warnings).hasSize(3);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(9)
                    .hasMessage("The method isValid(String) from the type X can potentially be declared as static")
                    .hasFileName("/home/piotr/.../X.java")
                    .hasCategory(Categories.OTHER);
            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(54)
                    .hasMessage("The method as10(String) from the type X can potentially be declared as static")
                    .hasFileName("/home/piotr/.../X.java")
                    .hasCategory(Categories.OTHER);
            softly.assertThat(warnings.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(60)
                    .hasMessage("The method as13(String) from the type X can potentially be declared as static")
                    .hasFileName("/home/piotr/.../X.java")
                    .hasCategory(Categories.OTHER);
        }
    }

    /**
     * Tests that warnings are categorized as {@code Code} or {@code JavaDoc}.
     */
    @Test
    void javadocCategory() {
        Report warnings = parse("eclipse-maven-withjavadoc.log");
        EclipseSharedChecks.verifyCategory(warnings);
    }

    @Test
    void shouldOnlyAcceptTextFiles() {
        EclipseMavenParser parser = createParser();

        assertThat(parser.accepts(createReaderFactory("eclipse-withinfo.txt"))).isTrue();
        assertThat(parser.accepts(createReaderFactory("eclipse-withinfo.xml"))).isFalse();
    }
}
