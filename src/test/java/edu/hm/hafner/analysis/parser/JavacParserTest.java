package edu.hm.hafner.analysis.parser;

import java.io.IOException;
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
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-14043">Issue 14043</a>
     */
    @Test
    public void issue14043() throws IOException {
        Issues warnings = parse("issue14043.txt");
        assertThat(warnings).hasSize(0);
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12482">Issue 12482</a>
     */
    @Test
    public void issue12482() throws IOException {
        Issues java6 = parse("issue12482-java6.txt");
        assertThat(java6).hasSize(62);

        Issues java7 = parse("issue12482-java7.txt");
        assertThat(java7).hasSize(62);
    }

    @Test
    public void kotlinMavenPlugin() throws IOException {
        Issues warnings = parse("kotlin-maven-plugin.txt");
        assertThat(warnings).hasSize(4);
    }

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Issues warnings = new JavacParser().parse(openFile());
        assertThat(warnings).hasSize(2);

        Iterator<Issue> iterator = warnings.iterator();
        iterator.next();
        Issue firstAnnotation = iterator.next();

        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(firstAnnotation)
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
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void parseArrayInDeprecatedMethod() throws IOException {
        Issues warnings = parse("issue5868.txt");
        assertThat(warnings).hasSize(1);

        Iterator<Issue> iterator = warnings.iterator();

        Issue issue = iterator.next();
        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(issue)
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
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void parseParallelPipelineOutput() throws IOException {
        Issues warnings = parse("javac-parallel-pipeline.txt");
        assertThat(warnings).hasSize(2);

        String fileName = "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java";
        Iterator<Issue> expectedWarnings = Arrays.asList(
                new IssueBuilder().setFileName(fileName).setLineStart(12).setType(WARNING_TYPE).setCategory("Deprecation").setMessage("org.eclipse.jface.contentassist.SubjectControlContentAssistant in org.eclipse.jface.contentassist has been deprecated").build(),
                new IssueBuilder().setFileName(fileName).setLineStart(40).setType(WARNING_TYPE).setCategory("Deprecation").setMessage("org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated").build()
        ).iterator();

        Iterator<Issue> iterator = warnings.iterator();
        while (iterator.hasNext()) {
            //assertTrue(expectedWarnings.hasNext(), WRONG_NUMBER_OF_WARNINGS_DETECTED);
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

    private Issues parse(final String fileName) throws IOException {
        return new JavacParser().parse(openFile(fileName));
    }

    @Override
    protected String getWarningsFile() {
        return "javac.txt";
    }
}

