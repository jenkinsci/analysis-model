package edu.hm.hafner.analysis.parser;

import java.io.Reader;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static edu.hm.hafner.analysis.parser.ParserTester.DEFAULT_CATEGORY;


/**
 * Tests the class {@link BuckminsterParser}.
 */
public class BuckminsterParserTest extends AbstractParserTest {
    private static final String CATEGORY = DEFAULT_CATEGORY;

    protected BuckminsterParserTest(){
        super ("buckminster.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(3);

        softly.assertThat(issues.get(0)).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(43)
                .hasLineEnd(43)
                .hasMessage("ArrayList is a raw type. References to generic type ArrayList<E> should be parameterized")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java");
        softly.assertThat(issues.get(1)).hasPriority(Priority.HIGH)
                .hasCategory(CATEGORY)
                .hasLineStart(57)
                .hasLineEnd(57)
                .hasMessage("Type safety: The method toArray(Object[]) belongs to the raw type ArrayList. References to generic type ArrayList<E> should be parameterized")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java");
        softly.assertThat(issues.get(2)).hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace that are strictly compatible with this environment.")
                .hasFileName("/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp");
    }

    @Override
    protected AbstractParser createParser() {
        return new BuckminsterParser();
    }
}

