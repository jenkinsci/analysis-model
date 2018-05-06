package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link P4Parser}.
 */
class P4ParserTest extends AbstractIssueParserTest {
    P4ParserTest() {
        super("perforce.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report)
                .hasSize(4)
                .hasPriorities(0, 2, 2);

        softly.assertThat(report.get(0))
                .hasFileName("//eng/Tools/Hudson/instances/PCFARM08/.owner")
                .hasCategory("can't add existing file")
                .hasPriority(Priority.NORMAL)
                .hasMessage("//eng/Tools/Hudson/instances/PCFARM08/.owner")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(report.get(1))
                .hasFileName("//eng/Tools/Hudson/instances/PCFARM08/jobs/EASW-FIFA DailyTasks/config.xml")
                .hasCategory("warning: add of existing file")
                .hasPriority(Priority.NORMAL)
                .hasMessage("//eng/Tools/Hudson/instances/PCFARM08/jobs/EASW-FIFA DailyTasks/config.xml")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(report.get(2))
                .hasFileName("//eng/Tools/Hudson/instances/PCFARM08/jobs/BFBC2-DailyTasksEurope/config.xml")
                .hasCategory("can't add (already opened for edit)")
                .hasPriority(Priority.LOW)
                .hasMessage("//eng/Tools/Hudson/instances/PCFARM08/jobs/BFBC2-DailyTasksEurope/config.xml")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(report.get(3))
                .hasFileName("//eng/Tools/Hudson/instances/PCFARM08/config.xml#8")
                .hasCategory("nothing changed")
                .hasPriority(Priority.LOW)
                .hasMessage("//eng/Tools/Hudson/instances/PCFARM08/config.xml#8")
                .hasDescription("")
                .hasPackageName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Override
    protected P4Parser createParser() {
        return new P4Parser();
    }
}

