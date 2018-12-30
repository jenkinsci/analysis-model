package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

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
                .hasMessage("The method compare(List<String>, List<String>) from the type PmModelImporter is never used locally")
                .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(391)
                .hasLineEnd(391)
                .hasMessage("The method getTableValues(PropertyRestrictionType) from the type PmModelImporter is never used locally")
                .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core/src/com/faktorzehn/pa2msgpm/core/loader/PmModelImporter.java");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(56)
                .hasLineEnd(56)
                .hasMessage("The value of the field PropertyImporterTest.ERROR_RESPONSE is not used")
                .hasFileName("/media/ssd/multi-x-processor/workspace/msgPM_Access/com.faktorzehn.pa2msgpm.core.test/src/com/faktorzehn/pa2msgpm/core/importer/PropertyImporterTest.java");
    }

     /**
     * Parses a warning log with previously undetected warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-21377">Issue 21377</a>
     */
    @Test
    void issue21377() {
        Report warnings = parse("issue21377.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(13)
                    .hasLineEnd(13)
                    .hasColumnStart(11)
                    .hasColumnEnd(11 + 12)
                    .hasMessage("The method getOldValue() from the type SomeType is deprecated")
                    .hasFileName("/path/to/job/job-name/module/src/main/java/com/example/Example.java");
        });
    }

    @Test
    void shouldOnlyAcceptTextFiles() {
        EclipseMavenParser parser = createParser();

        assertThat(parser.accepts(createReaderFactory("eclipse-columns.txt"))).isTrue();
        assertThat(parser.accepts(createReaderFactory("eclipse-withinfo.txt"))).isTrue();

        assertThat(parser.accepts(createReaderFactory("eclipse-columns.xml"))).isFalse();
        assertThat(parser.accepts(createReaderFactory("eclipse-withinfo.xml"))).isFalse();
    }
}

