package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link YuiCompressorParser}.
 *
 * @author Emidio Stani
 */
public class YuiCompressorParserTest extends ParserTester {
    /**
     * Parses a file with 3 warnings.
     */
    @Test
    public void parseDeprecation() {
        Issues<Issue> warnings = new YuiCompressorParser().parse(openFile());

        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("Use single 'var' per scope")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Try to use a single 'var' statement per scope."
                        + " [match){returnstringResult;}for( ---> var  <--- i=0;match&&i<match]")
                    .hasFileName("unknown.file");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Duplicate variable")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("The variable replacement has already been declared in the same scope..."
                        + " [replace(variable,replacement);}var  ---> replacement <--- =globalStoredVars[name];if(replacement!=]")
                    .hasFileName("unknown.file");
            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Use eval")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(
                            "Using 'eval' is not recommended. Moreover, using 'eval' reduces the level of compression!"
                        + " [function(condition,label){if( ---> eval <--- (condition)){this.doGotolabel(label]")
                    .hasFileName("unknown.file");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "yui.txt";
    }
}
