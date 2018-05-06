package edu.hm.hafner.analysis.parser;

import java.io.StringReader;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link XlcLinkerParser}.
 */
class XlcLinkerParserTest {
    private static final String FILE_NAME = "-";

    /**
     * Parses a string with xlC linker error.
     */
    @Test
    void shouldParseLinkerError() {
        Report report = parse("ld: 0711-987 Error occurred while reading file");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
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
    void shouldParseAnotherLinkerError() {
        Report report = parse("ld: 0711-317 ERROR: Undefined symbol: nofun()");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
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
    void shouldParseSevereError() {
        Report report = parse("ld: 0711-634 SEVERE ERROR: EXEC binder commands nested too deeply.");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
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
    void shouldParseWarning() {
        Report report = parse("ld: 0706-012 The -9 flag is not recognized.");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
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
    void shouldPareAnotherWarning() {
        Report report = parse("ld: 0711-224 WARNING: Duplicate symbol: dupe");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
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
    void shouldParseInformation() {
        Report report = parse("ld: 0711-345 Use the -bloadmap or -bnoquiet option to obtain more information.");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("0711-345")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Use the -bloadmap or -bnoquiet option to obtain more information.")
                    .hasFileName(FILE_NAME);
        });
    }

    private void assertSingleIssue(final Report report, final Consumer<SoftAssertions> assertion) {
        assertThat(report).hasSize(1);
        assertSoftly(assertion);
    }

    private Report parse(final String s) {
        return new XlcLinkerParser().parse(new StringReader(s));
    }
}

