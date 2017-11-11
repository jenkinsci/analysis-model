package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link ResharperInspectCodeParser}.
 */
public class ResharperInspectCodeParserTest extends ParserTester {

    private static final String WARNING_TYPE = "ResharperInspectCode";

    /**
     * Parses a file with warnings of the Reshaper InspectCodeParser  tools.
     *
     * @throws IOException
     *         if the file could not be read
     */
    @Test
    public void parseWarnings() {
        Issues<Issue> warnings = new ResharperInspectCodeParser().parse(openFile());

        assertEquals(3, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasLineStart(16)
                    .hasLineEnd(16)
                    .hasMessage("Cannot resolve symbol 'GetError'")
                    .hasFileName("ResharperDemo/Program.cs")
                    .hasType(WARNING_TYPE)
                    .hasCategory("CSharpErrors")
                    .hasPriority(Priority.HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(23)
                    .hasLineEnd(23)
                    .hasMessage("Expression is always true")
                    .hasFileName("ResharperDemo/Program.cs")
                    .hasType(WARNING_TYPE)
                    .hasCategory("ConditionIsAlwaysTrueOrFalse")
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(41)
                    .hasLineEnd(41)
                    .hasMessage("Convert to auto-property")
                    .hasFileName("ResharperDemo/Program.cs")
                    .hasType(WARNING_TYPE)
                    .hasCategory("ConvertToAutoProperty")
                    .hasPriority(Priority.LOW);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "ResharperInspectCode.xml";
    }
}

