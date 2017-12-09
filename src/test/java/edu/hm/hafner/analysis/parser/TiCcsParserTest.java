package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link TiCcsParser}.
 */
public class TiCcsParserTest extends ParserTester {
    /**
     * Parses a file with warnings of the TI CodeComposer tools.
     */
    @Test
    public void parseWarnings() {
        Issues<Issue> sortedWarnings = new TiCcsParser().parse(openFile());

        assertEquals(10, sortedWarnings.size());

        Iterator<Issue> iterator = sortedWarnings.iterator();
        Issue annotation = iterator.next();
        assertSoftly(softly -> {
            softly.assertThat(sortedWarnings.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("#880-D")
                    .hasLineStart(341)
                    .hasLineEnd(341)
                    .hasMessage("parameter \"params\" was never referenced")
                    .hasFileName("C:/SCM/Lr/src/fxns.c");
            softly.assertThat(sortedWarnings.get(1))
                    .hasPriority(Priority.LOW)
                    .hasCategory("#1116-D")
                    .hasLineStart(177)
                    .hasLineEnd(177)
                    .hasMessage("may want to suffix float constant with an f")
                    .hasFileName("C:/SCM/Lr/src/edge.c");
            softly.assertThat(sortedWarnings.get(2))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("symbol 'memset' redeclared with incompatible type")
                    .hasFileName("unknown.file");
            softly.assertThat(sortedWarnings.get(3))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasMessage("variable \"h\" was declared but never referenced")
                    .hasFileName("i2cDisplay12x2.c");
            softly.assertThat(sortedWarnings.get(4))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(2578)
                    .hasLineEnd(2578)
                    .hasMessage("variable")
                    .hasFileName("c:/DOCUME~1/JLINNE~1/LOCALS~1/Temp/0360811");
            softly.assertThat(sortedWarnings.get(5))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(11)
                    .hasLineEnd(11)
                    .hasMessage("expected a \";\"")
                    .hasFileName("i2cDisplay12x2.c");
            softly.assertThat(sortedWarnings.get(6))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("unresolved symbols remain")
                    .hasFileName("unknown.file");
            softly.assertThat(sortedWarnings.get(7))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("errors encountered during linking; \"../../bin/Debug/lrxyz.out\" not")
                    .hasFileName("unknown.file");
            softly.assertThat(sortedWarnings.get(8))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("could not open source file \"i2cDisplay12x12.h\"")
                    .hasFileName("i2cDisplay12x2.c");
            softly.assertThat(sortedWarnings.get(9))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("[E0002] Illegal mnemonic specified")
                    .hasFileName("foo.asm");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "ticcs.txt";
    }
}

