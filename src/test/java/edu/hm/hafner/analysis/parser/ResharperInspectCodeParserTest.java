package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link ResharperInspectCodeParser}.
 */
public class ResharperInspectCodeParserTest extends ParserTester {
    /**
     * Parses a file with warnings of the Reshaper InspectCodeParser tools.
     *
     * @throws IOException
     *         if the file could not be read
     */
    @Test
    public void parseWarnings() {
        Issues<Issue> warnings = new ResharperInspectCodeParser().parse(openFile());

        assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasLineStart(16)
                    .hasLineEnd(16)
                    .hasMessage("Cannot resolve symbol 'GetError'")
                    .hasFileName("ResharperDemo/Program.cs")
                    .hasCategory("CSharpErrors")
                    .hasPriority(Priority.HIGH);

            softly.assertThat(iterator.next())
                    .hasLineStart(23)
                    .hasLineEnd(23)
                    .hasMessage("Expression is always true")
                    .hasFileName("ResharperDemo/Program.cs")
                    .hasCategory("ConditionIsAlwaysTrueOrFalse")
                    .hasPriority(Priority.NORMAL);

            softly.assertThat(iterator.next())
                    .hasLineStart(41)
                    .hasLineEnd(41)
                    .hasMessage("Convert to auto-property")
                    .hasFileName("ResharperDemo/Program.cs")
                    .hasCategory("ConvertToAutoProperty")
                    .hasPriority(Priority.LOW);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "ResharperInspectCode.xml";
    }
}

