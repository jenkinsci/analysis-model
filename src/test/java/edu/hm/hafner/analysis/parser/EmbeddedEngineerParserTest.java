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
    EmbeddedEngineerParserTest() {
        super("ea.log");
    }

    @Override
    protected EmbeddedEngineerParser createParser() {
        return new EmbeddedEngineerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(9);
        softly.assertThat(report.get(0))
                .hasModuleName("index_module")
                .hasDescription("Complex type definition without referenced element found 'index_module' (uint8_t); {98CF1FE6-EC9C-43f1-e476-40EFCD63cA8D}")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Complex type definition without referenced element")
                .hasFileName("'01_first_sw'; {EF31393-1234-5678-8710-03561AD6E297}");
        softly.assertThat(report.get(3))
                .hasCategory("Code generation skipped")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(4))
                .hasCategory("Code generation failed")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(5))
                .hasCategory("No Category")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(6))
                .hasCategory("SampleValidation")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(7))
                .hasCategory("Error loading plugins from")
                .hasDescription("Error loading plugins from C:\\file1\\idc\\sample_ext.x64.dll")
                .hasSeverity(Severity.ERROR);
        softly.assertThat(report.get(8))
                .hasCategory("Out parameters")
                .hasDescription("Out parameters 'Model_ptr_2345') are not supported. Please use 'return' or 'inout' parameters; {98CF1FE6-EC9C-43f1-e476-40EFCD63cA8D}")
                .hasSeverity(Severity.WARNING_NORMAL);
    }
}

