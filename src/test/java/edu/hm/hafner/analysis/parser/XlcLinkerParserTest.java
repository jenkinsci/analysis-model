package edu.hm.hafner.analysis.parser;

import java.io.StringReader;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link XlcLinkerParserTest}.
 */
public class XlcLinkerParserTest extends ParserTester {
    private static final String FILE_NAME = "-";

    /**
     * Parses a string with xlC linker error.
     */
    @Test
    public void testWarningsParserError1() {
        Issues<Issue> warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-987 Error occurred while reading file"));

        assertThat(warnings).hasSize(1);

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.HIGH)
                    .hasCategory("0711-987")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Error occurred while reading file")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker error.
     */
    @Test
    public void testWarningsParserError2() {
        Issues<Issue> warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-317 ERROR: Undefined symbol: nofun()"));

        assertThat(warnings).hasSize(1);

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.HIGH)
                    .hasCategory("0711-317")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Undefined symbol: nofun()")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker error.
     */
    @Test
    public void testWarningsParserSevereError() {
        Issues<Issue> warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-634 SEVERE ERROR: EXEC binder commands nested too deeply."));

        assertThat(warnings).hasSize(1);

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.HIGH)
                    .hasCategory("0711-634")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("EXEC binder commands nested too deeply.")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker warning.
     */
    @Test
    public void testWarningsParserWarning1() {
        Issues<Issue> warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0706-012 The -9 flag is not recognized."));

        assertThat(warnings).hasSize(1);

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("0706-012")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("The -9 flag is not recognized.")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker warning.
     */
    @Test
    public void testWarningsParserWarning2() {
        Issues<Issue> warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-224 WARNING: Duplicate symbol: dupe"));

        assertThat(warnings).hasSize(1);

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("0711-224")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Duplicate symbol: dupe")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker informational message.
     */
    @Test
    public void testWarningsParserInfo1() {
        Issues<Issue> warnings = new XlcLinkerParser().parse(
                new StringReader("ld: 0711-345 Use the -bloadmap or -bnoquiet option to obtain more information."));

        assertThat(warnings).hasSize(1);

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.LOW)
                    .hasCategory("0711-345")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Use the -bloadmap or -bnoquiet option to obtain more information.")
                    .hasFileName(FILE_NAME);
        });
    }

    @Override
    protected String getWarningsFile() {
        return null;
    }
}

