package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests CCMParser.
 *
 * @author Bruno P. Kinoshita - <a href="http://www.kinoshita.eti.br">...</a>
 */
class CcmParserTest extends AbstractParserTest {
    CcmParserTest() {
        super("pynamodb_ccm_results_sample.xml");
    }

    @Override
    protected CcmParser createParser() {
        return new CcmParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        int numberOfLowPriorityFound = 0;
        int numberOfNormalPriorityFound = 0;
        int numberOfHighPriorityFound = 0;
        for (Issue issue : report) {
            if (issue.getSeverity().equals(Severity.WARNING_LOW)) {
                numberOfLowPriorityFound++;
            }
            else if (issue.getSeverity().equals(Severity.WARNING_NORMAL)) {
                numberOfNormalPriorityFound++;
            }
            else if (issue.getSeverity().equals(Severity.WARNING_HIGH)) {
                numberOfHighPriorityFound++;
            }
        }

        softly.assertThat(numberOfLowPriorityFound).isEqualTo(1);
        softly.assertThat(numberOfNormalPriorityFound).isEqualTo(1);
        softly.assertThat(numberOfHighPriorityFound).isEqualTo(4);
    }
}
