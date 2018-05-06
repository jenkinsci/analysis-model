package edu.hm.hafner.analysis.parser;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import edu.hm.hafner.analysis.parser.FindBugsParser.InputStreamProvider;
import edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty;
import static edu.hm.hafner.analysis.parser.FindBugsParser.PriorityProperty.*;
import edu.hm.hafner.analysis.parser.FindBugsParser.XmlBugInstance;

/**
 * Tests the extraction of FindBugs analysis results.
 */
class FindBugsParserTest {
    private static final String PREFIX = "findbugs/";

    private static final String SECOND_WARNING_HASH = "f32497e4bd8c80ef6228f10bd3363f52";
    private static final String FIRST_WARNING_HASH = "4d839755cabf60eacc6438ac77ac5104";
    /** File in native format. */
    private static final String FINDBUGS_NATIVE_XML = "findbugs-native.xml";

    private Report parseFile(final String fileName, final PriorityProperty priorityProperty) {
        return new FindBugsParser(priorityProperty).parse(getInputStreamProvider(PREFIX + fileName),
                Collections.emptyList(), new IssueBuilder());
    }

    private InputStreamProvider getInputStreamProvider(final String fileName) {
        return () -> read(fileName);
    }

    private InputStream read(final String fileName) {
        return FindBugsParserTest.class.getResourceAsStream(fileName);
    }

    /**
     * Parses messages from SpotBugs.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-46975">JENKINS-46975</a>
     */
    @Test
    void issue46975() {
        Report report = parseFile("spotbugsXml.xml", CONFIDENCE);
        assertThat(report).hasSize(2);

        assertSoftly(softly -> {
            softly.assertThat(report.get(0))
                    .hasFileName("edu/hm/hafner/analysis/IssuesTest.java")
                    .hasCategory("STYLE")
                    .hasType("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Return value of Issues.get(int) ignored, but method has no side effect")
                    .hasPackageName("edu.hm.hafner.analysis")
                    .hasModuleName("Static Analysis Model and Parsers")
                    .hasLineStart(286)
                    .hasLineEnd(286)
                    .hasFingerprint("3d78cb510b96490fd951f32d93e4e9ba");
            softly.assertThat(report.get(1))
                    .hasFileName("edu/hm/hafner/analysis/IssuesTest.java")
                    .hasCategory("STYLE")
                    .hasType("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
                    .hasPriority(Priority.NORMAL)
                    .hasMessage("Return value of Issues.get(int) ignored, but method has no side effect")
                    .hasPackageName("edu.hm.hafner.analysis")
                    .hasModuleName("Static Analysis Model and Parsers")
                    .hasLineStart(289)
                    .hasLineEnd(289)
                    .hasFingerprint("cc577f74735570f875f75b479484fecf");
        });
    }

    /**
     * Parses fb-contrib messages.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7238">Issue 7238</a>
     */
    @Test
    void issue7238() {
        Report report = parseFile("issue7238.xml", CONFIDENCE);

        assertThat(report).hasSize(1808).hasDuplicatesSize(12); // 12 issues are skipped (same attributes, but different instance hash)
    }

    /**
     * Parses fb-contrib messages.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-12314">Issue 12314</a>
     */
    @Test
    void issue12314() {
        Report report = parseFile("issue12314.xml", CONFIDENCE);
        assertThat(report).hasSize(1);

        assertSoftly(softly -> {
            softly.assertThat(report.get(0))
                    .hasFileName("com/sedsystems/core/valid/Transformers.java")
                    .hasPackageName("com.sedsystems.core.valid")
                    .hasPriority(Priority.NORMAL)
                    .hasModuleName("issue12314.xml")
                    .hasLineStart(60)
                    .hasLineEnd(60);
        });
    }

    /**
     * Tests the message mapping.
     */
    @Test
    void testMessageMapping() throws Exception {
        try (InputStream stream = read(PREFIX + FINDBUGS_NATIVE_XML)) {
            Map<String, String> mapping = new HashMap<>();
            for (XmlBugInstance bug : new FindBugsParser(CONFIDENCE).preParse(stream)) {
                mapping.put(bug.getInstanceHash(), bug.getMessage());
            }
            assertThat(mapping).hasSize(2);
            assertThat(mapping).containsKeys(FIRST_WARNING_HASH, SECOND_WARNING_HASH);
            assertThat(mapping.get(FIRST_WARNING_HASH)).isEqualTo(
                    "Inconsistent synchronization of org.apache.hadoop.dfs.BlockCrcUpgradeObjectDatanode.blocksPreviouslyUpgraded; locked 85% of time");
            assertThat(mapping.get(SECOND_WARNING_HASH)).isEqualTo(
                    "Should org.apache.hadoop.streaming.StreamJob$MultiPropertyOption be a _static_ inner class?");
        }
    }

    /**
     * Checks whether we correctly detect a file in FindBugs native format.
     */
    @Test
    void testFileWithMultipleLinesAndRanges() {
        scanNativeFile(FINDBUGS_NATIVE_XML, FINDBUGS_NATIVE_XML,
                Priority.NORMAL, "org/apache/hadoop/dfs/BlockCrcUpgrade.java", "org.apache.hadoop.dfs", 1309, 1309,
                4, "org/apache/hadoop/streaming/StreamJob.java", "org.apache.hadoop.streaming", 935, 980, 0, CONFIDENCE);
        scanNativeFile(FINDBUGS_NATIVE_XML, FINDBUGS_NATIVE_XML,
                Priority.LOW, "org/apache/hadoop/dfs/BlockCrcUpgrade.java", "org.apache.hadoop.dfs", 1309, 1309,
                4, "org/apache/hadoop/streaming/StreamJob.java", "org.apache.hadoop.streaming", 935, 980, 0, RANK);
    }

    /**
     * Checks whether, if a bug instance contains more than one element, we correctly take the first one as referring to
     * the buggy class.
     */
    @Test
    void scanFileWarningsHaveMultipleClasses() {
        scanNativeFile("findbugs-multclass.xml", "FindBugs",
                Priority.HIGH, "umd/cs/findbugs/PluginLoader.java", "edu.umd.cs.findbugs", 82, 82,
                0, "edu/umd/cs/findbugs/PluginLoader.java", "edu.umd.cs.findbugs", 93, 93, 0, CONFIDENCE);
        scanNativeFile("findbugs-multclass.xml", "FindBugs",
                Priority.LOW, "umd/cs/findbugs/PluginLoader.java", "edu.umd.cs.findbugs", 82, 82,
                0, "edu/umd/cs/findbugs/PluginLoader.java", "edu.umd.cs.findbugs", 93, 93, 0, RANK);
    }

    /**
     * Checks whether we could also parse bugs of the fbcontrib plug-in.
     */
    @Test
    void scanFbContribFile() {
        Report report = parseFile("fbcontrib.xml", CONFIDENCE);
        assertThat(report.filter(Issue.byPackageName("hudson.plugins.tasks"))).hasSize(16);
        assertThat(report.filter(Issue.byFileName("hudson/plugins/tasks/ResultSummary.java"))).hasSize(2);
    }

    /**
     * Checks whether we generate a message if there is no message in the XML file.
     */
    @Test
    void handleFilesWithoutMessages() {
        Report report = parseFile("findbugs-nomessage.xml", CONFIDENCE);
        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasCategory("STYLE")
                .hasType("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE");
        assertThat(report.get(0).getMessage()).contains("Redundant nullcheck of");
    }

    /**
     * Verifies that third party categories are correctly parsed.
     */
    @Test
    void thirdPartyCategory() {
        Report report = parseFile("findbugs-3rd-party-category.xml", CONFIDENCE);
        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasCategory("BAD_PRACTICE").hasType("SE_NO_SERIALVERSIONID");
        assertThat(report.get(1)).hasCategory("SECURITY").hasType("WEAK_MESSAGE_DIGEST");
    }

    @SuppressWarnings("parameternumber")
    private void scanNativeFile(final String findbugsFile, final String projectName, final Priority priority,
            final String fileName1, final String packageName1,
            final int start1, final int end1, final int ranges1,
            final String fileName2, final String packageName2,
            final int start2, final int end2, final int ranges2,
            final PriorityProperty priorityProperty) {
        Report report = parseFile(findbugsFile, priorityProperty);
        assertThat(report.getModules()).containsExactly(projectName);
        assertThat(report).hasSize(2);

        Issue first = report.filter(Issue.byFileName(fileName1)).get(0);
        Issue second = report.filter(Issue.byFileName(fileName2)).get(0);

        assertSoftly(softly -> {
            softly.assertThat(first)
                    .hasFileName(fileName1)
                    .hasPackageName(packageName1)
                    .hasPriority(priority)
                    .hasModuleName(projectName)
                    .hasLineStart(start1)
                    .hasLineEnd(end1);
            softly.assertThat(first.getLineRanges()).hasSize(ranges1);
            softly.assertThat(second)
                    .hasFileName(fileName2)
                    .hasPackageName(packageName2)
                    .hasPriority(priority)
                    .hasModuleName(projectName)
                    .hasLineStart(start2)
                    .hasLineEnd(end2);
            softly.assertThat(second.getLineRanges()).hasSize(ranges2);
        });
    }

}
