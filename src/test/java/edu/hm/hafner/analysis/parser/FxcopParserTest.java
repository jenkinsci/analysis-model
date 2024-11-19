package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.parser.fxcop.FxCopParser;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link FxCopParser}.
 *
 * @author Ullrich Hafner
 */
class FxcopParserTest extends AbstractParserTest {
    FxcopParserTest() {
        super("fxcop.xml");
    }

    @Override
    protected FxCopParser createParser() {
        return new FxCopParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        softly.assertThat(report.get(0)).hasSeverity(Severity.ERROR)
                .hasCategory("Microsoft.Globalization")
                .hasLineStart(299)
                .hasLineEnd(299)
                .hasMessage(
                        "<a href=\"http://msdn2.microsoft.com/library/ms182190(VS.90).aspx\">SpecifyIFormatProvider</a> - Because the behavior of 'decimal.ToString(string)' could vary based on the current user's locale settings, replace this call in 'FilmFacadeBase.Price.get()' with a call to 'decimal.ToString(string, IFormatProvider)'. If the result of 'decimal.ToString(string, IFormatProvider)' will be displayed to the user, specify 'CultureInfo.CurrentCulture' as the 'IFormatProvider' parameter. Otherwise, if the result will be stored and accessed by software, such as when it is persisted to disk or to a database, specify 'CultureInfo.InvariantCulture'.")
                .hasFileName("C:/Hudson/data/jobs/job1/workspace/test/Space/TestBase.cs");
        softly.assertThat(report.get(1)).hasSeverity(Severity.ERROR)
                .hasCategory("Microsoft.Naming")
                .hasLineStart(37)
                .hasLineEnd(37)
                .hasMessage(
                        "<a href=\"http://msdn2.microsoft.com/library/bb264474(VS.90).aspx\">CompoundWordsShouldBeCasedCorrectly</a> - In member 'MyControl.InitialParameters(bool)', the discrete term 'javascript' in parameter name 'javascript' should be expressed as a compound word, 'javaScript'.")
                .hasFileName("C:/Hudson/data/jobs/job1/workspace/web/UserControls/MyControl.ascx.cs");
    }

    /**
     * Verifies that the FXCop parser works as expected.
     */
    @Test
    void testJenkins14172() {
        var result = parse("issue14172.xml");

        assertThat(result).hasSize(44);
    }
}
