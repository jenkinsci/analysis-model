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
 * Tests the class {@link PyLintParser}.
 */
public class PylintParserTest extends ParserTester {
    private static final String WARNING_TYPE = new PyLintParser().getId();

    /**
     * Parses a txt file, containing 3 warnings.
     *
     * @throws IOException
     *         if the file could not be read
     */
    @Test
    public void pyLintTest() {
        Issues<Issue> warnings = new PyLintParser().parse(openFile());

        assertThat(warnings).hasSize(6);

        Iterator<Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("Line too long (85/80)")
                    .hasFileName("trunk/src/python/cachedhttp.py")
                    .hasType(WARNING_TYPE)
                    .hasCategory("C")
                    .hasPriority(Priority.LOW);

            softly.assertThat(iterator.next())
                    .hasLineStart(28)
                    .hasLineEnd(28)
                    .hasMessage("Invalid name \"seasonCount\" (should match [a-z_][a-z0-9_]{2,30}$)")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType(WARNING_TYPE)
                    .hasCategory("C0103")
                    .hasPriority(Priority.LOW);

            softly.assertThat(iterator.next())
                    .hasLineStart(35)
                    .hasLineEnd(35)
                    .hasMessage("Missing docstring")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType(WARNING_TYPE)
                    .hasCategory("C0111")
                    .hasPriority(Priority.LOW);

            softly.assertThat(iterator.next())
                    .hasLineStart(39)
                    .hasLineEnd(39)
                    .hasMessage("Method should have \"self\" as first argument")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType(WARNING_TYPE)
                    .hasCategory("E0213")
                    .hasPriority(Priority.HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("Unable to import 'deadbeef'")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType(WARNING_TYPE)
                    .hasCategory("F0401")
                    .hasPriority(Priority.HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(39)
                    .hasLineEnd(39)
                    .hasMessage("Dangerous default value \"[]\" as argument")
                    .hasFileName("trunk/src/python/tv.py")
                    .hasType(WARNING_TYPE)
                    .hasCategory("W0102")
                    .hasPriority(Priority.NORMAL);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "pyLint.txt";
    }
}
