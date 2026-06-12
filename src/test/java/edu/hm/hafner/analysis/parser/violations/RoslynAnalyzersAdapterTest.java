package edu.hm.hafner.analysis.parser.violations;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link RoslynAnalyzersAdapter}.
 *
 * @author Akash Manna
 */
class RoslynAnalyzersAdapterTest extends AbstractParserTest {
    RoslynAnalyzersAdapterTest() {
        super("../roslyn-analyzers-report.sarif");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);

        softly.assertThat(report.filter(Issue.byType("CA1822")).get(0))
                .hasFileName("src/Program.cs")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasType("CA1822")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage("Mark members as static");

        softly.assertThat(report.filter(Issue.byType("CA2000")).get(0))
                .hasFileName("C:/work/analysis-model/src/Helpers.cs")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasType("CA2000")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("Dispose objects before losing scope");

        softly.assertThat(report.filter(Issue.byType("IDE0051")).get(0))
                .hasFileName("src/Utilities/Formatting.cs")
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasType("IDE0051")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("Remove unused private members");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("roslyn-analyzers");

        assertThat(descriptor.getPattern()).isEqualTo("**/roslyn-analyzers-report.sarif");
        assertThat(descriptor.getHelp()).contains("/errorlog:roslyn-analyzers-report.sarif");
        assertThat(descriptor.getUrl())
                .isEqualTo("https://learn.microsoft.com/dotnet/csharp/language-reference/compiler-options/errors-warnings#errorlog");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }

    @Override
    protected IssueParser createParser() {
        return new RoslynAnalyzersAdapter();
    }
}
