package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

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

        assertEquals(105, warnings.size());
    }

    /**
     * Parses a file with three warnings.
     *
     * @throws IOException if the file cannot be read.
     */
    @Test
    public void testPerlCriticParserCreateWarning() throws IOException {
        Issues warnings = parse("issue17792.txt");

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();

        checkWarning(annotation, 1, 1, "Code is not tidy", "perl/dir_handler.pl", "33 of PBP", Priority.LOW);

        annotation = iterator.next();
        checkWarning(annotation, 10, 1, "Code before warnings are enabled", "perl/system.pl", "431 of PBP", Priority.HIGH);

        annotation = iterator.next();
        checkWarning(annotation, 7, 10, "Backtick operator used", "perl/ch1/hello", "Use IPC::Open3 instead",
                Priority.NORMAL);
    }

    /**
     * Parses a file with three warnings without the filename in the warning.
     *
     * @throws IOException if the file cannot be read
     */
    @Test
    public void testPerlCriticParserCreateWarningNoFileName() throws IOException {
        Issues warnings = parse("issue17792-nofilename.txt");

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();

        checkWarning(annotation, 18, 77, "Found \"\\N{SPACE}\" at the end of the line", "-", "Don't use whitespace at the end of lines", Priority.LOW);

        annotation = iterator.next();
        checkWarning(annotation, 16, 28, "Regular expression without \"/s\" flag", "-", "240,241 of PBP", Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation, 15, 1, "Bareword file handle opened", "-", "202,204 of PBP", Priority.HIGH);
    }

    private Issues parse(final String fileName) throws IOException {
        return new PerlCriticParser().parse(openFile(fileName));
    }

    @Override
    protected String getWarningsFile() {
        return "perlcritic.txt";
    }
}
