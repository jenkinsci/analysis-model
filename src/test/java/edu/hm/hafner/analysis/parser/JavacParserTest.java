package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.assertj.core.api.Assertions.*;


/**
 * Tests the class {@link JavacParser}.
 */
public class JavacParserTest extends AbstractParserTest {

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     */
    protected JavacParserTest() {
        super("javac.txt");
    }

    /**
     * Parses a warning log with two false positives.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-14043">Issue 14043</a>
     */
    @Test
    public void issue14043() {
        Issues<Issue> warnings = parse("issue14043.txt");

        assertThat(warnings).isEmpty();
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

    /** Verifies that the Kotlin maven plugin warnings are correctly parsed. */
    @Test
    public void kotlinMavenPlugin() {
        Issues<Issue> warnings = parse("kotlin-maven-plugin.txt");

        assertThat(warnings).hasSize(4);
    }

    /**
     * Verifies that arrays in deprecated methods are correctly handled.
     */
    @Test
    public void parseArrayInDeprecatedMethod() {
        Issues<Issue> warnings = parse("issue5868.txt");

        assertThat(warnings).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Deprecation")
                    .hasLineStart(14)
                    .hasLineEnd(14)
                    .hasMessage("loadAvailable(java.lang.String,int,int,java.lang.String[]) in my.OtherClass has been deprecated")
                    .hasFileName("D:/path/to/my/Class.java");
        });
    }

    /**
     * Parses parallel pipeline output based on 'javac.txt'.
     */
    @Test
    public void parseParallelPipelineOutput() {
        Issues<Issue> warnings = parse("javac-parallel-pipeline.txt");

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(AbstractParser.DEPRECATION)
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasMessage("org.eclipse.jface.contentassist.SubjectControlContentAssistant in org.eclipse.jface.contentassist has been deprecated")
                    .hasFileName("C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(AbstractParser.DEPRECATION)
                    .hasLineStart(40)
                    .hasLineEnd(40)
                    .hasMessage("org.eclipse.ui.contentassist.ContentAssistHandler in org.eclipse.ui.contentassist has been deprecated")
                    .hasFileName("C:/Build/Results/jobs/ADT-Base/workspace/com.avaloq.adt.ui/src/main/java/com/avaloq/adt/ui/elements/AvaloqDialog.java");
        });
    }

    protected Issues<Issue> parse(final String fileName) {
        return createParser().parse(openFile(fileName));
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
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

    @Override
    protected AbstractParser createParser() {
        return new JavacParser();
    }
}

