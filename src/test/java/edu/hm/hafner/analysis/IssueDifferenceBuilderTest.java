package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static java.util.Collections.*;

/**
 * Unit tests for {@link IssueDifferenceBuilder}.
 *
 * @author mbauerness
 */
class IssueDifferenceBuilderTest {
    private static final String REFERENCE_BUILD = "100";
    private static final String CURRENT_BUILD = "2";

    /**
     * Tests if the object from the builder and the one created directly have the same fields.
     */
    @Test
    void shouldHaveSameFields() {
        IssueDifference sut1 = new IssueDifferenceBuilder()
                .setCurrentIssues(createCurrentIssues())
                .setReferenceId(CURRENT_BUILD)
                .setReferenceIssues(createReferenceIssues())
                .build();
        IssueDifference sut2 = new IssueDifference(createCurrentIssues(), CURRENT_BUILD, createReferenceIssues());

        // Assertions for sut1
        Report outstandingSut1 = sut1.getOutstandingIssues();
        assertThat(outstandingSut1).hasSize(3);
        assertThat(outstandingSut1.get(0)).hasMessage("OUTSTANDING 2").hasReference(REFERENCE_BUILD);
        assertThat(outstandingSut1.get(1)).hasMessage("OUTSTANDING 3").hasReference(REFERENCE_BUILD);
        assertThat(outstandingSut1.get(2)).hasMessage("UPD OUTSTANDING 1").hasReference(REFERENCE_BUILD);

        Report fixedSut1 = sut1.getFixedIssues();
        assertThat(fixedSut1).hasSize(2);
        assertThat(fixedSut1.get(0)).hasMessage("TO FIX 1").hasReference(REFERENCE_BUILD);
        assertThat(fixedSut1.get(1)).hasMessage("TO FIX 2").hasReference(REFERENCE_BUILD);

        assertThat(sut1.getNewIssues()).hasSize(1);
        assertThat(sut1.getNewIssues().get(0)).hasMessage("NEW 1").hasReference("2");

        // Assertions for sut2
        Report outstandingSut2 = sut2.getOutstandingIssues();
        assertThat(outstandingSut2).hasSize(3);
        assertThat(outstandingSut2.get(0)).hasMessage("OUTSTANDING 2").hasReference(REFERENCE_BUILD);
        assertThat(outstandingSut2.get(1)).hasMessage("OUTSTANDING 3").hasReference(REFERENCE_BUILD);
        assertThat(outstandingSut2.get(2)).hasMessage("UPD OUTSTANDING 1").hasReference(REFERENCE_BUILD);

        Report fixedSut2 = sut2.getFixedIssues();
        assertThat(fixedSut2).hasSize(2);
        assertThat(fixedSut2.get(0)).hasMessage("TO FIX 1").hasReference(REFERENCE_BUILD);
        assertThat(fixedSut2.get(1)).hasMessage("TO FIX 2").hasReference(REFERENCE_BUILD);

        assertThat(sut2.getNewIssues()).hasSize(1);
        assertThat(sut2.getNewIssues().get(0)).hasMessage("NEW 1").hasReference("2");
    }

    @Test
    void shouldThrowNullpointerBecauseCurrentIssuesNull() {
        assertThatThrownBy(() -> new IssueDifferenceBuilder()
                .setCurrentIssues(null)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullpointerBecauseReferenceIssuesNull() {
        assertThatThrownBy(() -> new IssueDifferenceBuilder()
                .setReferenceIssues(null)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowNullpointerBecauseReferenceIdNull() {
        assertThatThrownBy(() -> new IssueDifferenceBuilder()
                .setReferenceId(null)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowIllegalStateBecauseCurrentIssuesNotSet() {
        assertThatThrownBy(() -> new IssueDifferenceBuilder()
                .setReferenceIssues(createReferenceIssues())
                .setReferenceId(REFERENCE_BUILD)
                .build())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateBecauseReferenceIssuesNotSet() {
        assertThatThrownBy(() -> new IssueDifferenceBuilder()
                .setCurrentIssues(createCurrentIssues())
                .setReferenceId(REFERENCE_BUILD)
                .build())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateBecauseReferenceIdNotSet() {
        assertThatThrownBy(() -> new IssueDifferenceBuilder()
                .setCurrentIssues(createCurrentIssues())
                .setReferenceIssues(createReferenceIssues())
                .build())
                .isInstanceOf(IllegalStateException.class);
    }

    private Issue createIssue(final String message, final String fingerprint) {
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName("file-name")
                .setLineStart(1)
                .setLineEnd(2)
                .setColumnStart(3)
                .setColumnEnd(4)
                .setCategory("category")
                .setType("type")
                .setPackageName("package-name")
                .setModuleName("module-name")
                .setSeverity(Severity.WARNING_HIGH)
                .setMessage(message)
                .setDescription("description")
                .setOrigin("origin")
                .setLineRanges(new LineRangeList(singletonList(new LineRange(5, 6))))
                .setFingerprint(fingerprint)
                .setReference(REFERENCE_BUILD);
        return builder.build();
    }

    private Report createReferenceIssues() {
        return new Report().addAll(
                createIssue("OUTSTANDING 1", "OUT 1"),
                createIssue("OUTSTANDING 2", "OUT 2"),
                createIssue("OUTSTANDING 3", "OUT 3"),
                createIssue("TO FIX 1", "FIX 1"),
                createIssue("TO FIX 2", "FIX 2"));
    }

    private Report createCurrentIssues() {
        return new Report().addAll(
                createIssue("UPD OUTSTANDING 1", "OUT 1"),
                createIssue("OUTSTANDING 2", "UPD OUT 2"),
                createIssue("OUTSTANDING 3", "OUT 3"),
                createIssue("NEW 1", "NEW 1"));
    }
}
