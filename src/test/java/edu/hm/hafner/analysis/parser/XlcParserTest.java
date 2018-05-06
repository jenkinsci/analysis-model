package edu.hm.hafner.analysis.parser;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link XlcParserTest}.
 */
class XlcParserTest {
    private static final String FILE_NAME = "-";

    /**
     * Parses a string with xlC error.
     */
    @Test
    void testWarningsParserError() {
        Report warnings = parseString("\"file.c\", line 9.17: 1506-098 (E) Missing argument(s).");

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("1506-098")
                    .hasLineStart(9)
                    .hasLineEnd(9)
                    .hasMessage("Missing argument(s).")
                    .hasFileName("file.c");
        });
    }

    /**
     * Parses a string with xlC error.
     */
    @Test
    void testWarningsParserSevereError() {
        Report warnings = parseString("file.c, line 11.18: 1506-189 (S) Floating point constant 10.23.3 is not valid");

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("1506-189")
                    .hasLineStart(11)
                    .hasLineEnd(11)
                    .hasMessage("Floating point constant 10.23.3 is not valid")
                    .hasFileName("file.c");
        });
    }

    /**
     * Parses a string with xlC error in z/OS message format.
     */
    @Test
    void testWarningsParserSevereErrorZOS() {
        Report warnings = parseString("\"./Testapi.cpp\", line 4000.22: CCN5217 (S) \"AEUPD_RQ_UPDT\" is not a member of \"struct AEUPD_RQ\".");

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("CCN5217")
                    .hasLineStart(4000)
                    .hasLineEnd(4000)
                    .hasMessage("\"AEUPD_RQ_UPDT\" is not a member of \"struct AEUPD_RQ\".")
                    .hasFileName("./Testapi.cpp");
        });
    }

    /**
     * Parses a string with xlC unrecoverable error.
     */
    @Test
    void testWarningsParserUnrecoverableError() {
        Report warnings2 = parseString("file.c, line 5.1: 1506-001 (U) INTERNAL COMPILER ERROR");

        assertSoftly(softly -> {
            softly.assertThat(warnings2.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("1506-001")
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("INTERNAL COMPILER ERROR")
                    .hasFileName("file.c");
        });
        
        Report warnings1 = parseString("1586-346 (U) An error occurred during code generation.  The code generation return code was 1.");

        assertSoftly(softly -> {
            softly.assertThat(warnings1.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("1586-346")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("An error occurred during code generation.  The code generation return code was 1.")
                    .hasFileName(FILE_NAME);
        });
        
        Report warnings = parseString("    1500-004: (U) INTERNAL COMPILER ERROR while compiling ----.  Compilation ended.  Contact your Service Representative and provide the following information: Internal abort. For more information visit: http://www.ibm.com/support/docview.wss?uid=swg21110810");

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.HIGH)
                    .hasCategory("1500-004")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("INTERNAL COMPILER ERROR while compiling ----.  Compilation ended.  Contact your Service Representative and provide the following information: Internal abort. For more information visit: http://www.ibm.com/support/docview.wss?uid=swg21110810")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC warning.
     */
    @Test
    void testWarningsParserWarning() {
        Report warnings = parseString("file.c, line 5.9: 1506-304 (W) No function prototype given for \"printf\".");

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("1506-304")
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasMessage("No function prototype given for \"printf\".")
                    .hasFileName("file.c");
        });
    }

    /**
     * Parses a string with xlC warning message in z/OS format.
     */
    @Test
    void testWarningsParserWarningZOS() {
        Report warnings1 = parseString("\"./Testapi.cpp\", line 130.13: CCN5053 (W) The declaration of a class member within the class definition must not be qualified.");

        assertSoftly(softly -> {
            softly.assertThat(warnings1.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("CCN5053")
                    .hasLineStart(130)
                    .hasLineEnd(130)
                    .hasMessage("The declaration of a class member within the class definition must not be qualified.")
                    .hasFileName("./Testapi.cpp");
        });
        
        Report warnings = parseString("CCN7504(W) \"//''\" is not a valid suboption for \"SEARCH\".  The option is ignored.");

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("CCN7504")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("\"//''\" is not a valid suboption for \"SEARCH\".  The option is ignored.")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC informational message.
     */
    @Test
    void testWarningsParserInfo() {
        Report warnings2 = parseString("file.c, line 12.9: 1506-478 (I) The then branch of conditional is an empty statement.");

        assertSoftly(softly -> {
            softly.assertThat(warnings2.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("1506-478")
                    .hasLineStart(12)
                    .hasLineEnd(12)
                    .hasMessage("The then branch of conditional is an empty statement.")
                    .hasFileName("file.c");
        });
        
        Report warnings1 = parseString("    1500-030: (I) INFORMATION: clazz::fun(): Additional optimization may be attained by recompiling and specifying MAXMEM option with a value greater than 8192.");

        assertSoftly(softly -> {
            softly.assertThat(warnings1.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("1500-030")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage(
                            "clazz::fun(): Additional optimization may be attained by recompiling and specifying MAXMEM option with a value greater than 8192.")
                    .hasFileName(FILE_NAME);
        });
        
        Report warnings = parseString("1540-5336 (I) Global variable \"__td __td__Q2_3std13runtime_error\" is not used.");

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("1540-5336")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Global variable \"__td __td__Q2_3std13runtime_error\" is not used.")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC informational message in z/OS format.
     */
    @Test
    void testWarningsParserInfoZOS1() {
        Report warnings1 = parseString(
                "\"./Testapi.cpp\", line 372.8: CCN6283 (I) \"Testapi::Test(long, long)\" is not a viable candidate.");

        assertSoftly(softly -> {
            softly.assertThat(warnings1.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("CCN6283")
                    .hasLineStart(372)
                    .hasLineEnd(372)
                    .hasMessage("\"Testapi::Test(long, long)\" is not a viable candidate.")
                    .hasFileName("./Testapi.cpp");
        });
        
        Report warnings = parseString("CCN8151(I) The option \"TARGET(0x410D0000)\" sets \"ARCH(5)\".");

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.LOW)
                    .hasCategory("CCN8151")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("The option \"TARGET(0x410D0000)\" sets \"ARCH(5)\".")
                    .hasFileName(FILE_NAME);
        });
    }

    private Report parseString(final String log) {
        Report warnings = new XlcCompilerParser().parse(new StringReader(log));

        assertThat(warnings).hasSize(1);
        
        return warnings;
    }
}

