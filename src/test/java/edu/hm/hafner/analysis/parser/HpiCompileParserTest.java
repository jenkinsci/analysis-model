package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link AntJavacParser} for output log of a HPI compile.
 */
class HpiCompileParserTest extends AbstractParserTest {
    HpiCompileParserTest() {
        super("hpi.txt");
    }

    @Override
    protected AntJavacParser createParser() {
        return new AntJavacParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Deprecation")
                .hasLineStart(46)
                .hasLineEnd(46)
                .hasMessage(
                        "newInstance(org.kohsuke.stapler.StaplerRequest) in hudson.model.Descriptor has been deprecated")
                .hasFileName(
                        "C:/Build/Results/jobs/ADT-Base/workspace/tasks/src/main/java/hudson/plugins/tasks/TasksDescriptor.java");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Deprecation")
                .hasLineStart(34)
                .hasLineEnd(34)
                .hasMessage(
                        "newInstance(org.kohsuke.stapler.StaplerRequest) in hudson.model.Descriptor has been deprecated")
                .hasFileName(
                        "C:/Build/Results/jobs/ADT-Base/workspace/tasks/src/main/java/hudson/plugins/tasks/TasksReporterDescriptor.java");
    }
}
