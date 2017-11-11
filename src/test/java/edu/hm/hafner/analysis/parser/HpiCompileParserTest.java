package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link AntJavacParser} for output log of a HPI compile.
 */
public class HpiCompileParserTest extends ParserTester {
    private static final String WARNING_TYPE = new AntJavacParser().getId();

    /**
     * Parses a file with two deprecation warnings.
     */
    @Test
    public void parseDeprecation() {
        Issues<Issue> warnings = new AntJavacParser().parse(openFile());

        assertThat(warnings).hasSize(2);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Deprecation")
                    .hasLineStart(46)
                    .hasLineEnd(46)
                    .hasMessage("newInstance(org.kohsuke.stapler.StaplerRequest) in hudson.model.Descriptor has been deprecated")
                    .hasFileName("C:/Build/Results/jobs/ADT-Base/workspace/tasks/src/main/java/hudson/plugins/tasks/TasksDescriptor.java")
                    .hasType(WARNING_TYPE);
        });

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Deprecation")
                    .hasLineStart(34)
                    .hasLineEnd(34)
                    .hasMessage("newInstance(org.kohsuke.stapler.StaplerRequest) in hudson.model.Descriptor has been deprecated")
                    .hasFileName("C:/Build/Results/jobs/ADT-Base/workspace/tasks/src/main/java/hudson/plugins/tasks/TasksReporterDescriptor.java")
                    .hasType(WARNING_TYPE);
        });
    }

    @Override
    protected String getWarningsFile() {
        return "hpi.txt";
    }
}