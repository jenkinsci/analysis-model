package edu.hm.hafner.analysis.parser;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static org.assertj.core.api.Assertions.*;


/**
 * Tests the class {@link JavacParser}.
 */
public class JavacParserTest extends ParserTester {
    private static final String WARNING_TYPE = new JavacParser().getId();

    /**
     * Parses a warning log with two false positives.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-14043">Issue 14043</a>
     */
    @Test
    public void issue14043() {
        Issues<Issue> warnings = parse("issue14043.txt");
        assertThat(warnings).hasSize(0);
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12482">Issue 12482</a>
     */
    @Test
    public void issue12482() {
        Issues<Issue> java6 = parse("issue12482-java6.txt");
        assertThat(java6).hasSize(62);

        Issues<Issue> java7 = parse("issue12482-java7.txt");
        assertThat(java7).hasSize(62);
    }

    @Test
    public void kotlinMavenPlugin() {
        Issues<Issue> warnings = parse("kotlin-maven-plugin.txt");
        assertThat(warnings).hasSize(4);
    }

    /**
     * Parses a file with two deprecation warnings.
     */
    @Test
    public void parseDeprecation() {
        Issues<Issue> warnings = new JavacParser().parse(openFile());
        assertThat(warnings).hasSize(2);

        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(AbstractParser.DEPRECATION)
                    .hasLineStart(40)
                    .hasLineEnd(40)
                    .hasMessage("org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated")
                    .hasFileName("C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java")
                    .hasColumnStart(36)
                    .hasType(WARNING_TYPE);


        });

    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     */
    @Test
    public void parseArrayInDeprecatedMethod() {
        Issues<Issue> warnings = parse("issue5868.txt");
        assertThat(warnings).hasSize(1);

        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Deprecation")
                    .hasLineStart(14)
                    .hasLineEnd(14)
                    .hasMessage("loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated")
                    .hasFileName("D:/path/to/my/Class.java")
                    .hasType(WARNING_TYPE);


        });

    }

    /**
     * Parses parallel pipeline output based on 'javac.txt'
     */
    @Test
    public void parseParallelPipelineOutput() {
        Issues<Issue> warnings = parse("javac-parallel-pipeline.txt");
        assertThat(warnings).hasSize(2);

        String fileName = "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java";
        Iterator<Issue> expectedWarnings = Arrays.asList(
                new IssueBuilder().setFileName(fileName).setLineStart(12).setType(WARNING_TYPE).setCategory("Deprecation").setMessage("org.eclipse.jface.contentassist.SubjectControlContentAssistant in org.eclipse.jface.contentassist has been deprecated").build(),
                new IssueBuilder().setFileName(fileName).setLineStart(40).setType(WARNING_TYPE).setCategory("Deprecation").setMessage("org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated").build()
        ).iterator();

        Iterator<Issue> iterator = warnings.iterator();
        while (iterator.hasNext()) {
            assertThat(expectedWarnings.hasNext()).isTrue();

            Issue expectedWarning = expectedWarnings.next();
            final int lineNumber = expectedWarning.getLineStart();
            SoftAssertions.assertSoftly(softly -> {

                softly.assertThat(iterator.next())
                        .hasPriority(expectedWarning.getPriority())
                        .hasCategory(expectedWarning.getCategory())
                        .hasLineStart(lineNumber)
                        .hasLineEnd(lineNumber)
                        .hasMessage(expectedWarning.getMessage())
                        .hasFileName(expectedWarning.getFileName())
                        .hasType(expectedWarning.getType());


            });

        }
    }

    private Issues<Issue> parse(final String fileName) {
        return new JavacParser().parse(openFile(fileName));
    }

    @Override
    protected String getWarningsFile() {
        return "javac.txt";
    }
}

