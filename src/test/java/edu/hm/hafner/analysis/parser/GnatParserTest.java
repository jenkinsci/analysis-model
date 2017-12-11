package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link GnatParser}.
 */
public class GnatParserTest extends ParserTester {
    private static final String GNAT_WARNING = "GNAT warning";

    /**
     * Parses a file with 9 Gnat warnings.
     */
    @Test
    public void testWarningsParser() {
        Issues<Issue> warnings = new GnatParser().parse(openFile());
        assertThat(warnings).hasSize(9);

        Iterator<Issue> iterator = warnings.iterator();

        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(GNAT_WARNING)
                    .hasLineStart(402)
                    .hasLineEnd(402)
                    .hasMessage("call to obsolescent procedure \"Very_Verbose\" declared at debug.ads:59")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/utilities/class_utilities.adb");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(GNAT_WARNING)
                    .hasLineStart(63)
                    .hasLineEnd(63)
                    .hasMessage("variable \"E\" is not referenced")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/utilities/iml-interfaces-cfg.adb");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.LOW)
                    .hasCategory("GNAT style")
                    .hasLineStart(96)
                    .hasLineEnd(96)
                    .hasMessage("this line is too long")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/pointsto/andersen_results.adb");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(GNAT_WARNING)
                    .hasLineStart(3)
                    .hasLineEnd(3)
                    .hasMessage("redundant with clause in body")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/generated/ada_delta_constraints.adb");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(GNAT_WARNING)
                    .hasLineStart(97)
                    .hasLineEnd(97)
                    .hasMessage("variable \"Dummy_Empty_Array_Item\" is read but never assigned")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/array_tables.adb");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(GNAT_WARNING)
                    .hasLineStart(63)
                    .hasLineEnd(63)
                    .hasMessage("\"C\" is not modified, could be declared constant")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/graph_algorithms-generic_explorers.adb");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.LOW)
                    .hasCategory("GNAT style")
                    .hasLineStart(257)
                    .hasLineEnd(257)
                    .hasMessage("bad casing of \"False\" declared in Standard")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/process_data.adb");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.HIGH)
                    .hasCategory("GNAT error")
                    .hasLineStart(23)
                    .hasLineEnd(23)
                    .hasMessage("binary operator expected")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/tools/scangen/src/scangen.adb");

            softly.assertThat(iterator.next())
                    .hasPriority(Priority.HIGH)
                    .hasCategory("GNAT error")
                    .hasLineStart(23)
                    .hasLineEnd(23)
                    .hasMessage("identifier cannot start with underline")
                    .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/tools/scangen/src/scangen.adb");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "gnat.txt";
    }
}

