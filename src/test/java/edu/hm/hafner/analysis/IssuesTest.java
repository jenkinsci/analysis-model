package edu.hm.hafner.analysis;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertions.IssueAssertions;
import edu.hm.hafner.analysis.assertions.IssuesSoftAssertions;
import edu.hm.hafner.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.*;


/**
 * Tests for {@link Issues}
 * @author Raphael Furch
 */
public class IssuesTest {


    /**
     * Check if a new Issues has no elements and no priority entry.
     */
    @Test
    public void beEmptyAndNoPriorities(){

        Issues issues = new Issues();
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
              .hasSize(0)
              .hasNumberOfFiles(0)
              .hasLowPrioritySize(0)
              .hasNormalPrioritySize(0)
              .hasHighPrioritySize(0);
        softly.assertAll();
    }

    /**
     * Check if adding a element increases the element and priority counter.
     */
    @Test
    public void increasePriorityCounter(){

        Issues issues = new Issues();
        issues.add(getGoodIssueBuilder().setPriority(Priority.LOW).build());
        issues.add(getGoodIssueBuilder().setPriority(Priority.NORMAL).build());
        issues.add(getGoodIssueBuilder().setPriority(Priority.HIGH).build());
        issues.add(getGoodIssueBuilder().setPriority(Priority.HIGH).build());
        issues.add(getGoodIssueBuilder().setPriority(Priority.NORMAL).build());
        issues.add(getGoodIssueBuilder().setPriority(Priority.HIGH).build());

        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(6)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(2)
                .hasHighPrioritySize(3);
        softly.assertAll();
    }

    /**
     * Check if adding a element increases ths file counter.
     * If a file already exits, counter doesn't increases the counter.
     */
    @Test
    public void increaseFileCounter(){

        Issues issues = new Issues();
        issues.add(getGoodIssueBuilder().setFileName("File1").build());
        issues.add(getGoodIssueBuilder().setFileName("File1").build());
        issues.add(getGoodIssueBuilder().setFileName("File2").build());
        issues.add(getGoodIssueBuilder().setFileName("File3").build());

        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(4)
                .hasNumberOfFiles(3)
                .hasFiles("File1", "File2", "File3");
        softly.assertAll();
    }

    /**
     * Check if adding of a single element works fine.
     */
    @Test
    public void singleAddedElements(){

        Issues issues = new Issues();
        Issue iu1 = issues.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = issues.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = issues.add(getGoodIssueBuilder().setFileName("test3").build());


        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(3)
                .hasElementAtPosition(0,iu1)
                .hasElementAtPosition(1,iu2)
                .hasElementAtPosition(2,iu3)
                .hasElement(iu1)
                .hasElement(iu2)
                .hasElement(iu3);
        softly.assertAll();


    }

    /**
     * Check if adding a list of elements works fine.
     */
    @Test
    public void addedAllElements(){
        Issues issues = new Issues();

        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(3)
                .hasElements(   getGoodIssueBuilder().setFileName("test1").build(),
                                getGoodIssueBuilder().setFileName("test2").build(),
                                getGoodIssueBuilder().setFileName("test3").build())
                .hasAllElements(getGoodIssueBuilder().setFileName("test1").build(),
                            getGoodIssueBuilder().setFileName("test2").build(),
                            getGoodIssueBuilder().setFileName("test3").build());
        softly.assertAll();
    }

    /**
     * Check if removing of a element at the beginning works fine.
     * --> Don't lost teh other.
     */
    @Test
    public void removeSingleElementAtBeginningOtherStay(){

        Issues issues = new Issues();
        Issue iu1 = issues.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = issues.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = issues.add(getGoodIssueBuilder().setFileName("test3").build());
        Issue del = issues.remove(iu1.getId());
        //Check that deleted element is same as really deleted.
        IssueAssertions.assertThat(iu1).isEqualTo(del);
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasNotElement(iu1)
                .hasElement(iu2)
                .hasElement(iu3);
        softly.assertAll();
    }


    /**
     * Check if removing of a element at the end works fine.
     * --> List have to work after this.
     */
    @Test
    public void removeSingleElementAtEndOtherStay(){

        Issues issues = new Issues();
        Issue iu1 = issues.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = issues.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = issues.add(getGoodIssueBuilder().setFileName("test3").build());
        Issue del = issues.remove(iu3.getId());
        //Check that deleted element is same as really deleted.
        IssueAssertions.assertThat(iu3).isSameAs(del);
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasElement(iu1)
                .hasElement(iu2)
                .hasNotElement(iu3);
        softly.assertAll();
    }

    /**
     * Check if removing of a element in the middle works fine.
     * --> List have to work after this.
     */
    @Test
    public void removeSingleElementInMiddleOtherStay(){

        Issues issues = new Issues();
        Issue iu1 = issues.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = issues.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = issues.add(getGoodIssueBuilder().setFileName("test3").build());
        Issue del = issues.remove(iu2.getId());
        //Check that deleted element is same as really deleted.
        IssueAssertions.assertThat(iu2).isSameAs(del);
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasElement(iu1)
                .hasNotElement(iu2)
                .hasElement(iu3);
        softly.assertAll();
    }

    /**
     * Check if removing of a not existing element works fine.
     * --> List have to work after this.
     */
    @Test
    public void removeNotExistingElementInMiddleOtherStay(){

        Issues issues = new Issues();
        Issue iu1 = issues.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = issues.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = issues.add(getGoodIssueBuilder().setFileName("test3").build());
        UUID notExistingUuid = UUID.randomUUID();

        assertThatThrownBy(() ->   issues.remove(notExistingUuid))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining("No issue found with id "+notExistingUuid+".");


        //Check if list is ok
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(3)
                .hasHighPrioritySize(3)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasElement(iu1)
                .hasElement(iu2)
                .hasElement(iu3);
        softly.assertAll();
    }

    /**
     * Find a element by id.
     */
    @Test
    public void findIssueById(){

        Issues issues = new Issues();
        Issue iu1 = issues.add(getGoodIssueBuilder().setFileName("test1").build());
        issues.add(getGoodIssueBuilder().setFileName("test3").build());
        // Get elements by id
        assertThat(issues.findById(iu1.getId()))
                .as("Should be iu1")
                .isEqualTo(iu1);


    }


    /**
     * Force a exception because of deleting a not existing element.
     */
    @Test
    public void notFindIssueById(){

        Issues issues = new Issues();
        issues.add(getGoodIssueBuilder().setFileName("test1").build());
        issues.add(getGoodIssueBuilder().setFileName("test3").build());
        //Check if list is ok
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0);
        softly.assertAll();

        // Force exception because no element too found.
        UUID notExistingUuid = UUID.randomUUID();
        assertThatThrownBy(() ->  issues.findById(notExistingUuid))
                .isInstanceOf(NoSuchElementException.class)
                .hasNoCause()
                .hasMessageContaining("No issue found with id "+notExistingUuid+".");

    }


    /**
     * Find a element by property.
     */
    @Test
    public void findSingleIssuesByProperty(){

        Issues issues = new Issues();
        Issue iu1 = issues.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = issues.add(getGoodIssueBuilder().setFileName("test2").build());
        //Check if list is ok
        assertThat(issues.findByProperty(
                // Filter for filename which equals with iu1 (only ui1)
                (element)->
                    element.getFileName().equals(iu1.getFileName())
                ))
                .as("Should return first element")
                .containsExactly(iu1)
                .doesNotContain(iu2);

    }

    /**
     * Should find more then one element by property.
     */
    @Test
    public void findMultipleIssuesByProperty(){

        Issues issues = new Issues();
        Issue iu1 = issues.add(getGoodIssueBuilder().build());
        Issue iu2 = issues.add(getGoodIssueBuilder().build());
        //Check if list is ok
        assertThat(issues.findByProperty(
                // Filter for all elements with high priority --> Both
                (Issue element) ->
                        element.getPriority() == Priority.HIGH
                ))
                .as("Should return all elements")
                .containsExactly(iu1, iu2);

    }

    /**
     * Try to find a property but there is no.
     */
    @Test
    public void findNoIssuesByProperty(){

        Issues issues = new Issues();
        issues.add(getGoodIssueBuilder().build());
        issues.add(getGoodIssueBuilder().build());
        //Check if list is ok
        assertThat(issues.findByProperty(
                // Filter for all elements with low priority
                (element)->
                     element.getPriority() == Priority.LOW
                ))
                .as("List should be empty.").isEmpty();

    }

    /**
     * Check toString method.
     */
    @Test
    public void stringShowSize(){

        Issues issues = new Issues();
        issues.add(getGoodIssueBuilder().setFileName("test1").build());
        issues.add(getGoodIssueBuilder().setFileName("test2").build());
        issues.add(getGoodIssueBuilder().setFileName("test3").build());

        //Check if list is ok
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .isString(String.format("%d issues", 3));
        softly.assertAll();

    }

    /**
     * Check if copy has elements in same order.
     */
    @Test
    public void copyHaveElementsInSameOrder(){

        Issues issues = new Issues();
        issues.add(getGoodIssueBuilder().setFileName("test1").build());
        issues.add(getGoodIssueBuilder().setFileName("test2").build());
        issues.add(getGoodIssueBuilder().setFileName("test3").build());

        Issues copy = issues.copy();
        //Check if list is ok
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .isACopyOf(copy);
        softly.assertAll();

    }

    /**
     * Check if copy is independently
     */
    @Test
    public void copyBeImmutable(){

        Issues issues = new Issues();
        Issue ui1 = issues.add(getGoodIssueBuilder().setFileName("test1").build());
        issues.add(getGoodIssueBuilder().setFileName("test2").build());
        issues.add(getGoodIssueBuilder().setFileName("test3").build());

        Issues copy = issues.copy();
        copy.remove(ui1.getId());

        //Check if list is ok
        IssuesSoftAssertions softly = new IssuesSoftAssertions();
        softly.assertThat(issues)
                .hasElement(ui1);


        softly.assertAll();

    }

    private IssueBuilder getGoodIssueBuilder(){
        return new IssueBuilder()
                .setFileName("\\test\\")
                .setLineStart(10)
                .setLineEnd(42)
                .setColumnStart(5)
                .setColumnEnd(10)
                .setCategory("testCategory")
                .setType("testType")
                .setPackageName("testPackageName")
                .setPriority(Priority.HIGH)
                .setMessage("Test Message")
                .setDescription("Test Description");
    }
}
