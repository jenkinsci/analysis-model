package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link IarParser}.
 *
 * @author Ullrich Hafner
 * @author Jon Ware
 */
class IarParserTest extends AbstractParserTest {
    IarParserTest() {
        super("issue8823.txt");
    }

    @Override
    protected IarParser createParser() {
        return new IarParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6).hasDuplicatesSize(1);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Pe188")
                .hasLineStart(3767)
                .hasLineEnd(3767)
                .hasMessage("enumerated type mixed with another type")
                .hasFileName("D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Pe188")
                .hasLineStart(3918)
                .hasLineEnd(3918)
                .hasMessage("enumerated type mixed with another type")
                .hasFileName("D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Pe1696")
                .hasLineStart(17)
                .hasLineEnd(17)
                .hasMessage("cannot open source file \"System/ProcDef_LPC17xx.h\"")
                .hasFileName("c:/JenkinsJobs/900ZH/Workspace/Product.900ZH/Src/System/AdditionalResources.h");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Pe177")
                .hasLineStart(43)
                .hasLineEnd(43)
                .hasMessage("variable \"pgMsgEnv\" was declared but never referenced")
                .hasFileName("C:/dev/bsc/daqtask.c");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("Pe1696")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("cannot open source file \"c:\\JenkinsJobs\\900ZH\\Workspace\\Lib\\Drivers\\_Obsolete\\Uart\\UartInterface.c\"")
                .hasFileName("-");
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("Pe177")
                .hasLineStart(861)
                .hasLineEnd(861)
                .hasMessage("function \"FlashErase\" was declared but never referenced")
                .hasFileName("d:/jenkins/workspace/Nightly/src/flash/flashdrv.c");
}
    /**
     * Parses a warning log with IAR ARM warnings.
     *
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-55750">Issue 55750</a>
     */
    @Test
    void issue55750() {
        Report warnings = parse("issue55750.txt");
        assertThat(warnings).hasSize(4);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Pe852")
                    .hasLineStart(432)
                    .hasLineEnd(432)
                    .hasMessage("expression must be a pointer to a complete object type")
                    .hasFileName("c:/external/specific/cpp/iar_cxxabi.cpp");
            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Pe549")
                    .hasLineStart(81)
                    .hasLineEnd(81)
                    .hasMessage("variable \"result\" is used before its value is set")
                    .hasFileName("external/specific/wiced/WICED/security/BESL/host/WICED/wiced_p2p.c");
            softly.assertThat(warnings.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("Pe223")
                    .hasLineStart(412)
                    .hasLineEnd(412)
                    .hasMessage("function \"memcpy\" declared implicitly")
                    .hasFileName("external/specific/wiced/WICED/platform/MCU/RTOS_STM32F4xx/platform_dct_external.c");
            softly.assertThat(warnings.get(3))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasCategory("Pe018")
                    .hasLineStart(633)
                    .hasLineEnd(633)
                    .hasMessage("expected a \")\"")
                    .hasFileName("source/dal/InterMcu/InterMcuTransport.cpp");
        });
    }
}
