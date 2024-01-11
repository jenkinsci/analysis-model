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
 * Tests the Perl::Critic Parser.
 *
 * @author Mihail Menev, menev@hm.edu
 */
class PerlCriticParserTest extends AbstractParserTest {
    PerlCriticParserTest() {
        super("perlcritic.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(105);
    }

    /**
     * Parses a file with three warnings.
     */
    @Test
    void testPerlCriticParserCreateWarning() {
        Report warnings = parse("issue17792.txt");

        assertThat(warnings).hasSize(3);

        Iterator<? extends Issue> iterator = warnings.iterator();

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasCategory("33 of PBP")
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("Code is not tidy")
                    .hasFileName("perl/dir_handler.pl")
                    .hasColumnStart(1);

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasCategory("431 of PBP")
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasMessage("Code before warnings are enabled")
                    .hasFileName("perl/system.pl")
                    .hasColumnStart(1);

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Use IPC::Open3 instead")
                    .hasLineStart(7)
                    .hasLineEnd(7)
                    .hasMessage("Backtick operator used")
                    .hasFileName("perl/ch1/hello")
                    .hasColumnStart(10);
        }
    }

    /**
     * Parses a file with three warnings without the filename in the warning.
     */
    @Test
    void testPerlCriticParserCreateWarningNoFileName() {
        Report warnings = parse("issue17792-nofilename.txt");
        assertThat(warnings).hasSize(3);

        Iterator<? extends Issue> iterator = warnings.iterator();

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasCategory("Don't use whitespace at the end of lines")
                    .hasLineStart(18)
                    .hasLineEnd(18)
                    .hasMessage("Found \"\\N{SPACE}\" at the end of the line")
                    .hasFileName("-")
                    .hasColumnStart(77);

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("240,241 of PBP")
                    .hasLineStart(16)
                    .hasLineEnd(16)
                    .hasMessage("Regular expression without \"/s\" flag")
                    .hasFileName("-")
                    .hasColumnStart(28);

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasCategory("202,204 of PBP")
                    .hasLineStart(15)
                    .hasLineEnd(15)
                    .hasMessage("Bareword file handle opened")
                    .hasFileName("-")
                    .hasColumnStart(1);
        }
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    @Override
    protected PerlCriticParser createParser() {
        return new PerlCriticParser();
    }
}
