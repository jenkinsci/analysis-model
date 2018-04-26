package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link JavacParser}.
 */
class JavacParserTest extends AbstractIssueParserTest {
    JavacParserTest() {
        super("javac.txt");
    }

    @Override
    protected JavacParser createParser() {
        return new JavacParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(2);

        softly.assertThat(issues.get(1))
                .hasPriority(Priority.NORMAL)
                .hasCategory(AbstractParser.DEPRECATION)
                .hasLineStart(40)
                .hasLineEnd(40)
                .hasMessage("org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated")
                .hasFileName("C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java")
                .hasColumnStart(36);
    }

    /**
     * Parses a warning log with two false positives.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-14043">Issue 14043</a>
     */
    @Test
    void issue14043() {
        Issues warnings = parse("issue14043.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with 15 warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12482">Issue 12482</a>
     */
    @Test
    void issue12482() {
        Issues java6 = parse("issue12482-java6.txt");
        assertThat(java6).hasSize(62);

        Issues java7 = parse("issue12482-java7.txt");
        assertThat(java7).hasSize(62);
    }

    /** Verifies that the Kotlin maven plugin warnings are correctly parsed. */
    @Test
    void kotlinMavenPlugin() {
        Issues warnings = parse("kotlin-maven-plugin.txt");

        assertThat(warnings).hasSize(4);
    }

    /**
     * Parses a warning log written by Gradle containing 4 Kotlin warnings.
     */
    @Test
    void kotlinGradle() {
        Issues warnings = parse("kotlin-gradle.txt");

        assertThat(warnings).hasSize(4);
    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     */
    @Test
    void parseArrayInDeprecatedMethod() {
        Issues warnings = parse("issue5868.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Deprecation")
                    .hasLineStart(14)
                    .hasLineEnd(14)
                    .hasMessage(
                            "loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated")
                    .hasFileName("D:/path/to/my/Class.java");
        });
    }

    /**
     * Parses parallel pipeline output based on 'javac.txt'.
     */
    @Test
    void parseParallelPipelineOutput() {
        Issues warnings = parse("javac-parallel-pipeline.txt");

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(AbstractParser.DEPRECATION)
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasMessage(
                            "org.eclipse.jface.contentassist.SubjectControlContentAssistant in org.eclipse.jface.contentassist has been deprecated")
                    .hasFileName(
                            "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(AbstractParser.DEPRECATION)
                    .hasLineStart(40)
                    .hasLineEnd(40)
                    .hasMessage(
                            "org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated")
                    .hasFileName(
                            "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java");
        });
    }

    /**
     * Parses a file with five deprecation warnings.
     */
    @Test
    void parseMaven() {
        Issues warnings = parse("maven.txt");

        assertThat(warnings).hasSize(5);

        Iterator<? extends Issue> iterator = warnings.iterator();
        assertThatWarningIsAtLine(iterator.next(), 3);
        assertThatWarningIsAtLine(iterator.next(), 36);
        assertThatWarningIsAtLine(iterator.next(), 47);
        assertThatWarningIsAtLine(iterator.next(), 69);
        assertThatWarningIsAtLine(iterator.next(), 105);
    }

    /**
     * Verifies the annotation content.
     *
     * @param annotation
     *         the annotation to check
     * @param lineNumber
     *         the line number of the warning
     */
    private void assertThatWarningIsAtLine(final Issue annotation, final int lineNumber) {
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(AbstractParser.PROPRIETARY_API)
                    .hasLineStart(lineNumber)
                    .hasLineEnd(lineNumber)
                    .hasMessage(
                            "com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName(
                            "/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java");
        });
    }
}

