package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Assertions.IssueSoftAssertion;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.Assertions.IssuesAssert.*;

/**
 * Tests the class {@link JavaDocParser}.
 */
public class JavaDocParserTest extends ParserTester {
    private static final String TYPE = new JavaDocParser().getId();
    private static final String CATEGORY = DEFAULT_CATEGORY;

    /**
     * Parses a warning log with JavaDoc 1.8 warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void falseJavaDocPositives() throws IOException {
        Issues warnings = new JavaDocParser().parse(openFile("all.txt"));
        assertThat(warnings).hasSize(8);
    }

    /**
     * Parses a warning log with JavaDoc 1.8 errors.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-37975">Issue 37975</a>
     */
    @Test
    public void issue37975() throws IOException {
        Issues warnings = new JavaDocParser().parse(openFile("issue37975.txt"));

        assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();
        Issue firstIssue = iterator.next();


        IssueSoftAssertion.assertIssueSoftly(softly-> {

            softly.assertThat(firstIssue)
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("malformed HTML")
                    .hasFileName("/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java")
                    .hasType(TYPE);


        });

        Issue secondIssue = iterator.next();


        IssueSoftAssertion.assertIssueSoftly(softly-> {

            softly.assertThat(secondIssue)
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("bad use of '>'")
                    .hasFileName("/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java")
                    .hasType(TYPE);


        });

        Issue thirdIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {

            softly.assertThat(thirdIssue)
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY)
                    .hasLineStart(79)
                    .hasLineEnd(79)
                    .hasMessage("unexpected end tag: </a>")
                    .hasFileName("/home/jeans/ideaWork/cache2k-internal/cache2k/api/src/main/java/org/cache2k/processor/MutableCacheEntry.java")
                    .hasType(TYPE);


        });

    }

    /**
     * Parses a warning log with JavaDoc 1.8 warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-32298">Issue 32298</a>
     */
    @Test
    public void issue32298() throws IOException {
        Issues warnings = new JavaDocParser().parse(openFile("issue32298.txt"));
        assertThat(warnings).hasSize(7);

        Iterator<Issue> iterator = warnings.iterator();
        Issue firstIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {
            softly.assertThat(firstIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(683)
                    .hasLineEnd(683)
                    .hasMessage("no description for @param")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/Apps.java")
                    .hasType(TYPE);


        });

        Issue secondIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {
            softly.assertThat(secondIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(85)
                    .hasLineEnd(85)
                    .hasMessage("no description for @param")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java")
                    .hasType(TYPE);

        });

        Issue thirdIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {
            softly.assertThat(thirdIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(86)
                    .hasLineEnd(86)
                    .hasMessage("no description for @param")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java")
                    .hasType(TYPE);


        });

        Issue fourthIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {
            softly.assertThat(fourthIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(190)
                    .hasLineEnd(190)
                    .hasMessage("no description for @param")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/apps/AppsLaunchFrame.java")
                    .hasType(TYPE);

        });

        Issue fifthIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {
            softly.assertThat(fifthIssue)
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY)
                    .hasLineStart(25)
                    .hasLineEnd(25)
                    .hasMessage("bad use of '>'")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottle.java")
                    .hasType(TYPE);
        });

        Issue sixthIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {
            softly.assertThat(sixthIssue)
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY)
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("malformed HTML")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottleController.java")
                    .hasType(TYPE);
        });

        Issue seventhIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {

            softly.assertThat(seventhIssue)
                    .hasPriority(Priority.HIGH)
                    .hasCategory(CATEGORY)
                    .hasLineStart(26)
                    .hasLineEnd(26)
                    .hasMessage("bad use of '>'")
                    .hasFileName("/var/lib/jenkins/jobs/Development/jobs/JavaDoc check/workspace/java/src/jmri/jmrit/withrottle/MultiThrottleController.java")
                    .hasType(TYPE);


        });

    }

    /**
     * Parses a file with 6 warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseJavaDocWarnings() throws IOException {
        Issues warnings = new JavaDocParser().parse(openFile());

        assertThat(warnings).hasSize(6);
        Iterator<Issue> iterator = warnings.iterator();
        Issue firstIssue = iterator.next();

        IssueSoftAssertion.assertIssueSoftly(softly-> {
            softly.assertThat(firstIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(116)
                    .hasLineEnd(116)
                    .hasMessage("Tag @link: can't find removeSpecChangeListener(ChangeListener, String) in chenomx.ccma.common.graph.module.GraphListenerRegistry")
                    .hasFileName("/home/builder/hudson/workspace/Homer/oddjob/src/chenomx/ccma/common/graph/module/GraphListenerRegistry.java")
                    .hasType(TYPE);

        });

    }

    /**
     * Parses a warning log with 2 JavaDoc warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4576">Issue 4576</a>
     */
    @Test
    public void issue4576() throws IOException {
        Issues warnings = new JavaDocParser().parse(openFile("issue4576.txt"));

        assertThat(warnings).hasSize(2);
        Iterator<Issue> iterator = warnings.iterator();
        Issue firstIssue = iterator.next();


        IssueSoftAssertion.assertIssueSoftly(softly-> {

            softly.assertThat(firstIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Multiple sources of package comments found for package \"org.hamcrest\"")
                    .hasFileName("-")
                    .hasType(TYPE);


        });

        Issue secondIssue = iterator.next();
        IssueSoftAssertion.assertIssueSoftly(softly-> {

            softly.assertThat(secondIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(94)
                    .hasLineEnd(94)
                    .hasMessage("@param argument \"<code>CoreAccountNumberTO</code>\" is not a parameter")
                    .hasFileName("/home/hudson-farm/.hudson/jobs/farm-toplevel/workspace/farm-toplevel/service-module/src/main/java/com/rackspace/farm/service/service/CoreAccountServiceImpl.java")
                    .hasType(TYPE);
        });

    }

    /**
     * Parses a log with Junit message (false positive).
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8630">Issue 8630</a>
     */
    @Test
    public void issue8630() throws IOException {
        Issues warnings = new JavaDocParser().parse(openFile("issue8630.txt"));
        assertThat(warnings).hasSize(0);
    }

    /**
     * Parses a warning log with several JavaDoc warnings.
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7718">Issue 7718</a>
     */
    @Test
    public void issue7718() throws IOException {
        Issues warnings = new JavaDocParser().parse(openFile("issue7718.txt"));

        assertThat(warnings).hasSize(7);
        Iterator<Issue> iterator = warnings.iterator();
        Issue firstIssue = iterator.next();

        IssueSoftAssertion.assertIssueSoftly(softly -> {
            softly.assertThat(firstIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Text of tag @sys.prop in class ch.post.pf.mw.service.common.alarm.AlarmingService is too long!")
                    .hasFileName("-")
                    .hasType(TYPE);
        });

        Issue secondIssue = iterator.next();


        IssueSoftAssertion.assertIssueSoftly(softly-> {
            softly.assertThat(secondIssue)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(57)
                    .hasLineEnd(57)
                    .hasMessage("@(#) is an unknown tag.")
                    .hasFileName("/u01/src/KinePolygon.java")
                    .hasType(TYPE);
        });

    }

    @Override
    protected String getWarningsFile() {
        return "javadoc.txt";
    }
}

