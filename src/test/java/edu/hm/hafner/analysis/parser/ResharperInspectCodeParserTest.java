package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link ResharperInspectCodeParser}.
 */
public class ResharperInspectCodeParserTest extends AbstractParserTest {

    public static final String ISSUES_FILE = "ResharperInspectCode.xml";

    /**
     * Creates a new instance of {@link ResharperInspectCodeParserTest}.
     */
    ResharperInspectCodeParserTest() {
        super(ISSUES_FILE);
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(3);

        Iterator<Issue> iterator = issues.iterator();

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
    }

    @Override
    protected AbstractParser createParser() {
        return new ResharperInspectCodeParser();
    }
}

