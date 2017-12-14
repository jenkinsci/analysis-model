package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the Perl::Critic Parser.
 *
 * @author Mihail Menev, menev@hm.edu
 */
public class PerlCriticParserTest extends AbstractParserTest {

    PerlCriticParserTest() {
        super("perlcritic.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(105);
    }

    /**
     * Parses a file with three warnings.
     */
    @Test
    public void testPerlCriticParserCreateWarning() {
        Issues<Issue> warnings = parse("issue17792.txt");

        assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.LOW)
                    .hasCategory("33 of PBP")
                    .hasLineStart(1)
                    .hasLineEnd(1)
                    .hasMessage("Code is not tidy")
                    .hasFileName("perl/dir_handler.pl")
                    .hasColumnStart(1);

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.HIGH)
                    .hasCategory("431 of PBP")
                    .hasLineStart(10)
                    .hasLineEnd(10)
                    .hasMessage("Code before warnings are enabled")
                    .hasFileName("perl/system.pl")
                    .hasColumnStart(1);

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Use IPC::Open3 instead")
                    .hasLineStart(7)
                    .hasLineEnd(7)
                    .hasMessage("Backtick operator used")
                    .hasFileName("perl/ch1/hello")
                    .hasColumnStart(10);
        });
    }

    /**
     * Parses a file with three warnings without the filename in the warning.
     */
    @Test
    public void testPerlCriticParserCreateWarningNoFileName() {
        Issues<Issue> warnings = parse("issue17792-nofilename.txt");
        assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.LOW)
                    .hasCategory("Don't use whitespace at the end of lines")
                    .hasLineStart(18)
                    .hasLineEnd(18)
                    .hasMessage("Found \"\\N{SPACE}\" at the end of the line")
                    .hasFileName("-")
                    .hasColumnStart(77);

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("240,241 of PBP")
                    .hasLineStart(16)
                    .hasLineEnd(16)
                    .hasMessage("Regular expression without \"/s\" flag")
                    .hasFileName("-")
                    .hasColumnStart(28);

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.HIGH)
                    .hasCategory("202,204 of PBP")
                    .hasLineStart(15)
                    .hasLineEnd(15)
                    .hasMessage("Bareword file handle opened")
                    .hasFileName("-")
                    .hasColumnStart(1);
        });
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    @Override
    protected AbstractParser createParser() {
        return new PerlCriticParser();
    }
}
