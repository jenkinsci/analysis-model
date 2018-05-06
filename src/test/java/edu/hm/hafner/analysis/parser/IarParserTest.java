package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link IarParser}.
 *
 * @author Ullrich Hafner
 */
class IarParserTest extends AbstractIssueParserTest {
    IarParserTest() {
        super("issue8823.txt");
    }

    @Override
    protected IarParser createParser() {
        return new IarParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5).hasDuplicatesSize(1);

        softly.assertThat(report.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory("Pe188")
                .hasLineStart(3767)
                .hasLineEnd(3767)
                .hasMessage("enumerated type mixed with another type")
                .hasFileName("D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c");
        softly.assertThat(report.get(1))
                .hasPriority(Priority.NORMAL)
                .hasCategory("Pe188")
                .hasLineStart(3918)
                .hasLineEnd(3918)
                .hasMessage("enumerated type mixed with another type")
                .hasFileName("D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c");
        softly.assertThat(report.get(2))
                .hasPriority(Priority.HIGH)
                .hasCategory("Pe1696")
                .hasLineStart(17)
                .hasLineEnd(17)
                .hasMessage("cannot open source file \"System/ProcDef_LPC17xx.h\"")
                .hasFileName("c:/JenkinsJobs/900ZH/Workspace/Product.900ZH/Src/System/AdditionalResources.h");
        softly.assertThat(report.get(3))
                .hasPriority(Priority.NORMAL)
                .hasCategory("Pe177")
                .hasLineStart(43)
                .hasLineEnd(43)
                .hasMessage("variable \"pgMsgEnv\" was declared but never referenced")
                .hasFileName("C:/dev/bsc/daqtask.c");
        softly.assertThat(report.get(4))
                .hasPriority(Priority.HIGH)
                .hasCategory("Pe1696")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("cannot open source file \"c:\\JenkinsJobs\\900ZH\\Workspace\\Lib\\Drivers\\_Obsolete\\Uart\\UartInterface.c\"")
                .hasFileName("\"c:/JenkinsJobs/900ZH/Workspace/Lib/Drivers/_Obsolete/Uart/UartInterface.c\"");
    }
}