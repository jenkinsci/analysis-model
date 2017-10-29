package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertions.IssueSoftAssertions;
import static edu.hm.hafner.analysis.assertions.IssuesAssert.*;

/**
 * Tests the class {@link IarParser}.
 *
 * @author Ullrich Hafner
 */
public class IarParserTest extends ParserTester {
    private static final String TYPE = new IarParser().getId();

    /**
     * Parses a file with warnings/errors in all styles. it check the amount of error/warnings found
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
    @Test
    public void IAR_error_size() throws IOException {
        Issues warnings = new IarParser().parse(openFile("issue8823.txt"));
        assertThat(warnings).hasSize(6);
    }

    /**
     * Parses a file and check error number 1
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
    @Test
    public void IAR_error1() throws IOException {
        Issue annotation = getErrorNumber(1);
        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Pe188")
                    .hasLineStart(3767)
                    .hasLineEnd(3767)
                    .hasMessage("enumerated type mixed with another type")
                    .hasFileName("D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c");
        });


    }

    /**
     * Parses a file and check error number 2
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
    @Test
    public void IAR_error2() throws IOException {
        Issue annotation = getErrorNumber(2);
        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Pe188")
                    .hasLineStart(3767)
                    .hasLineEnd(3767)
                    .hasMessage("enumerated type mixed with another type")
                    .hasFileName("D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c");
        });


    }

    /**
     * Parses a file and check error number 3
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
    @Test
    public void IAR_error3() throws IOException {
        Issue annotation = getErrorNumber(3);
        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Pe188")
                    .hasLineStart(3918)
                    .hasLineEnd(3918)
                    .hasMessage("enumerated type mixed with another type")
                    .hasFileName("D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c");
        });


    }

    /**
     * Parses a file and check error number 4
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
    @Test
    public void IAR_error4() throws IOException {
        Issue annotation = getErrorNumber(4);
        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Pe1696")
                    .hasLineStart(17)
                    .hasLineEnd(17)
                    .hasMessage("cannot open source file \"System/ProcDef_LPC17xx.h\"")
                    .hasFileName("c:/JenkinsJobs/900ZH/Workspace/Product.900ZH/Src/System/AdditionalResources.h");
        });


    }

    /**
     * Parses a file and check error number 5
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
    @Test
    public void IAR_error5() throws IOException {
        Issue annotation = getErrorNumber(5);
        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Pe177")
                    .hasLineStart(43)
                    .hasLineEnd(43)
                    .hasMessage("variable \"pgMsgEnv\" was declared but never referenced")
                    .hasFileName("C:/dev/bsc/daqtask.c");
        });


    }

    /**
     * Parses a file and check error number 6
     *
     * @throws IOException if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
    @Test
    public void IAR_error6() throws IOException {
        Issue annotation = getErrorNumber(6);
        // the \" is needed
        IssueSoftAssertions.assertIssueSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.HIGH)
                    .hasCategory("Pe1696")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("cannot open source file \"c:\\JenkinsJobs\\900ZH\\Workspace\\Lib\\Drivers\\_Obsolete\\Uart\\UartInterface.c\"")
                    .hasFileName("\"c:/JenkinsJobs/900ZH/Workspace/Lib/Drivers/_Obsolete/Uart/UartInterface.c\"");
        });


    }

    @Override
    protected String getWarningsFile() {
        return "iar-nowrap.log";
    }

    protected Issue getErrorNumber(final int number) throws IOException {
        Issues warnings = new IarParser().parse(openFile("issue8823.txt"));
        Iterator<Issue> iterator = warnings.iterator();
        Issue annotation = iterator.next();

        for (int i = 1; i < number; i++) {
            annotation = iterator.next();
        }

        return annotation;
    }

}