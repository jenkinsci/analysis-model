package edu.hm.hafner.analysis.parser;

import java.time.Duration;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link JavacParser}.
 */
class JavacParserTest extends AbstractParserTest {
    JavacParserTest() {
        super("javac.txt");
    }

    @Override
    protected JavacParser createParser() {
        return new JavacParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(2);

        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(Categories.DEPRECATION)
                .hasLineStart(40)
                .hasLineEnd(40)
                .hasMessage(
                        "org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated")
                .hasFileName(
                        "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java")
                .hasColumnStart(36);
    }

    /**
     * Parses a warning log with JavaDoc warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-63346">Issue 63346</a>
     */
    @Test
    void issue63346() {
        Report report = parse("issue63346.txt");

        assertThat(report).isEmpty();
    }

    /**
     * Parses a warning log with two warning generated on windows.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-66737">Issue 66738</a>
     */
    @Test
    void issue66738() {
        Report report = parse("issue66738.txt");
        assertThat(report).hasSize(2);
        
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(report.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("")
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasType("maven-compiler-plugin:compile")
                    .hasMessage(
                            "found raw type: java.util.ArrayList")
                    .hasFileName(
                            "C:/Users/user1/JENKINS-66738/src/main/java/simple/HelloWorld.java")
                    .hasColumnStart(42);
        }
    }

    /**
     * Parses a warning log with two false positives.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-55805">Issue 55805</a>
     */
    @Test
    void issue55805() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> parse("issue55805.txt"));
    }

    /**
     * Parses a file with ANSI colors.
     *
     * @see <a href="https://github.com/jenkinsci/analysis-model/pull/118">PR #118</a>
     */
    @Test
    void shouldParseAnsiColorCodedLog() {
        Report report = parse("maven-ansi.txt");

        assertThat(report).hasSize(4);
    }

    /**
     * Parses a warning log with 4 compile errors.
     */
    @Test
    void shouldParseErrors() {
        Report warnings = parse("javac-errors.txt");

        assertThat(warnings).hasSize(4);
        assertThat(warnings.get(0)).hasSeverity(Severity.ERROR);
    }

    /**
     * Parses a log with 3 valid and 3 invalid timestamp preceding warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-56737">JENKINS-56737</a>
     */
    @Test
    void shouldParseTimestamps() {
        Report warnings = parse("javac-timestamps.txt");

        assertThat(warnings).hasSize(5);
        assertThat(warnings.get(0)).hasMessage("Test1");
        assertThat(warnings.get(1)).hasMessage("Test2");
        assertThat(warnings.get(2)).hasMessage("Test3");
        assertThat(warnings.get(3)).hasMessage("Test4");
        assertThat(warnings.get(4)).hasMessage("Test8");
    }

    /**
     * Parses a warning log with two false positives.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-14043">Issue 14043</a>
     */
    @Test
    void issue14043() {
        Report warnings = parse("issue14043.txt");

        assertThat(warnings).isEmpty();
    }

    /** Verifies that the Kotlin maven plugin warnings are correctly parsed. */
    @Test
    void kotlinMavenPlugin() {
        Report warnings = parse("kotlin-maven-plugin.txt");

        assertThat(warnings).hasSize(4);
    }

    /**
     * Parses a warning log written by Gradle containing 4 Kotlin warnings.
     */
    @Test
    void kotlinGradle() {
        Report warnings = parse("kotlin-gradle.txt");

        assertThat(warnings).hasSize(4);
    }

    /**
     * Parses an error log written by Gradle containing 1 Kotlin error.
     */
    @Test
    void kotlinGradleError() {
        Report errors = parse("kotlin-gradle-error.txt");

        assertThat(errors).hasSize(1);
    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     */
    @Test
    void parseArrayInDeprecatedMethod() {
        Report warnings = parse("issue5868.txt");

        assertThat(warnings).hasSize(1);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Deprecation")
                    .hasLineStart(14)
                    .hasLineEnd(14)
                    .hasMessage(
                            "loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated")
                    .hasFileName("D:/path/to/my/Class.java");
        }
    }

    /**
     * Parses parallel pipeline output based on 'javac.txt'.
     */
    @Test
    void parseParallelPipelineOutput() {
        Report warnings = parse("javac-parallel-pipeline.txt");

        assertThat(warnings).hasSize(2);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(Categories.DEPRECATION)
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasMessage(
                            "org.eclipse.jface.contentassist.SubjectControlContentAssistant in org.eclipse.jface.contentassist has been deprecated")
                    .hasFileName(
                            "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java");
            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(Categories.DEPRECATION)
                    .hasLineStart(40)
                    .hasLineEnd(40)
                    .hasMessage(
                            "org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated")
                    .hasFileName(
                            "C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java");
        }
    }

    /**
     * Parses a file with five deprecation warnings.
     */
    @Test
    void parseMaven() {
        Report warnings = parse("maven.txt");

        assertThat(warnings).hasSize(5);

        Iterator<? extends Issue> iterator = warnings.iterator();
        assertThatWarningIsAtLine(iterator.next(), 3);
        assertThatWarningIsAtLine(iterator.next(), 36);
        assertThatWarningIsAtLine(iterator.next(), 47);
        assertThatWarningIsAtLine(iterator.next(), 69);
        assertThatWarningIsAtLine(iterator.next(), 105);
    }

    @Test
    void shouldNotIncludeErrorprone() {
        Report warnings = parse("javac-errorprone.txt");

        assertThat(warnings).hasSize(2);
        assertThat(warnings.get(0)).hasSeverity(Severity.ERROR);
        assertThat(warnings.get(1)).hasSeverity(Severity.ERROR);

        warnings = parse("errorprone-maven.log");

        assertThat(warnings).hasSize(0);
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
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(annotation)
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(Categories.PROPRIETARY_API)
                    .hasLineStart(lineNumber)
                    .hasLineEnd(lineNumber)
                    .hasMessage(
                            "com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName(
                            "/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java");
        }
    }

    @Test
    void shouldParseJavaWarningsInMavenCompilerPlugin(){
        Report warnings = parse("tracker_issue63346.log");
        assertThat(warnings).hasSize(4);
        assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(warnings.get(1)).hasSeverity(Severity.WARNING_NORMAL);
    }
}

