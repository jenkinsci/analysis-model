package edu.hm.hafner.analysis;

import edu.hm.hafner.util.NoSuchElementException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static edu.hm.hafner.analysis.IssuesAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IssuesTest {

    @Test
    public void emptyIssues() {
        assertThat(new Issues()).hasSize(0);
    }

    @Test
    public void addIssueToEmptyIssues() {
        final Issues sut = new Issues();

        sut.add(new IssueBuilder().setFileName("asdf").build());
        assertThat(sut)
                .hasSize(1)
                .hasHighPrioritySize(0)
                .hasNormalPrioritySize(1)
                .hasLowPrioritySize(0);
        IssueAssert.assertThat(sut.get(0))
                .hasFileName("asdf");
    }

    @Test
    public void addNullToEmptyIssuesAndThrowNPE() {
        final Issues sut = new Issues();

        assertThatThrownBy(() -> sut.add(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addAllWithNullCollection() {
        assertThatThrownBy(() -> new Issues().addAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void addAllWithEmptyCollection() {
        final Issues sut = new Issues();

        sut.addAll(new ArrayList<Issue>());
        assertThat(sut).hasSize(0);
    }

    @Test
    public void addAllWithSingleElementCollection() {

        final Issues sut = new Issues();
        final Issue issue = new IssueBuilder().setFileName("asdf").build();
        final Collection<Issue> collection = new ArrayList<>();
        collection.add(issue);
        sut.addAll(collection);

        assertThat(sut).hasSize(1);
        IssueAssert.assertThat(sut.get(0)).hasFileName("asdf");
    }

    @Test
    public void removeWithNull() {
        final Issues sut = new Issues();

        assertThatThrownBy(() -> sut.remove(null))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void removeNotContainingIssue() {
        final Issues sut = new Issues();
        assertThat(sut).hasSize(0);
        assertThatThrownBy(() -> sut.remove(new IssueBuilder().build().getId()))
                .isInstanceOf(NoSuchElementException.class);
        assertThat(sut).hasSize(0);
    }

    @Test
    public void removeIssueAfterAddingIt() {
        final Issues sut = new Issues();
        final Issue issue = new IssueBuilder().setFileName("asdf").build();

        sut.add(issue);
        assertThat(sut).hasSize(1);
        final Issue removedIssue = sut.remove(issue.getId());
        assertThat(sut).hasSize(0);
        IssueAssert.assertThat(removedIssue).isEqualTo(issue);
    }

    @Test
    public void removeTenthIssue() {
        final Issues sut = new Issues();
        for (int i = 0; i < 9; i++) {
            sut.add(new IssueBuilder().build());
        }

        final Issue tenth = new IssueBuilder().setFileName("tenthFile").build();

        sut.add(tenth);
        assertThat(sut).hasSize(10);

        final Issue removedIssue = sut.remove(tenth.getId());

        IssueAssert.assertThat(removedIssue).isEqualTo(tenth);
        assertThat(sut).hasSize(9);
    }

    @Test
    public void findIdInEmptyIssues() {
        final Issues sut = new Issues();

        assertThatThrownBy(() -> sut.remove(new IssueBuilder().build().getId()))
                .isInstanceOf(NoSuchElementException.class);

    }

    @Test
    public void findNullIdInEmptyIssues() {
        final Issues sut = new Issues();

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThatThrownBy(() -> sut.remove(null))
                    .isInstanceOf(NoSuchElementException.class);
        });

    }

    @Test
    public void findIdInIssuesContainingASingleElement() {
        final Issues sut = new Issues();
        final Issue issue = new IssueBuilder().setFileName("asdf").build();

        sut.add(issue);
        assertThat(sut).hasSize(1);
        final Issue foundIssue = sut.findById(issue.getId());
        IssueAssert.assertThat(foundIssue).isEqualTo(issue);
    }

    @Test
    public void findTenthId() {
        final Issues sut = new Issues();
        for (int i = 0; i < 9; i++) {
            sut.add(new IssueBuilder().build());
        }

        final Issue tenth = new IssueBuilder().setFileName("tenthFile").build();

        sut.add(tenth);
        assertThat(sut).hasSize(10);

        final Issue foundIssue = sut.findById(tenth.getId());

        IssueAssert.assertThat(foundIssue).isEqualTo(tenth);
    }

    @Test
    public void containsNoHighNormalLowPriorities() {
        final Issues sut = new Issues();
        assertThat(sut)
                .hasHighPrioritySize(0)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0);
    }

    @Test
    public void containsOneOfAllPriorities() {
        final Issues sut = new Issues();
        final IssueBuilder builder = new IssueBuilder();
        sut.add(builder.setPriority(Priority.HIGH).build());
        sut.add(builder.setPriority(Priority.NORMAL).build());
        sut.add(builder.setPriority(Priority.LOW).build());

        assertThat(sut)
                .hasHighPrioritySize(1)
                .hasNormalPrioritySize(1)
                .hasLowPrioritySize(1);
    }

    @Test
    public void getTenthIssue() {
        final Issues sut = new Issues();
        for (int i = 0; i < 9; i++) {
            sut.add(new IssueBuilder().build());
        }

        final Issue tenth = new IssueBuilder().setFileName("tenthFile").build();
        sut.add(tenth);

        for (int i = 0; i < 9; i++) {
            sut.add(new IssueBuilder().build());
        }

        IssueAssert.assertThat(sut.get(9)).isEqualTo(tenth);
    }

    @Test
    public void stringRepresentationOfEmptyIssues() {
        final Issues sut = new Issues();

        assertThat(sut).hasToString("0 issues");
    }

    @Test
    public void stringRepresentationOfIssuesWithTenElements() {
        final Issues sut = new Issues();
        for (int i = 0; i < 10; i++) {
            sut.add(new IssueBuilder().build());
        }

        assertThat(sut).hasToString("10 issues");
    }
}