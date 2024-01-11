package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link StyleCopParser}.
 *
 * @author Sebastian Seidl
 */
class StyleCopParserTest extends AbstractParserTest {
    StyleCopParserTest() {
        super("stylecop.xml");
    }

    @Override
    protected StyleCopParser createParser() {
        return new StyleCopParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);

        Iterator<Issue> iterator = report.iterator();
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("ReadabilityRules")
                .hasLineStart(18)
                .hasLineEnd(18)
                .hasMessage(
                        "The call to components must begin with the 'this.' prefix to indicate that the item is a member of the class.")
                .hasFileName("Form1.Designer.cs");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("ReadabilityRules")
                .hasLineStart(16)
                .hasLineEnd(16)
                .hasMessage(
                        "The call to components must begin with the 'this.' prefix to indicate that the item is a member of the class.")
                .hasFileName("Form1.Designer.cs");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("DocumentationRules")
                .hasLineStart(7)
                .hasLineEnd(7)
                .hasMessage("The class must have a documentation header.")
                .hasFileName("MainClass.cs");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("DocumentationRules")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasMessage("The field must have a documentation header.")
                .hasFileName("MainClass.cs");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("DocumentationRules")
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasMessage("The property must have a documentation header.")
                .hasFileName("MainClass.cs");
    }

    /**
     * Verifies that the StyleCop parser works as expected with a file of 3 warnings.
     */
    @Test
    void testStyleCopOneFile() {
        Report result = parse("stylecop/onefile.xml");

        assertThat(result).hasSize(3);
    }

    /**
     * Verifies that the StyleCop parser works as expected with a file of 2 warnings (4.3 format).
     */
    @Test
    void testStyleCop43() {
        Report result = parse("stylecop/stylecop-v4.3.xml");

        assertThat(result).hasSize(2);
    }
}

