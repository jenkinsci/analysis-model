package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the Perl::Critic Parser.
 *
 * @author Mihail Menev, menev@hm.edu
 */
public class PerlCriticParserTest extends ParserTester {
    /**
     * Parses a mixed log file with 105 perlcritic warnings and /var/log/ messages.
     */
    @Test
    public void testPerlCriticParser() {
        Issues<Issue> warnings = parse("perlcritic.txt");

        assertThat(warnings).hasSize(105);
    }

    /**
     * Parses a file with three warnings.
     */
    @Test
    public void testPerlCriticParserCreateWarning() {
        Issues<Issue> warnings = parse("issue17792.txt");
        assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();

        SoftAssertions softly = new SoftAssertions();
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

        softly.assertAll();
    }

    /**
     * Parses a file with three warnings without the filename in the warning.
     */
    @Test
    public void testPerlCriticParserCreateWarningNoFileName() {
        Issues<Issue> warnings = parse("issue17792-nofilename.txt");
        assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();

        SoftAssertions softly = new SoftAssertions();
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

        softly.assertAll();
    }

    private Issues<Issue> parse(final String fileName) {
        return new PerlCriticParser().parse(openFile(fileName));
    }

    @Override
    protected String getWarningsFile() {
        return "perlcritic.txt";
    }
}
