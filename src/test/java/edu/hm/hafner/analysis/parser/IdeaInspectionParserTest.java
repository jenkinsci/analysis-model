package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;

/**
 * Tests {@link IdeaInspectionParser } parser class.
 *
 * @author Alex Lopashev, alexlopashev@gmail.com
 */
public class IdeaInspectionParserTest extends ParserTester {
    /**
     * Parses an example file with single inspection.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parse() throws IOException {
        Issues inspections = new IdeaInspectionParser().parse(openFile());

        assertThat(inspections).hasSize(1);


        SoftAssertions.assertSoftly(softly -> {

            softly.assertThat(inspections.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("Unused method parameters")
                    .hasLineStart(42)
                    .hasLineEnd(42)
                    .hasMessage("Parameter <code>intentionallyUnusedString</code> is not used  in either this method or any of its derived methods")
                    .hasFileName("file://$PROJECT_DIR$/src/main/java/org/lopashev/Test.java");


        });
    }

    @Override
    protected String getWarningsFile() {
        return "IdeaInspectionExample.xml";
    }
}

