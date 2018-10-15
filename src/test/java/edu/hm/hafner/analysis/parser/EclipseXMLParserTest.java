package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests for {@link TaglistParser}.
 * 
 * @author Jason Faust
 */
class EclipseXMLParserTest extends AbstractIssueParserTest {

    EclipseXMLParserTest() {
        super("eclipse-withinfo.xml");
    }

    @Override
    protected IssueParser createParser() {
        return new EclipseXMLParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(6);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasColumnStart(13)
                .hasColumnEnd(17)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("Type mismatch: cannot convert from float to Integer");

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(16)
                .hasLineEnd(16)
                .hasColumnStart(8)
                .hasColumnEnd(41)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("Dead code");

        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(22)
                .hasLineEnd(22)
                .hasColumnStart(9)
                .hasColumnEnd(10)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("The value of the local variable x is not used");

        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(27)
                .hasLineEnd(27)
                .hasColumnStart(8)
                .hasColumnEnd(41)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage(
                        "Statement unnecessarily nested within else clause. The corresponding then clause does not complete normally");

        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(33)
                .hasLineEnd(33)
                .hasColumnStart(13)
                .hasColumnEnd(19)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("Comparing identical expressions");

        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasColumnStart(1)
                .hasColumnEnd(96)
                .hasFileName("C:/devenv/workspace/x/y/src/main/java/y/ECE.java")
                .hasMessage("The allocated object is never used");
    }

}
