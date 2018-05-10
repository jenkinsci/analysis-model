package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static edu.hm.hafner.analysis.parser.JavaDocParser.CATEGORY_JAVADOC;

/**
 * Tests the class {@link JavaDocParser}.
 */
class JavaDocParserTest extends AbstractIssueParserTest {
    private static final String JAVA_DOC_LINK = "JavaDoc @link";
    private static final String JAVA_DOC_PARAM = "JavaDoc @param";

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6);

        softly.assertThat(report.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory(JAVA_DOC_LINK)
                .hasLineStart(116)
                .hasLineEnd(116)
                .hasMessage("Tag @link: can't find removeSpecChangeListener(ChangeListener, String) in chenomx.ccma.common.graph.module.GraphListenerRegistry")
                .hasFileName("/home/builder/hudson/workspace/Homer/oddjob/src/chenomx/ccma/common/graph/module/GraphListenerRegistry.java");
    }

    @Override
    protected JavaDocParser createParser() {
        return new JavaDocParser();
    }
    JavaDocParserTest() {
        super("javadoc.txt");
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
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-37975">Issue 37975</a>
     */
    @Test
    void issue37975() {
        Report warnings = parse("issue37975.txt");
        assertThat(warnings).hasSize(3);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY_JAVADOC)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("malformed HTML")
                    .hasFileName("/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java");

            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY_JAVADOC)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("bad use of '>'")
                    .hasFileName("/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java");

            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY_JAVADOC)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("unexpected end tag: </a>")
                    .hasFileName("/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java");
        });
    }

    /**
     * Parses a warning log with JavaDoc 1.8 warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-32298">Issue 32298</a>
     */
    @Test
    void issue32298() {
        Report warnings = parse("issue32298.txt");
        assertThat(warnings).hasSize(7);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(683)
                    .hasLineEnd(683)
                    .hasMessage("no description for @param")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/Apps.java");

            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(85)
                    .hasLineEnd(85)
                    .hasMessage("no description for @param")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java");

            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(86)
                    .hasLineEnd(86)
                    .hasMessage("no description for @param")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java");

            softly.assertThat(warnings.get(3))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(190)
                    .hasLineEnd(190)
                    .hasMessage("no description for @param")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java");

            softly.assertThat(warnings.get(4))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY_JAVADOC)
                    .hasLineStart(25)
                    .hasLineEnd(25)
                    .hasMessage("bad use of '>'")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottle.java");

            softly.assertThat(warnings.get(5))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY_JAVADOC)
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("malformed HTML")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottleController.java");

            softly.assertThat(warnings.get(6))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY_JAVADOC)
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("bad use of '>'")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottleController.java");
        });
    }


    /**
     * Parses a warning log with 2 JavaDoc warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4576">Issue 4576</a>
     */
    @Test
    void issue4576() {
        Report warnings = parse("issue4576.txt");

        assertThat(warnings).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY_JAVADOC)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Multiple sources of package comments found for package \"org.hamcrest\"")
                    .hasFileName("-");

            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(JAVA_DOC_PARAM)
                    .hasLineStart(94)
                    .hasLineEnd(94)
                    .hasMessage("@param argument \"<code>CoreAccountNumberTO</code>\" is not a parameter")
                    .hasFileName("/home/hudson-farm/.hudson/jobs/farm-toplevel/workspace/farm-toplevel/service-module/src/main/java/com/rackspace/farm/service/service/CoreAccountServiceImpl.java");
        });
    }

    /**
     * Parses a log with Junit message (false positive).
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8630">Issue 8630</a>
     */
    @Test
    void issue8630() {
        Report warnings = parse("issue8630.txt");

        assertThat(warnings).isEmpty();
    }

    /**
     * Parses a warning log with several JavaDoc warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7718">Issue 7718</a>
     */
    @Test
    void issue7718() {
        Report warnings = parse("issue7718.txt");

        assertThat(warnings).hasSize(7);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("JavaDoc @sys")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Text of tag @sys.prop in class ch.post.pf.mw.service.common.alarm.AlarmingService is too long!")
                    .hasFileName("-");

            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY_JAVADOC)
                    .hasLineStart(57)
                    .hasLineEnd(57)
                    .hasMessage("@(#) is an unknown tag.")
                    .hasFileName("/u01/src/KinePolygon.java");
        });
    }
}
