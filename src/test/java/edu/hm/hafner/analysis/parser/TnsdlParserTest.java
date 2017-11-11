package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link TnsdlParser}.
 */
public class TnsdlParserTest extends ParserTester {
    private static final String TYPE = new TnsdlParser().getId();
    private static final String WARNING_CATEGORY = TnsdlParser.WARNING_CATEGORY;

    /**
     * Parses a file with four tnsdl warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new TnsdlParser().parse(openFile());

        assertThat(warnings).hasSize(4);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(398)
                    .hasLineEnd(398)
                    .hasMessage("unused variable sender_pid")
                    .hasFileName("tstmasgx.sdl");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(399)
                    .hasLineEnd(399)
                    .hasMessage("unused variable a_sender_pid")
                    .hasFileName("tstmasgx.sdl");
            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("Id. length is reserved in PL/M 386 intrinsics")
                    .hasFileName("s_dat:dty0132c.sdt");
            softly.assertThat(warnings.get(3))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(WARNING_CATEGORY)
                    .hasLineStart(4)
                    .hasLineEnd(4)
                    .hasMessage("Id. length is reserved in PL/M 386 intrinsics")
                    .hasFileName("s_dat:dty0132c.sdt");
        });

    }

    @Override
    protected String getWarningsFile() {
        return "tnsdl.txt";
    }

}

