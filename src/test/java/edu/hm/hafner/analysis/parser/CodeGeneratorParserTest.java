package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link CodeGeneratorParser}.
 *
 *  @author Eva Habeeb
 */
class CodeGeneratorParserTest extends AbstractParserTest {
    CodeGeneratorParserTest() {
        super("CodeGenerator.log");
    }

    @Override
    protected CodeGeneratorParser createParser() {
        return new CodeGeneratorParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(8);
        softly.assertThat(report.get(0))
                .hasCategory("Configuration Parameters Unavailable")
                .hasMessage("'A_123 interface' is no longer available in the Configuration Parameters dialog box and will be removed in a future release. Set 'GenerateASAP2' to 'off' by using thecommand line. To generate ASAP2 files, use the <a href=\"matlab:helpview(fullfile(docroot,'rtw','helptargets.map'),'export_asap2_cdf')\">\"Generate Calibration Files\"</a> tool instead.");
        softly.assertThat(report.get(1))
                .hasCategory("Multiword Aliases not Supported by Code Generation");
        softly.assertThat(report.get(2))
                .hasCategory("Unnecessary Data Type Conversion");
        softly.assertThat(report.get(3))
                .hasCategory("Unnecessary Data Type Conversion");
        softly.assertThat(report.get(4))
                .hasCategory("Model Cannot be Closed");
        softly.assertThat(report.get(5))
                .hasCategory("Library Not Found");
        softly.assertThat(report.get(6))
                .hasCategory("Other");
        softly.assertThat(report.get(7))
                .hasCategory("File not found or permission denied");
    }
}
