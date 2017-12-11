package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link P4Parser}.
 */
@SuppressWarnings("ReuseOfLocalVariable")
public class P4ParserTest extends ParserTester {
    /**
     * Parses a file with four Perforce warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new P4Parser().parse(openFile());

        assertSoftly(softly -> {
            softly.assertThat(warnings).hasSize(4);
            softly.assertThat(warnings).hasNormalPrioritySize(2);
            softly.assertThat(warnings).hasLowPrioritySize(2);

            Iterator<Issue> iterator = warnings.iterator();

            Issue warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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

            warning = iterator.next();
            softly.assertThat(warning)
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
        });
    }

    @Override
    protected String getWarningsFile() {
        return "perforce.txt";
    }
}

