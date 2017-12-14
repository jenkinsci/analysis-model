package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link P4Parser}.
 */
public class P4ParserTest extends AbstractParserTest {
    P4ParserTest() {
        super("perforce.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues)
                .hasSize(4)
                .hasNormalPrioritySize(2)
                .hasLowPrioritySize(2);

        softly.assertThat(issues.get(0))
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

        softly.assertThat(issues.get(1))
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

        softly.assertThat(issues.get(2))
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

        softly.assertThat(issues.get(3))
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
    protected AbstractParser createParser() {
        return new P4Parser();
    }
}

