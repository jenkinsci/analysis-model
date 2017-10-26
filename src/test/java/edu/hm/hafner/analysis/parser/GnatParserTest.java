package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueAssertSoft;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.IssuesAssert;
import edu.hm.hafner.analysis.Priority;

/**
 * Tests the class {@link GnatParser}.
 */
public class GnatParserTest extends ParserTester {
    private static final String TYPE = new GnatParser().getId();
    private static final String GNAT_WARNING = "GNAT warning";

    /**
     * Parses a file with 9 Gnat warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Issues warnings = new GnatParser().parse(openFile());
        IssuesAssert.assertThat(warnings).hasSize(9);

        Iterator<Issue> iterator = warnings.iterator();
        IssueAssertSoft softly = new IssueAssertSoft();

        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/utilities/class_utilities.adb:402:23:
        // warning: call to obsolescent procedure "Very_Verbose" declared at
        // debug.ads:59
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(402)
                .hasLineEnd(402)
                .hasMessage("call to obsolescent procedure \"Very_Verbose\" declared at debug.ads:59")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/utilities/class_utilities.adb")
                .hasType(TYPE);
        softly.assertAll();


        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/utilities/iml-interfaces-cfg.adb:63:14:
        // warning: variable "E" is not referenced
       softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(63)
                .hasLineEnd(63)
                .hasMessage("variable \"E\" is not referenced")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/utilities/iml-interfaces-cfg.adb")
                .hasType(TYPE);
        softly.assertAll();

        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/pointsto/andersen_results.adb:96:80:
        // (style) this line is too long
        softly.assertThat(iterator.next())
                .hasPriority(Priority.LOW)
                .hasCategory("GNAT style")
                .hasLineStart(96)
                .hasLineEnd(96)
                .hasMessage("this line is too long")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/pointsto/andersen_results.adb")
                .hasType(TYPE);
        softly.assertAll();

        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/generated/ada_delta_constraints.adb:3:06:
        // warning: redundant with clause in body
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("redundant with clause in body")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/generated/ada_delta_constraints.adb")
                .hasType(TYPE);
        softly.assertAll();

        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/array_tables.adb:97:07:
        // warning: variable "Dummy_Empty_Array_Item" is read but never assigned
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(97)
                .hasLineEnd(97)
                .hasMessage("variable \"Dummy_Empty_Array_Item\" is read but never assigned")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/array_tables.adb")
                .hasType(TYPE);
        softly.assertAll();

        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/graph_algorithms-generic_explorers.adb:63:14:
        // warning: "C" is not modified, could be declared constant
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(GNAT_WARNING)
                .hasLineStart(63)
                .hasLineEnd(63)
                .hasMessage("\"C\" is not modified, could be declared constant")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/graph_algorithms-generic_explorers.adb")
                .hasType(TYPE);
        softly.assertAll();

        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/process_data.adb:257:49:
        // (style) bad casing of "False" declared in Standard
        softly.assertThat(iterator.next())
                .hasPriority(Priority.LOW)
                .hasCategory("GNAT style")
                .hasLineStart(257)
                .hasLineEnd(257)
                .hasMessage("bad casing of \"False\" declared in Standard")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/libs/reuse/src/process_data.adb")
                .hasType(TYPE);
        softly.assertAll();

        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/tools/scangen/src/scangen.adb:23:35:
        // error: binary operator expected
        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("GNAT error")
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("binary operator expected")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/tools/scangen/src/scangen.adb")
                .hasType(TYPE);
        softly.assertAll();

        // /home/bergerbd/.hudson/jobs/Test/workspace/projects/tools/scangen/src/scangen.adb:23:36:
        // error: identifier cannot start with underline
        softly.assertThat(iterator.next())
                .hasPriority(Priority.HIGH)
                .hasCategory("GNAT error")
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("identifier cannot start with underline")
                .hasFileName("/home/bergerbd/.hudson/jobs/Test/workspace/projects/tools/scangen/src/scangen.adb")
                .hasType(TYPE);
        softly.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "gnat.txt";
    }
}

