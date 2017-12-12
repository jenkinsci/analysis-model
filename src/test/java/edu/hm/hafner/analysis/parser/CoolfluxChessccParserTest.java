package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.parser.ParserTester.*;

/**
 * Tests the class {@link CoolfluxChessccParserTest}.
 */
public class CoolfluxChessccParserTest extends AbstractParserTest {

    /**
     * Creates a new CoolfluxChessccParserTest.
     */
    public CoolfluxChessccParserTest() {
        super("coolfluxchesscc.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        Issues<Issue> warnings = new CoolfluxChessccParser().parse(openFile());

        softly.assertThat(warnings).hasSize(1).hasDuplicatesSize(1);

        softly.assertThat(warnings.get(0))
                .hasLineStart(150)
                .hasLineEnd(150)
                .hasMessage(
                        "function `unsigned configureRealCh(unsigned)' was declared static, but was not defined")
                .hasFileName("/nfs/autofs/nett/nessie6/dailies/Monday/src/n6/heidrun/dsp/Modules/LocalChAdmin.c")
                .hasCategory(DEFAULT_CATEGORY)
                .hasPriority(Priority.HIGH);
    }

    @Override
    protected AbstractParser createParser() {
        return new CoolfluxChessccParser();
    }
}

