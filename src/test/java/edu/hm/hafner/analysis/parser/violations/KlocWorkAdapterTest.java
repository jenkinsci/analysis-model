package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link KlocWorkAdapter}.
 *
 * @author Ullrich Hafner
 */
class KlocWorkAdapterTest extends AbstractParserTest {
    KlocWorkAdapterTest() {
        super("klocwork.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasMessage("In method main. Variable 'bzz' was never read after null being assigned. http://server:8080/review/insight-review.html#goto:project=TestProject,pid=10")
                .hasFileName("/home/test_build/src/main/java/Main.java")
                .hasLineStart(1)
                .hasType("JD.VNU.NULL")
                .hasPriority(Priority.LOW);
        softly.assertThat(report.get(1))
                .hasMessage("In method getURLConnection. The 'getURLConnection' method throws a generic exception 'java.lang.Exception' http://server:8080/review/insight-review.html#goto:project=TestProject,pid=15")
                .hasFileName("/home/test_build/src/main/java/Main2.java")
                .hasLineStart(1)
                .hasType("EXC.BROADTHROWS")
                .hasPriority(Priority.LOW);
    }

    @Override
    protected AbstractParser createParser() {
        return new KlocWorkAdapter();
    }
}