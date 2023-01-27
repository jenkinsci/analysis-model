package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Tests the class {@link EmbeddedEngineerParser}.
 *
 *  @author Eva Habeeb
 */
class EmbeddedEngineerParserTest extends AbstractParserTest {
    protected EmbeddedEngineerParserTest() {
        super("ea.log");
    }

    @Override
    protected EmbeddedEngineerParser createParser() {
        return new EmbeddedEngineerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);
        softly.assertThat(report.get(0))
                .hasModuleName("index_module")
                .hasDescription("Complex type definition without referenced element found 'index_module' (uint8_t) ({98CF1FE6-EC9C-43f1-e476-40EFCD63cA8D})")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Complex type definition without referenced element")
                .hasFileName("'01_first_sw' ({F3878EC2-1234-92713-8710-03561AD6E297})");
        softly.assertThat(report.get(3))
                .hasCategory("Code generation skipped")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(4))
                .hasCategory("Code generation failed")
                .hasSeverity(Severity.WARNING_HIGH);

    }
}

