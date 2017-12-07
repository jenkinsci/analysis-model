package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link TiCcsParser}.
 */
public class TiCcsParserTest extends ParserTester {
    private static final String TYPE = new TiCcsParser().getId();
    private static final String CATEGORY = DEFAULT_CATEGORY;


    /**
     * Parses a file with warnings of the TI CodeComposer tools.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseWarnings() throws IOException {
        Issues sortedWarnings = new TiCcsParser().parse(openFile());

        assertEquals(10, sortedWarnings.size());

        Iterator<Issue> iterator = sortedWarnings.iterator();
        Issue annotation = iterator.next();
        checkWarning(annotation,
                341,
                "parameter \"params\" was never referenced",
                "C:/SCM/Lr/src/fxns.c",
                TYPE, "#880-D", Priority.LOW);
        annotation = iterator.next();
        checkWarning(annotation,
                177,
                "may want to suffix float constant with an f",
                "C:/SCM/Lr/src/edge.c",
                TYPE, "#1116-D", Priority.LOW);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "symbol 'memset' redeclared with incompatible type",
                "unknown.file",
                TYPE, CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                12,
                "variable \"h\" was declared but never referenced",
                "i2cDisplay12x2.c",
                TYPE, CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                2578,
                "variable",
                "c:/DOCUME~1/JLINNE~1/LOCALS~1/Temp/0360811",
                TYPE, CATEGORY, Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                11,
                "expected a \";\"",
                "i2cDisplay12x2.c",
                TYPE, CATEGORY, Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "unresolved symbols remain",
                "unknown.file",
                TYPE, CATEGORY, Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "errors encountered during linking; \"../../bin/Debug/lrxyz.out\" not",
                "unknown.file",
                TYPE, CATEGORY, Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                3,
                "could not open source file \"i2cDisplay12x12.h\"",
                "i2cDisplay12x2.c",
                TYPE, CATEGORY, Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                5,
                "[E0002] Illegal mnemonic specified",
                "foo.asm",
                TYPE, CATEGORY, Priority.HIGH);
    }


    @Override
    protected String getWarningsFile() {
        return "ticcs.txt";
    }
}

