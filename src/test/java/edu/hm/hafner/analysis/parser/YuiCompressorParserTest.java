package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link YuiCompressorParser}.
 *
 * @author Emidio Stani
 */
class YuiCompressorParserTest extends AbstractParserTest {
    YuiCompressorParserTest() {
        super("yui.txt");
    }

    @Override
    protected YuiCompressorParser createParser() {
        return new YuiCompressorParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("Use single 'var' per scope")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("Try to use a single 'var' statement per scope."
                        + " [match){returnstringResult;}for( ---> var  <--- i=0;match&&i<match]")
                .hasFileName("unknown.file");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Duplicate variable")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("The variable replacement has already been declared in the same scope..."
                        + " [replace(variable,replacement);}var  ---> replacement <--- =globalStoredVars[name];if(replacement!=]")
                .hasFileName("unknown.file");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Use eval")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("Using 'eval' is not recommended. Moreover, using 'eval' reduces the level of compression!"
                        + " [function(condition,label){if( ---> eval <--- (condition)){this.doGotolabel(label]")
                .hasFileName("unknown.file");
        //Warning with only one line
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Duplicate function")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("The function myFunction has already been declared in the same scope..."
                        + " []")
                .hasFileName("unknown.file");
    }
}
