package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link GnatParser}.
 */
class GnatParserTest extends AbstractParserTest {
    private static final String GNAT_WARNING = "GNAT warning";

    GnatParserTest() {
        super("gnat.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        Iterator<Issue> iterator = report.iterator();
        softly.assertThat(report).hasSize(9);
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(402)
                .hasLineEnd(402)
                .hasMessage("call to obsolescent procedure \"Very_Verbose\" declared at debug.ads:59")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/utilities/class_utilities.adb");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(63)
                .hasLineEnd(63)
                .hasMessage("variable \"E\" is not referenced")
                .hasFileName(
                        "/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/utilities/iml-interfaces-cfg.adb");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("GNAT style")
                .hasLineStart(96)
                .hasLineEnd(96)
                .hasMessage("this line is too long")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/pointsto/andersen_results.adb");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("redundant with clause in body")
                .hasFileName(
                        "/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/generated/ada_delta_constraints.adb");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(97)
                .hasLineEnd(97)
                .hasMessage("variable \"Dummy_Empty_Array_Item\" is read but never assigned")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/array_tables.adb");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(63)
                .hasLineEnd(63)
                .hasMessage("\"C\" is not modified, could be declared constant")
                .hasFileName(
                        "/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/graph_algorithms-generic_explorers.adb");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("GNAT style")
                .hasLineStart(257)
                .hasLineEnd(257)
                .hasMessage("bad casing of \"False\" declared in Standard")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/process_data.adb");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("GNAT error")
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("binary operator expected")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/tools/scangen/src/scangen.adb");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("GNAT error")
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("identifier cannot start with underline")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/tools/scangen/src/scangen.adb");
    }

    @Override
    protected GnatParser createParser() {
        return new GnatParser();
    }
}

