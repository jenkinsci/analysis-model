package edu.hm.hafner.edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Priority;

@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
public class IssueTest {
    private static final String UNDEFINED = "-";

    @Test
    public void checkIfAllEmptyVarsWillConvertedToDefaultValue(){
        // Create a object which forces all default values
        Issue issue = new IssueBuilder()
                .setFileName("")
                .setLineStart(-1)
                .setLineEnd(-1)
                .setColumnStart(-1)
                .setColumnEnd(-1)
                .setCategory(null)
                .setType("")
                .setPackageName("")
                .setPriority(null)
                .setMessage(null)
                .setDescription(null)
                .build();

        // Check
        IssueAssert.assertThat(issue)
                .hasFileName(UNDEFINED)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0)
                .hasCategory("")
                .hasType(UNDEFINED)
                .hasPackagename(UNDEFINED)
                .hasPriority(Priority.NORMAL)
                .hasMessage("")
                .hasDescription("");
    }


}
