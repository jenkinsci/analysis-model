package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GenericIssueFilterTest {

    private static final String FILENAME_1 = "ABC";
    private static final String FILENAME_2 = "DEF";

    private static final Issue ISSUE_1 = new IssueBuilder()
            .setPriority(Priority.HIGH)
            .setFileName(FILENAME_1)
            .build();

    private static final Issue ISSUE_2 = new IssueBuilder()
            .setPriority(Priority.LOW)
            .setFileName(FILENAME_2)
            .build();

    @Test
    void shouldIncludeMatchingIssueWithMatchingStringProperty() {
        IssueFilter filter = new GenericIssueFilter<>(Issue::getFileName, FILENAME_1::equals);
        assertThat(filter.isIncluded(ISSUE_1))
                .isTrue();

        assertThat(filter.isIncluded(ISSUE_2))
                .isFalse();
    }

    @Test
    void shouldMatchingIssueWithMatchingPriorityProperty() {
        IssueFilter filter = new GenericIssueFilter<>(Issue::getPriority, Priority.LOW::equals);

        assertThat(filter.isIncluded(ISSUE_1))
                .isFalse();

        assertThat(filter.isIncluded(ISSUE_2))
                .isTrue();
    }
}