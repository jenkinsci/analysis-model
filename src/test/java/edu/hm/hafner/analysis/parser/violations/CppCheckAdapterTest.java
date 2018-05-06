package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link CppCheckAdapter}.
 *
 * @author Ullrich Hafner
 */
class CppCheckAdapterTest extends AbstractParserTest {
    CppCheckAdapterTest() {
        super("cppcheck.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3);
        softly.assertThat(report.get(0))
                .hasMessage("The scope of the variable 'i' can be reduced. "
                        + "The scope of the variable 'i' can be reduced. "
                        + "Warning: It can be unsafe to fix this message. Be careful. "
                        + "Especially when there are inner loops. Here is an example where cppcheck will write that "
                        + "the scope for 'i' can be reduced:&#xa;void f(int x)&#xa;{&#xa;    int i = 0;&#xa;    "
                        + "if (x) {&#xa;        // it's safe to move 'int i = 0' here&#xa;        "
                        + "for (int n = 0; n < 10; ++n) {&#xa;            // it is possible but not safe to move "
                        + "'int i = 0' here&#xa;            do_something(&i);&#xa;        }&#xa;    }&#xa;}&#xa;When "
                        + "you see this message it is always safe to reduce the variable scope 1 level.")
                .hasFileName("api.c")
                .hasType("variableScope")
                .hasLineStart(498)
                .hasPriority(Priority.LOW);
        softly.assertThat(report.get(2))
                .hasMessage("The scope of the variable 'i' can be reduced. The scope of the variable 'i' can be reduced. "
                            + "Warning: It can be unsafe to fix this message. Be careful. "
                            + "Especially when there are inner loops. Here is an example where cppcheck will write that "
                        + "the scope for 'i' can be reduced:&#xa;void f(int x)&#xa;{&#xa;    int i = 0;&#xa;    "
                        + "if (x) {&#xa;        // it's safe to move 'int i = 0' here&#xa;        "
                        + "for (int n = 0; n < 10; ++n) {&#xa;            // it is possible but not safe to move "
                        + "'int i = 0' here&#xa;            do_something(&i);&#xa;        }&#xa;    }&#xa;}&#xa;"
                        + "When you see this message it is always safe to reduce the variable scope 1 level.")
                .hasFileName("api_storage.c")
                .hasType("variableScope")
                .hasLineStart(104)
                .hasPriority(Priority.HIGH);
    }

    @Override
    protected AbstractParser createParser() {
        return new CppCheckAdapter();
    }
}