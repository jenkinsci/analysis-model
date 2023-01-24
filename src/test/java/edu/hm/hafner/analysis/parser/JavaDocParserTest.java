package edu.hm.hafner.analysis.parser;

import java.time.Duration;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link JavaDocParser}.
 */
class JavaDocParserTest extends AbstractParserTest {
    private static final String JAVA_DOC_LINK = "JavaDoc @link";
    private static final String JAVA_DOC_PARAM = "JavaDoc @param";

    JavaDocParserTest() {
        super("javadoc.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(JAVA_DOC_LINK)
                .hasLineStart(116)
                .hasLineEnd(116)
                .hasMessage(
                        "Tag @link: can't find removeSpecChangeListener(ChangeListener, String) in chenomx.ccma.common.graph.module.GraphListenerRegistry")
                .hasFileName(
                        "/home/builder/hudson/workspace/Homer/oddjob/src/chenomx/ccma/common/graph/module/GraphListenerRegistry.java");
    }

    @Override
    protected JavaDocParser createParser() {
        return new JavaDocParser();
    }

    /**
     * Parses a warning log with JavaDoc 1.8 warnings.
     */
    @Test
    void shouldHaveACategory() {
        Report warnings = parse("javadoc-category.txt");

        assertThat(warnings).hasSize(4);
        assertThat(warnings.filter(Issue.byCategory("JavaDoc @return"))).hasSize(4);
    }

    /**
     * Parses a warning log with JavaDoc 1.8 warnings.
     */
    @Test
    void falseJavaDocPositives() {
        Report warnings = parse("all.txt");

        assertThat(warnings).hasSize(8);
    }

    /**
     * Parses a warning log with JavaDoc 1.8 errors.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-37975">Issue 37975</a>
     */
    @Test
    void issue37975() {
        Report warnings = parse("issue37975.txt");
        assertThat(warnings).hasSize(3);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("malformed HTML")
                    .hasFileName(
                            "/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java");

            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("bad use of '>'")
                    .hasFileName(
                            "/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java");

            softly.assertThat(warnings.get(2))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("unexpected end tag: </a>")
                    .hasFileName(
                            "/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java");
        }
    }

    /**
     * Parses a warning log with JavaDoc 1.8 warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-32298">Issue 32298</a>
     */
    @Test
    void issue32298() {
        Report warnings = parse("issue32298.txt");
        assertThat(warnings).hasSize(7);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(683)
                    .hasLineEnd(683)
                    .hasMessage("no description for @param")
                    .hasFileName(
                            "/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/Apps.java");

            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(85)
                    .hasLineEnd(85)
                    .hasMessage("no description for @param")
                    .hasFileName(
                            "/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java");

            softly.assertThat(warnings.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(86)
                    .hasLineEnd(86)
                    .hasMessage("no description for @param")
                    .hasFileName(
                            "/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java");

            softly.assertThat(warnings.get(3))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(190)
                    .hasLineEnd(190)
                    .hasMessage("no description for @param")
                    .hasFileName(
                            "/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java");

            softly.assertThat(warnings.get(4))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasLineStart(25)
                    .hasLineEnd(25)
                    .hasMessage("bad use of '>'")
                    .hasFileName(
                            "/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottle.java");

            softly.assertThat(warnings.get(5))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("malformed HTML")
                    .hasFileName(
                            "/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottleController.java");

            softly.assertThat(warnings.get(6))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("bad use of '>'")
                    .hasFileName(
                            "/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottleController.java");
        }
    }

    /**
     * Parses a warning log with 2 JavaDoc warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-4576">Issue 4576</a>
     */
    @Test
    void issue4576() {
        Report warnings = parse("issue4576.txt");

        assertThat(warnings).hasSize(2);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Multiple sources of package comments found for package \"org.hamcrest\"")
                    .hasFileName("-");

            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(94)
                    .hasLineEnd(94)
                    .hasMessage("@param argument \"<code>CoreAccountNumberTO</code>\" is not a parameter")
                    .hasFileName(
                            "/home/hudson-farm/.hudson/jobs/farm-toplevel/workspace/farm-toplevel/service-module/src/main/java/com/rackspace/farm/service/service/CoreAccountServiceImpl.java");
        }
    }

    /**
     * Parses a log with Junit message (false positive).
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-8630">Issue 8630</a>
     */
    @Test
    void issue8630() {
        Report warnings = parse("issue8630.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with several JavaDoc warnings.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-7718">Issue 7718</a>
     */
    @Test
    void issue7718() {
        Report warnings = parse("issue7718.txt");
        assertThat(warnings).hasSize(7);

        Iterator<Issue> iterator = warnings.iterator();
        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("JavaDoc @sys")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(
                            "Text of tag @sys.prop in class ch.post.pf.mw.service.common.alarm.AlarmingService is too long!")
                    .hasFileName("-");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("-")
                    .hasLineStart(57)
                    .hasLineEnd(57)
                    .hasMessage("@(#) is an unknown tag.")
                    .hasFileName("/u01/src/KinePolygon.java");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("JavaDoc @param")
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("@param argument \"wBuffer\" is not a parameter name.")
                    .hasFileName("/u01/src/SpeedUnit.java");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("JavaDoc @return")
                    .hasLineStart(65)
                    .hasLineEnd(65)
                    .hasMessage("@return tag cannot be used in method with void return type.")
                    .hasFileName("/u01/src/code/com/abc/Argument.java");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("JavaDoc @see")
                    .hasLineStart(1005)
                    .hasLineEnd(1005)
                    .hasMessage("Tag @see: can't find climb(int,")
                    .hasFileName("/u01/src/code/com/abc/acPerformance/AcPerformanceServicesImpl.java");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("JavaDoc @return")
                    .hasLineStart(517)
                    .hasLineEnd(517)
                    .hasMessage(
                            "Tag @return cannot be used in field documentation. It can only be used in the following types of documentation: method.")
                    .hasFileName("/u01/src/code/com/abc/CodedRouteFormat.java");

            softly.assertThat(iterator.next())
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("JavaDoc @List")
                    .hasLineStart(64)
                    .hasLineEnd(64)
                    .hasMessage("@List is an unknown tag.")
                    .hasFileName("/u01/src/code/com/abc/adasupport/EnhancedListIterator.java");
        }
    }

    /**
     * Parses a log with Java compiler message (false positive).
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-54506">Issue 54506</a>
     */
    @Test
    void issue54506() {
        Report warnings = parse("issue54506.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with a very long line that will take several seconds to parse.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-55805">Issue 55805</a>
     */
    @Test
    void issue55805() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> parse("issue55805.txt"));
    }

    @Test
    void shouldParseJavaDocsWarningsWithMavenSourcePlugin() {
        Report warnings = parse("issue63346.log");
        assertThat(warnings).hasSize(1);

        assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(warnings.get(0)).hasLineStart(135);
        assertThat(warnings.get(0)).hasFileName("/Users/hafner/git/warnings-ng-plugin-devenv/codingstyle/src/main/java/edu/hm/hafner/util/FilteredLog.java");
    }
}
