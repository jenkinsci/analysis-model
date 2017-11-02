package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

/**
 * Tests the class {@link ResharperInspectCodeParser}.
 */
public class ResharperInspectCodeParserTest extends ParserTester {
    /**
     * Parses a file with warnings of the Reshaper InspectCodeParser  tools.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseWarnings() throws IOException {
        Issues warnings = new ResharperInspectCodeParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        //<Issue TypeId="CSharpErrors" File="ResharperDemo\Program.cs" Offset="408-416" Line="16" Message="" />
        Issue annotation = iterator.next();
        checkWarning(annotation,
                16,
                "Cannot resolve symbol 'GetError'",
                "ResharperDemo/Program.cs",
                "ResharperInspectCode",
                "CSharpErrors",
                Priority.HIGH);

        annotation = iterator.next();
        checkWarning(annotation,
                23,
                "Expression is always true",
                "ResharperDemo/Program.cs",
                "ResharperInspectCode",
                "ConditionIsAlwaysTrueOrFalse",
                Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                41,
                "Convert to auto-property",
                "ResharperDemo/Program.cs",
                "ResharperInspectCode",
                "ConvertToAutoProperty",
                Priority.LOW);
    }

    @Override
    protected String getWarningsFile() {
        return "ResharperInspectCode.xml";
    }
}

