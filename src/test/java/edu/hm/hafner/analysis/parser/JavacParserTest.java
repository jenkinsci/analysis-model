package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static org.junit.Assert.*;

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

        assertEquals(0, warnings.size());
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

        assertEquals(62, java6.size());

        Issues java7 = parse("issue12482-java7.txt");

        assertEquals(62, java7.size());
    }

    @Test
    public void kotlinMavenPlugin() throws IOException {
        Issues warnings = parse("kotlin-maven-plugin.txt");
        assertEquals(4, warnings.size());
    }

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Issues warnings = new JavacParser().parse(openFile());

        assertEquals(2, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();
        annotation = iterator.next();
        checkWarning(annotation,
                40, 36,
                "org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated",
                "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java",
                WARNING_TYPE, AbstractParser.DEPRECATION, Priority.NORMAL);
    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void parseArrayInDeprecatedMethod() throws IOException {
        Issues warnings = parse("issue5868.txt");

        assertEquals(1, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                14,
                "loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated",
                "D:/path/to/my/Class.java",
                WARNING_TYPE, "Deprecation", Priority.NORMAL);
    }

    /**
     * Parses parallel pipeline output based on 'javac.txt'
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void parseParallelPipelineOutput() throws IOException {
        Issues warnings = parse("javac-parallel-pipeline.txt");

        assertEquals(2, warnings.size());

        String fileName = "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java";
        Iterator<Issue> expectedWarnings = Arrays.asList(
                new IssueBuilder()
                        .setFileName(fileName)
                        .setLineStart(12)
                        .setType(WARNING_TYPE)
                        .setCategory("Deprecation")
                        .setMessage("org.eclipse.jface.contentassist.SubjectControlContentAssistant in org.eclipse.jface.contentassist has been deprecated")
                        .build(),
                new IssueBuilder()
                        .setFileName(fileName)
                        .setLineStart(40)
                        .setType(WARNING_TYPE)
                        .setCategory("Deprecation")
                        .setMessage("org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated")
                        .build()
        ).iterator();

        Iterator<Issue> iterator = warnings.iterator();
        while (iterator.hasNext()) {
            assertTrue(WRONG_NUMBER_OF_WARNINGS_DETECTED, expectedWarnings.hasNext());
            Issue expectedWarning = expectedWarnings.next();
            checkWarning(iterator.next(), expectedWarning.getLineStart(), expectedWarning.getMessage(), expectedWarning.getFileName(), expectedWarning
                    .getType(), expectedWarning.getCategory(), expectedWarning.getPriority());
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

