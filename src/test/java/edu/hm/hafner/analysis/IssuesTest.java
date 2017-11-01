package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;

import static edu.hm.hafner.analysis.assertj.CustomAssertions.assertThat;
import static org.assertj.core.api.Assertions.*;


class IssuesTest {


    private static final Issue ISSUE_HIGH = new IssueBuilder()
            .setMessage("ISSUE HIGH")
            .setFileName("high.txt")
            .setPriority(Priority.HIGH)
            .build();

    private static final Issue ISSUE_NORMAL = new IssueBuilder()
            .setMessage("ISSUE NORMAL")
            .setFileName("normal.txt")
            .setPriority(Priority.NORMAL)
            .build();

    private static final Issue ISSUE_LOW = new IssueBuilder()
            .setMessage("ISSUE LOW")
            .setFileName("low.txt")
            .setPriority(Priority.LOW)
            .build();

    private Issues sut = null;

    @BeforeEach
    public void Setup() {
        sut = new Issues();
    }

    @Test
    public void shouldAppendElementAndUpdateHighPriority() {
        sut.add(ISSUE_HIGH);

        assertThat(sut)
                .hasIssueAt(0, ISSUE_HIGH)
                .hasNoLowPriorityIssues()
                .hasNoNormalPriorityIssues()
                .hasHighPriorityIssues(1);
    }

    @Test
    public void shouldAppendElementAndUpdateNormalPrioritySize() {
        sut.add(ISSUE_NORMAL);

        assertThat(sut)
                .hasIssueAt(0, ISSUE_NORMAL)
                .hasNoLowPriorityIssues()
                .hasNoHighPriorityIssues()
                .hasNormalPriorityIssues(1);
    }

    @Test
    public void shouldAppendElementAndUpdateLowPrioritySize() {
        sut.add(ISSUE_LOW);

        assertThat(sut)
                .hasIssueAt(0, ISSUE_LOW)
                .hasNoHighPriorityIssues()
                .hasNoNormalPriorityIssues()
                .hasLowPriorityIssues(1);
    }

    @Test
    public void shouldReturnSameRefWhenAdded() {
        Issue actual = sut.add(ISSUE_LOW);

        assertThat(actual)
                .isSameAs(ISSUE_LOW);
    }

    @Test
    public void shouldAddAllItemsWhenCollectionPopulated() {
        int expectedNumberOfIssues = 3;
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_HIGH, ISSUE_NORMAL));

        assertThat(sut)
            .hasIssues(ISSUE_LOW, ISSUE_HIGH, ISSUE_NORMAL);
    }

    @Test
    public void shouldNotAddAnyWhenAddedCollectionEmpty() {
        sut.addAll(new ArrayList<>());

        assertThat(sut).isEmpty();
    }

    @Test
    public void shouldThrowWhenRemovingOnEmptyCollection() {
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> sut.remove(id))
                .isInstanceOfAny(NoSuchElementException.class)
                .hasMessage("No issue found with id %s.", id);
    }

    @Test
    public void shouldThrowWhenRemovingAbsentItem() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_HIGH));

        assertThatThrownBy(() -> sut.remove(ISSUE_NORMAL.getId()))
                .isInstanceOfAny(NoSuchElementException.class)
                .hasMessage("No issue found with id %s.", ISSUE_NORMAL.getId());
    }

    @Test
    public void shouldRemovePresentItem() {
        sut.add(ISSUE_NORMAL);

        sut.remove(ISSUE_NORMAL.getId());


        assertThat(sut)
                .isEmpty()
                .hasNoNormalPriorityIssues();
    }

    @Test
    public void shouldDecreaseCountOfIssuePerPriority() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_NORMAL, ISSUE_HIGH));

        ImmutableSet<Issue> i = sut.all();

        for (Issue x : i) {
            sut.remove(x.getId());
        }

        assertThat(sut)
                .isEmpty()
                .hasNoLowPriorityIssues()
                .hasNoNormalPriorityIssues()
                .hasNoHighPriorityIssues();
    }

    @Test
    public void shouldAcceptDuplicates() {

        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_LOW));

        assertThat(sut)
                .hasIssueAt(0, ISSUE_LOW)
                .hasIssueAt(1, ISSUE_LOW);
    }

    @Test
    public void shouldRemoveDuplicatesInSet() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_LOW));

        ImmutableSet<Issue> all = sut.all();
        Assertions.assertThat(all)
                .containsExactly(ISSUE_LOW);
    }

    @Test
    public void shouldRemovePresentItemWhenLast() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_HIGH, ISSUE_NORMAL));

        Issue removed = sut.remove(ISSUE_NORMAL.getId());

        assertThat(removed).isEqualTo(ISSUE_NORMAL);
    }

    @Test
    public void shouldRemovePresentItemWhenFirst() {

        sut.addAll(Arrays.asList(ISSUE_HIGH, ISSUE_NORMAL, ISSUE_LOW));

        Issue removed = sut.remove(ISSUE_HIGH.getId());

        assertThat(removed).isEqualTo(ISSUE_HIGH);
    }

    @Test
    public void shouldBeEmptyWhenNewlyConstructed() {
        assertThat(new Issues())
                .isEmpty();
    }

    @Test
    public void shouldFindPresentIssueById() {
        sut.add(ISSUE_LOW);

        Issue found = sut.findById(ISSUE_LOW.getId());

        assertThat(found)
                .isEqualTo(ISSUE_LOW);

    }

    @Test
    public void shouldFindIssueByIdWhenFirst() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_NORMAL));

        Issue found = sut.findById(ISSUE_LOW.getId());

        assertThat(found).isEqualTo(ISSUE_LOW);
    }
    @Test
    public void shouldFindIssueByIdWhenLast(){

        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_LOW));

        Issue found = sut.findById(ISSUE_LOW.getId());

        assertThat(found).isEqualTo(ISSUE_LOW);
    }

    @Test
    public void shouldThrowWhenIssueNotFoundById() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_HIGH));

        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> sut.findById(id))
                .hasMessage("No issue found with id %s.", id)
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldThrowWhenFindByIdOnEmptyCollection() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> sut.findById(id))
                .hasMessageContaining("No issue found with id")
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldReturnEmptyCollectionWhenPredicateIsFalse() {
        sut.addAll(Arrays.asList(ISSUE_HIGH, ISSUE_LOW));

        Collection<Issue> found = sut.findByProperty(p -> false);

        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void shouldNotFilterWhenPredicateTrue() {
        sut.addAll(Arrays.asList(ISSUE_HIGH, ISSUE_LOW));

        Collection<Issue> found = sut.findByProperty(p -> true);

        Assertions.assertThat(found).containsExactly(ISSUE_HIGH, ISSUE_LOW);
    }

    @Test
    public void shouldReturnItemsWithTruePredicate() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_HIGH));

        Assertions.assertThat(sut.findByProperty(p -> p.getPriority() == ISSUE_LOW.getPriority()))
                .containsExactly(ISSUE_LOW);
    }

    @Test
    public void shouldReturnIteratorForAllElements() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_HIGH));

        List<Issue> iteratorList = new ArrayList<>();

        for (final Issue aSut : sut) {
            iteratorList.add(aSut);
        }

        Assertions.assertThat(iteratorList)
                .containsExactly(ISSUE_LOW, ISSUE_HIGH);
    }

    @Test
    public void shouldHaveSameResultForSizeAndGetSizeWhenEmpty() {
        Assertions.assertThat(sut.size())
                .isEqualTo(sut.getSize());
    }


    @Test
    public void shouldHaveSameResultForSizeAndGetSizeWhenPopulated() {
        sut.addAll(Arrays.asList(ISSUE_LOW, ISSUE_HIGH));

        Assertions.assertThat(sut.size())
                .isEqualTo(sut.getSize());
    }

    @Test
    public void shouldGetElements() {

        sut.addAll(Arrays.asList(ISSUE_HIGH, ISSUE_LOW, ISSUE_NORMAL));

        assertThat(sut).hasIssueAt(0, ISSUE_HIGH)
                .hasIssueAt(1, ISSUE_LOW)
                .hasIssueAt(2, ISSUE_NORMAL);

        assertThatThrownBy(() -> sut.get(3))
                .isInstanceOf(IndexOutOfBoundsException.class);

        assertThatThrownBy(() -> sut.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);

    }

    @Test
    public void shouldContainNumerOfItemsWhenGeneratingString() {
        sut.addAll(Arrays.asList(ISSUE_NORMAL, ISSUE_LOW, ISSUE_HIGH));

        assertThat(sut)
                .hasStringRepresentationContaining("3", "issues");
    }

    @Test
    public void shouldRetrieveFilesFromIssues() {

        sut.addAll(Arrays.asList(ISSUE_NORMAL, ISSUE_HIGH));

        Assertions.assertThat(sut.getFiles())
                .contains(ISSUE_NORMAL.getFileName(), ISSUE_HIGH.getFileName());

        assertThat(sut).hasNumberOfFiles(2);
    }

    @Test
    public void shouldCopyIssues() {
        sut.addAll(Arrays.asList(ISSUE_NORMAL, ISSUE_HIGH));
        Issues copy = sut.copy();
        Assertions.assertThat(sut)
                .isNotSameAs(copy);

        Assertions.assertThat(copy.all())
                .containsExactly(ISSUE_NORMAL, ISSUE_HIGH);
    }
}