package edu.hm.hafner.analysis;

/**
 * Base class for tests of {@link AbstractParser} instances.
 *
 * @author Ullrich Hafner
 */
public abstract class AbstractIssueParserTest extends AbstractParserTest {
   /**
     * Creates a new instance of {@link AbstractIssueParserTest}.
     *
     * @param fileWithIssuesName
     *         the file that should contain some issues
     */
    protected AbstractIssueParserTest(final String fileWithIssuesName) {
        super(fileWithIssuesName);
    }
}
