package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.parser.ccm.CcmParser;

/**
 * Tests CCMParser.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 */
class CcmParserTest extends AbstractIssueParserTest {
    CcmParserTest() {
        super("pynamodb_ccm_results_sample.xml");
    }

    @Override
    protected AbstractParser createParser() {
        return new CcmParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        int expectedNumberOfLowPriority = 1;
        int expectedNumberOfNormalPriority = 1;
        int expectedNumberOfHighPriority = 4;
        int numberOfLowPriorityFound = 0;
        int numberOfNormalPriorityFound = 0;
        int numberOfHighPriorityFound = 0;
        for (Issue issue : report) {
            String priority = issue.getSeverity().toString().toLowerCase();
            if ("low".equals(priority)) {
                numberOfLowPriorityFound++;
            }
            else if ("normal".equals(priority)) {
                numberOfNormalPriorityFound++;
            }
            else if ("high".equals(priority)) {
                numberOfHighPriorityFound++;
            }
        }

        softly.assertThat(numberOfLowPriorityFound).isEqualTo(expectedNumberOfLowPriority);
        softly.assertThat(numberOfNormalPriorityFound).isEqualTo(expectedNumberOfNormalPriority);
        softly.assertThat(numberOfHighPriorityFound).isEqualTo(expectedNumberOfHighPriority);
    }
}
