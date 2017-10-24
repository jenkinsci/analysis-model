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
import static org.assertj.core.api.Assertions.*;

class IssuesTest {

    private Issues issuesUnderTest;
    private static Issue lowPriorityIssue;
    private static Issue normalPriorityIssue;
    private static Issue highPriorityIssue;

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

    private List<Issue> getIssueList() {
        List<Issue> issueList = new ArrayList<>();
        issueList.add(lowPriorityIssue);
        issueList.add(normalPriorityIssue);
        issueList.add(highPriorityIssue);
        return issueList;
    }

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

    @BeforeEach
    void setupEach() {
        issuesUnderTest = new Issues();
    }

    @Test
    void containerShouldBeEmptyOnInitialization() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.size()).isZero();
            softly.assertThat(issuesUnderTest.getSize()).isZero();
        });
    }

    @Test
    void addShouldReturnAddedObject() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.add(lowPriorityIssue)).isEqualTo(lowPriorityIssue);
            softly.assertThat(issuesUnderTest.add(normalPriorityIssue)).isEqualTo(normalPriorityIssue);
            softly.assertThat(issuesUnderTest.add(highPriorityIssue)).isEqualTo(highPriorityIssue);
        });
    }

    @Test
    void addShouldIncreaseSize() {
        SoftAssertions.assertSoftly((softly) -> {

            // Should be 0 at the start
            softly.assertThat(issuesUnderTest.size()).isZero();
            softly.assertThat(issuesUnderTest.getSize()).isZero();

            // Should be 1 after adding an item
            issuesUnderTest.add(lowPriorityIssue);
            softly.assertThat(issuesUnderTest.size()).isOne();
            softly.assertThat(issuesUnderTest.getSize()).isOne();

            // Should be 2 after adding another item
            issuesUnderTest.add(normalPriorityIssue);
            softly.assertThat(issuesUnderTest.size()).isEqualTo(2);
            softly.assertThat(issuesUnderTest.getSize()).isEqualTo(2);

            // Should be 3 after adding another item
            issuesUnderTest.add(highPriorityIssue);
            softly.assertThat(issuesUnderTest.size()).isEqualTo(3);
            softly.assertThat(issuesUnderTest.getSize()).isEqualTo(3);
        });
    }

    @Test
    void addShouldIncreasePriority() {
        issuesUnderTest.add(lowPriorityIssue);
        SoftAssertions.assertSoftly((softly) -> {
            // Size should be 1
            softly.assertThat(issuesUnderTest.size()).isOne();
            softly.assertThat(issuesUnderTest.getSize()).isOne();

            // Low priority should be 2, the other should be 0
            softly.assertThat(issuesUnderTest.getLowPrioritySize()).isOne();
            softly.assertThat(issuesUnderTest.getNormalPrioritySize()).isZero();
            softly.assertThat(issuesUnderTest.getHighPrioritySize()).isZero();
        });
    }

    @Test
    void addAllShouldReturnAddedCollection() {
        List<Issue> issueList = getIssueList();
        assertThat(issuesUnderTest.addAll(issueList)).isEqualTo(issueList);
    }

    @Test
    void addAllShouldAddEveryElement() {
        issuesUnderTest.addAll(getIssueList());

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.findById(lowPriorityIssue.getId())).isEqualTo(lowPriorityIssue);
            softly.assertThat(issuesUnderTest.findById(normalPriorityIssue.getId())).isEqualTo(normalPriorityIssue);
            softly.assertThat(issuesUnderTest.findById(highPriorityIssue.getId())).isEqualTo(highPriorityIssue);
        });
    }

    @Test
    void addEmptyCollectionShouldAddNoItems() {
        issuesUnderTest.addAll(Collections.emptyList());
        assertThat(issuesUnderTest.size()).isZero();
    }

    @Test
    void removeShouldReturnElement() {
        issuesUnderTest.add(lowPriorityIssue);
        assertThat(issuesUnderTest.remove(lowPriorityIssue.getId())).isEqualTo(lowPriorityIssue);
    }

    @Test
    void removeShouldDecreasePriority() {
        issuesUnderTest.add(lowPriorityIssue);
        assertThat(issuesUnderTest.getLowPrioritySize()).isOne();
        issuesUnderTest.remove(lowPriorityIssue.getId());
        assertThat(issuesUnderTest.getLowPrioritySize()).isZero();
    }

    @Test
    void removeShouldDecreaseSize() {
        issuesUnderTest.add(lowPriorityIssue);
        assertThat(issuesUnderTest.size()).isOne();
        issuesUnderTest.remove(lowPriorityIssue.getId());
        assertThat(issuesUnderTest.size()).isZero();
    }

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

    @Test
    void allShouldReturnEmptySetWhenEmpty() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.all())
                    .isInstanceOf(ImmutableSet.class);
            softly.assertThat(issuesUnderTest.all())
                    .isEmpty();
        });
    }

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

    @Test
    void findByIdShouldReturnElementWithMatchingId() {
        issuesUnderTest.add(lowPriorityIssue);
        assertThat(issuesUnderTest.findById(lowPriorityIssue.getId()).getId()).isEqualTo(lowPriorityIssue.getId());
    }

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

    @Test
    void getShouldReturnElementAtPosition() {
        issuesUnderTest.add(lowPriorityIssue);
        issuesUnderTest.add(normalPriorityIssue);
        assertThat(issuesUnderTest.get(0)).isEqualTo(lowPriorityIssue);
        assertThat(issuesUnderTest.get(1)).isEqualTo(normalPriorityIssue);
    }

    @Test
    void toStringShouldReturnSizeString() {
        assertThat(issuesUnderTest.toString())
                .isEqualTo(String.format("%d issues", issuesUnderTest.size()));

        issuesUnderTest.addAll(getIssueList());
        assertThat(issuesUnderTest.toString())
                .isEqualTo(String.format("%d issues", issuesUnderTest.size()));
    }

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

    @Test
    void getNumberOfFilesShouldReturnCountOfAffectedFiles() {
        SoftAssertions.assertSoftly((softly) -> {
            assertThat(issuesUnderTest.getNumberOfFiles()).isZero();
            issuesUnderTest.addAll(getIssueList());
            assertThat(issuesUnderTest.getNumberOfFiles()).isEqualTo(3);
        });
    }

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

    @Test
    void copyOfEmptyCollectionShouldBeEmpty() {
        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.copy())
                    .isInstanceOf(Issues.class);
            softly.assertThat(issuesUnderTest.copy())
                    .isEmpty();
        });
    }

    @Test
    void copyOfIssuesShouldContainAllIssuesInSameOrder() {
        issuesUnderTest.addAll(getIssueList());

        SoftAssertions.assertSoftly((softly) -> {
            softly.assertThat(issuesUnderTest.copy())
                    .containsExactlyElementsOf(issuesUnderTest);
        });
    }

    @Test
    void copyOfShouldReturnShallowCopy() {
        issuesUnderTest.addAll(getIssueList());
        Issues copy = issuesUnderTest.copy();

        issuesUnderTest.get(1).setFingerprint("Fingerprint");
        assertThat(copy.get(1).getFingerprint()).isEqualTo(issuesUnderTest.get(1).getFingerprint());
    }
}