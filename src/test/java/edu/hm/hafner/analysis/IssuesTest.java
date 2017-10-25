package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.UUID;
import java.util.function.Function;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.util.NoSuchElementException;
import static edu.hm.hafner.analysis.IssueAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the class {@link Issues}.
 */
class IssuesTest {

    private static Issue lowPriorityIssue;
    private static Issue normalPriorityIssue;
    private static Issue highPriorityIssue;
    private Issues issuesUnderTest;

    @BeforeAll
    static void setup() {
        lowPriorityIssue = new IssueBuilder()
                .setPriority(Priority.LOW)
                .setMessage("lowPriorityIssue")
                .setFileName("Low_Priority_File.txt")
                .build();
        normalPriorityIssue = new IssueBuilder()
                .setPriority(Priority.NORMAL)
                .setMessage("normalPriorityIssue")
                .setFileName("Normal_Priority_File.txt")
                .build();
        highPriorityIssue = new IssueBuilder()
                .setPriority(Priority.HIGH)
                .setMessage("highPriorityIssue")
                .setFileName("High_Priority_File.txt")
                .build();
    }

    /**
     * @return an UUID unique to the issues UUIDs.
     */
    private UUID getUniqueUUID() {
        // Make sure we get an UUID that should surely raise an exception
        UUID random;
        do {
            random = UUID.randomUUID();
        } while (lowPriorityIssue.getId().equals(random));

        return random;
    }

    /**
     * @return a list containing 3 issues, one for each priority.
     */
    private List<Issue> getIssueList() {
        List<Issue> issueList = new ArrayList<>();
        issueList.add(lowPriorityIssue);
        issueList.add(normalPriorityIssue);
        issueList.add(highPriorityIssue);
        return issueList;
    }

    @BeforeEach
    void setupEach() {
        issuesUnderTest = new Issues();
    }

    /** Issues should be empty on initialization. */
    @Test
    void containerShouldBeEmptyOnInitialization() {
        assertThat(issuesUnderTest).hasSize(0);
    }

    /** On adding an element to the collection, the same object should be returned. */
    @Test
    void addShouldReturnAddedObject() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.add(lowPriorityIssue)).isEqualTo(lowPriorityIssue);
            softly.assertThat(issuesUnderTest.add(normalPriorityIssue)).isEqualTo(normalPriorityIssue);
            softly.assertThat(issuesUnderTest.add(highPriorityIssue)).isEqualTo(highPriorityIssue);
        });
    }

    /** On adding an element to the collection, size should increase. */
    @Test
    void addShouldIncreaseSize() {
        IssuesSoftAssertions soft = new IssuesSoftAssertions();

        // Should be 0 at the start
        soft.assertThat(issuesUnderTest).hasSize(0);

        // Should be 1 after adding an item
        issuesUnderTest.add(lowPriorityIssue);
        soft.assertThat(issuesUnderTest).hasSize(1);

        // Should be 2 after adding another item
        issuesUnderTest.add(normalPriorityIssue);
        soft.assertThat(issuesUnderTest).hasSize(2);

        // Should be 3 after adding another item
        issuesUnderTest.add(highPriorityIssue);
        soft.assertThat(issuesUnderTest).hasSize(3);

        /*SoftAssertions.assertSoftly((softly) -> {

            // Should be 0 at the start
            softly.assertThat(issuesUnderTest).hasSize(0);

            // Should be 1 after adding an item
            issuesUnderTest.add(lowPriorityIssue);
            softly.assertThat(issuesUnderTest).hasSize(1);

            // Should be 2 after adding another item
            issuesUnderTest.add(normalPriorityIssue);
            softly.assertThat(issuesUnderTest).hasSize(2);

            // Should be 3 after adding another item
            issuesUnderTest.add(highPriorityIssue);
            softly.assertThat(issuesUnderTest).hasSize(3);
        });*/
    }

    /** On adding an element, prioritySize should incease accordingly. */
    @Test
    void addShouldIncreasePriority() {
        IssuesSoftAssertions soft = new IssuesSoftAssertions();

        // lowPrioritySize should be 1, the others should be zero
        issuesUnderTest.add(lowPriorityIssue);
        soft.assertThat(issuesUnderTest).hasLowPrioritySize(1);
        soft.assertThat(issuesUnderTest).hasNormalPrioritySize(0);
        soft.assertThat(issuesUnderTest).hasHighPrioritySize(0);
        soft.assertAll();
    }

    /** On adding a collection, that same collection should be returned. */
    @Test
    void addAllShouldReturnAddedCollection() {
        List<Issue> issueList = getIssueList();
        assertThat(issuesUnderTest.addAll(issueList)).isEqualTo(issueList);
    }

    /** On adding a collection, every element of that collection should be added. */
    @Test
    void addAllShouldAddEveryElement() {
        issuesUnderTest.addAll(getIssueList());

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.findById(lowPriorityIssue.getId())).isEqualTo(lowPriorityIssue);
            softly.assertThat(issuesUnderTest.findById(normalPriorityIssue.getId())).isEqualTo(normalPriorityIssue);
            softly.assertThat(issuesUnderTest.findById(highPriorityIssue.getId())).isEqualTo(highPriorityIssue);
        });
    }

    /** On adding an empty collection, no items should be added so the size will remain the same. */
    @Test
    void addEmptyCollectionShouldAddNoItems() {
        issuesUnderTest.addAll(Collections.emptyList());
        assertThat(issuesUnderTest).isEmpty();
    }

    /** On removing an item, that item should be returned. */
    @Test
    void removeShouldReturnElement() {
        issuesUnderTest.add(lowPriorityIssue);
        assertThat(issuesUnderTest.remove(lowPriorityIssue.getId())).isEqualTo(lowPriorityIssue);
    }

    /** On removing an item, the prioritySize should be decreased accordingly. */
    @Test
    void removeShouldDecreasePriority() {
        issuesUnderTest.add(lowPriorityIssue);
        assertThat(issuesUnderTest.getLowPrioritySize()).isOne();
        issuesUnderTest.remove(lowPriorityIssue.getId());
        assertThat(issuesUnderTest.getLowPrioritySize()).isZero();
    }

    /** On removing an item, the size should be decreased. */
    @Test
    void removeShouldDecreaseSize() {
        issuesUnderTest.add(lowPriorityIssue);
        assertThat(issuesUnderTest).hasSize(1);
        issuesUnderTest.remove(lowPriorityIssue.getId());
        assertThat(issuesUnderTest).isEmpty();
    }

    /** On trying to remove a non-existing item, NoSuchElementException should be raised. */
    @Test
    void removeNonExistingItemShouldThrowException() {
        UUID unique = getUniqueUUID();

        SoftAssertions.assertSoftly((softly) -> {
            issuesUnderTest.add(lowPriorityIssue);
            softly.assertThatThrownBy(() -> issuesUnderTest.remove(unique))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage(String.format("No issue found with id %s.", unique));
        });
    }

    /** all() should return an empty set when no issues were added. */
    @Test
    void allShouldReturnEmptySetWhenEmpty() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.all())
                    .isInstanceOf(ImmutableSet.class);
            softly.assertThat(issuesUnderTest.all())
                    .isEmpty();
        });
    }

    /** all() should return an ImmutableSet of the items that were added. */
    @Test
    void allShouldReturnImmutableSetOfCollection() {
        List<Issue> issueList = getIssueList();
        issuesUnderTest.addAll(issueList);

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.all())
                    .isInstanceOf(ImmutableSet.class);
            softly.assertThat(issuesUnderTest.all())
                    .containsAll(issueList);
        });
    }

    /** findById should return an item with a matching id. */
    @Test
    void findByIdShouldReturnElementWithMatchingId() {
        issuesUnderTest.add(lowPriorityIssue);
        assertThat(issuesUnderTest.findById(
                lowPriorityIssue.getId()).getId()).isEqualTo(lowPriorityIssue.getId());
    }

    /** findById should raise NoSuchElementException when no item with matching id was found. */
    @Test
    void findByNonExistingIdShouldThrowException() {
        UUID unique = getUniqueUUID();

        SoftAssertions.assertSoftly((softly) -> {
            issuesUnderTest.add(lowPriorityIssue);
            softly.assertThatThrownBy(() -> issuesUnderTest.findById(unique))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage(String.format("No issue found with id %s.", unique));
        });
    }

    /** findByProperty() should return an ImmutableList which is empty when no item matches the predicate. */
    @Test
    void findByPropertyNoMatchingElementsShouldReturnImmutableSet() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(
                    issuesUnderTest.findByProperty(e -> e.equals(normalPriorityIssue)))
                    .isInstanceOf(ImmutableList.class);

            softly.assertThat(
                    issuesUnderTest.findByProperty(e -> e.equals(normalPriorityIssue)))
                    .isEmpty();
        });
    }

    /** findByProperty() should return all elements which match the predicate in an immutable list. */
    @Test
    void findByPropertyShouldReturnAllMatchingElements() {
        issuesUnderTest.addAll(getIssueList());

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(
                    issuesUnderTest.findByProperty(e -> e.getPriority() == lowPriorityIssue.getPriority()))
                    .isInstanceOf(ImmutableList.class);
            softly.assertThat(
                    issuesUnderTest.findByProperty(e -> e.getPriority() == lowPriorityIssue.getPriority()))
                    .hasSize(1);
        });
    }

    /** get() should return the element with index accordingly. */
    @Test
    void getShouldReturnElementAtPosition() {
        issuesUnderTest.add(lowPriorityIssue);
        issuesUnderTest.add(normalPriorityIssue);
        assertThat(issuesUnderTest.get(0)).isEqualTo(lowPriorityIssue);
        assertThat(issuesUnderTest.get(1)).isEqualTo(normalPriorityIssue);
    }

    /** toString() should return a nicely formatted string. */
    @Test
    void toStringShouldReturnSizeString() {
        assertThat(issuesUnderTest.toString())
                .isEqualTo(String.format("%d issues", issuesUnderTest.size()));

        issuesUnderTest.addAll(getIssueList());
        assertThat(issuesUnderTest.toString())
                .isEqualTo(String.format("%d issues", issuesUnderTest.size()));
    }

    /** getFiles() should return a SortedSet of affected files of the issues that were added. */
    @Test
    void getFilesShouldReturnSortedSetOfFiles() {
        SoftAssertions.assertSoftly((softly) -> {
            assertThat(issuesUnderTest.getFiles())
                    .isInstanceOf(SortedSet.class);
            assertThat(issuesUnderTest.getFiles())
                    .isEmpty();

            issuesUnderTest.addAll(getIssueList());
            assertThat(issuesUnderTest.getFiles())
                    .hasSize(3);

            assertThat(issuesUnderTest.getFiles())
                    .contains(
                            lowPriorityIssue.getFileName(),
                            normalPriorityIssue.getFileName(),
                            highPriorityIssue.getFileName()
                    );
        });
    }

    /** getNumberOfFiles() should return the count of files that were affected by the issues that were added. */
    @Test
    void getNumberOfFilesShouldReturnCountOfAffectedFiles() {
        IssuesSoftAssertions soft = new IssuesSoftAssertions();

        // 0 Files at start
        soft.assertThat(issuesUnderTest).hasNumberOfFiles(0);

        // Add issues, number of files should be 3 afterwards
        issuesUnderTest.addAll(getIssueList());
        soft.assertThat(issuesUnderTest).hasNumberOfFiles(3);

        soft.assertAll();
    }

    /** getProperties() should return a SortedSet of the mapped properties. */
    @Test
    void getPropertiesShouldReturnSortedSetWithProperties() {
        issuesUnderTest.addAll(getIssueList());

        SoftAssertions.assertSoftly((softly) -> {
            Function<Issue, String> mapper = issue -> issue.getMessage();
            assertThat(issuesUnderTest.getProperties(mapper))
                    .isInstanceOf(SortedSet.class);
            assertThat(issuesUnderTest.getProperties(mapper))
                    .contains(
                            lowPriorityIssue.getMessage(),
                            normalPriorityIssue.getMessage(),
                            highPriorityIssue.getMessage()
                    );
        });
    }

    /** copy() of an empty issues-instance should be empty as well. */
    @Test
    void copyOfEmptyCollectionShouldBeEmpty() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.copy())
                    .isInstanceOf(Issues.class);
            softly.assertThat(issuesUnderTest.copy())
                    .isEmpty();
        });
    }

    /** copy() should return an instance of Issues which contains the same Issue-elements in the same order. */
    @Test
    void copyOfIssuesShouldContainAllIssuesInSameOrder() {
        issuesUnderTest.addAll(getIssueList());

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.copy())
                    .containsExactlyElementsOf(issuesUnderTest);
        });
    }

    /** copy() should return a shallow copy, e.g. the Issue-elements are only copied by reference. */
    @Test
    void copyOfShouldReturnShallowCopy() {
        issuesUnderTest.addAll(getIssueList());
        Issues copy = issuesUnderTest.copy();

        issuesUnderTest.get(1).setFingerprint("Fingerprint");
        assertThat(copy.get(1)).hasFingerprint("Fingerprint");
        //assertThat(copy.get(1).getFingerprint()).isEqualTo(issuesUnderTest.get(1).getFingerprint());
    }
}