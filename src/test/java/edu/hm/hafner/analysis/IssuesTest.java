package edu.hm.hafner.analysis;

import com.google.common.collect.ImmutableSet;
import edu.hm.hafner.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

import java.util.*;

import static edu.hm.hafner.analysis.IssuesAssert.assertThat;
import static edu.hm.hafner.analysis.IssueAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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
        assertThat(sut.get(0))
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
        assertThat(sut.get(0)).hasFileName("asdf");
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
        assertThat(removedIssue).isEqualTo(issue);
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

        assertThat(removedIssue).isEqualTo(tenth);
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

        assertThatThrownBy(() -> sut.remove(null))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void findIdInIssuesContainingASingleElement() {
        final Issues sut = new Issues();
        final Issue issue = new IssueBuilder().setFileName("asdf").build();

        sut.add(issue);
        assertThat(sut).hasSize(1);
        final Issue foundIssue = sut.findById(issue.getId());
        assertThat(foundIssue).isEqualTo(issue);
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

        assertThat(foundIssue).isEqualTo(tenth);
    }
}