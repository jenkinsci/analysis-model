package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the Perl::Critic Parser.
 *
 * @author Mihail Menev, menev@hm.edu
 */
public class PerlCriticParserTest extends ParserTester {
    /**
     * Parses a mixed log file with 105 perlcritic warnings and /var/log/ messages.
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    public void testPerlCriticParser() throws IOException {
        Issues warnings = parse("perlcritic.txt");

        assertThat(warnings).hasSize(105);
    }

    /**
     * Parses a file with three warnings.
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    public void testPerlCriticParserCreateWarning() throws IOException {
        Issues warnings = parse("issue17792.txt");
        assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();

        checkWarnings(iterator.next(), Priority.LOW, "33 of PBP", 1,
                "Code is not tidy", "perl/dir_handler.pl", 1);

        checkWarnings(iterator.next(), Priority.HIGH, "431 of PBP", 10,
                "Code before warnings are enabled", "perl/system.pl", 1);

        checkWarnings(iterator.next(), Priority.NORMAL, "Use IPC::Open3 instead", 7,
                "Backtick operator used", "perl/ch1/hello", 10);
    }

    /**
     * Parses a file with three warnings without the filename in the warning.
     *
     * @throws IOException if the file cannot be read
     */
    @Test
    public void testPerlCriticParserCreateWarningNoFileName() throws IOException {
        Issues warnings = parse("issue17792-nofilename.txt");
        assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();
        checkWarnings(iterator.next(), Priority.LOW, "Don't use whitespace at the end of lines", 18,
                "Found \"\\N{SPACE}\" at the end of the line", "-", 77);

        checkWarnings(iterator.next(), Priority.NORMAL, "240,241 of PBP", 16,
                "Regular expression without \"/s\" flag", "-", 28);

        checkWarnings(iterator.next(), Priority.HIGH, "202,204 of PBP", 15,
                "Bareword file handle opened", "-", 1);
    }

    private void checkWarnings(Issue issue, Priority priority, String category, int lineStartAndEnd, String message, String fileName, int columnStart) {
        assertSoftly(softly -> softly.assertThat(issue)
                .hasPriority(priority)
                .hasCategory(category)
                .hasLineStart(lineStartAndEnd)
                .hasLineEnd(lineStartAndEnd)
                .hasMessage(message)
                .hasFileName(fileName)
                .hasColumnStart(columnStart)
        );
    }

    private Issues parse(final String fileName) throws IOException {
        return new PerlCriticParser().parse(openFile(fileName));
    }

    @Override
    protected String getWarningsFile() {
        return "perlcritic.txt";
    }
}
