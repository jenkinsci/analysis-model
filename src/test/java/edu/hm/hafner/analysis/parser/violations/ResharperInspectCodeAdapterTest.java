package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link ResharperInspectCodeAdapter}.
 */
class ResharperInspectCodeAdapterTest extends AbstractParserTest {
    private static final String ISSUES_FILE = "ResharperInspectCode.xml";

    /**
     * Creates a new instance of {@link ResharperInspectCodeAdapterTest}.
     */
    ResharperInspectCodeAdapterTest() {
        super(ISSUES_FILE);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasLineStart(41)
                .hasLineEnd(41)
                .hasMessage("Convert to auto-property. Language Usage Opportunities. Convert property to auto-property")
                .hasFileName("ResharperDemo/Program.cs")
                .hasType("ConvertToAutoProperty")
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(1))
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage(
                        "Expression is always true. Redundancies in Code. Expression is always 'true' or always 'false'")
                .hasFileName("ResharperDemo/Program.cs")
                .hasType("ConditionIsAlwaysTrueOrFalse")
                .hasSeverity(Severity.WARNING_NORMAL);
        softly.assertThat(report.get(2))
                .hasLineStart(16)
                .hasLineEnd(16)
                .hasMessage("Cannot resolve symbol 'GetError'. C# Compiler Errors. CSharpErrors")
                .hasFileName("ResharperDemo/Program.cs")
                .hasType("CSharpErrors")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Override
    protected ResharperInspectCodeAdapter createParser() {
        return new ResharperInspectCodeAdapter();
    }
}

