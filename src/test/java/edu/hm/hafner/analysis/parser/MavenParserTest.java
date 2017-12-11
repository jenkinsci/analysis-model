package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link JavacParser} for output log of a maven compile.
 */
public class MavenParserTest extends ParserTester {
    /**
     * Parses a file with five deprecation warnings.
     */
    @Test
    public void parseMaven() {
        Issues<Issue> warnings = new JavacParser().parse(openFile());

        assertThat(warnings).hasSize(5);

        Iterator<Issue> iterator = warnings.iterator();
        assertThatWarningIsAtLine(iterator.next(), 3);
        assertThatWarningIsAtLine(iterator.next(), 36);
        assertThatWarningIsAtLine(iterator.next(), 47);
        assertThatWarningIsAtLine(iterator.next(), 69);
        assertThatWarningIsAtLine(iterator.next(), 105);
    }

    /**
     * Verifies the annotation content.
     *
     * @param annotation
     *         the annotation to check
     * @param lineNumber
     *         the line number of the warning
     */
    private void assertThatWarningIsAtLine(final Issue annotation, final int lineNumber) {
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(AbstractParser.PROPRIETARY_API)
                    .hasLineStart(lineNumber)
                    .hasLineEnd(lineNumber)
                    .hasMessage("com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName("/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "maven.txt";
    }
}

