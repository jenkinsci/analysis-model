package edu.hm.hafner.edu.hm.hafner.analysis;


import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import com.google.common.collect.ImmutableSortedSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions.IssueAssertions;
import edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions.IssuesSoftAssertions;
import edu.hm.hafner.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("JUnitTestMethodWithNoAssertions")
public class IssuesTest {


    /**
     * Check if a new Issues has no elements and no priority entry.
     */
    @Test
    public void shouldBeEmptyAndNoPriorities(){

        Issues ius = new Issues();
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
              .hasSize(0)
              .hasNumberOfFiles(0)
              .hasLowPrioritySize(0)
              .hasNormalPrioritySize(0)
              .hasHighPrioritySize(0);
        isa.assertAll();
    }

    /**
     * Check if adding a element increases the element and priority counter.
     */
    @Test
    public void shouldIncreasePriorityCounter(){

        Issues ius = new Issues();
        ius.add(getGoodIssueBuilder().setPriority(Priority.LOW).build());
        ius.add(getGoodIssueBuilder().setPriority(Priority.NORMAL).build());
        ius.add(getGoodIssueBuilder().setPriority(Priority.HIGH).build());
        ius.add(getGoodIssueBuilder().setPriority(Priority.HIGH).build());
        ius.add(getGoodIssueBuilder().setPriority(Priority.NORMAL).build());
        ius.add(getGoodIssueBuilder().setPriority(Priority.HIGH).build());

        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(6)
                .hasLowPrioritySize(1)
                .hasNormalPrioritySize(2)
                .hasHighPrioritySize(3);
        isa.assertAll();
    }

    /**
     * Check if adding a element increases ths file counter.
     * If a file already exits, counter doesn't increases the counter.
     */
    @Test
    public void shouldIncreaseFileCounter(){

        Issues ius = new Issues();
        ius.add(getGoodIssueBuilder().setFileName("File1").build());
        ius.add(getGoodIssueBuilder().setFileName("File1").build());
        ius.add(getGoodIssueBuilder().setFileName("File2").build());
        ius.add(getGoodIssueBuilder().setFileName("File3").build());

        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(4)
                .hasNumberOfFiles(3)
                .hasFiles(ImmutableSortedSet.copyOf(Arrays.asList("File1", "File2", "File3")));
        isa.assertAll();
    }

    /**
     * Check if adding of a single element works fine.
     */
    @Test
    public void shouldContainsSingleAddedElements(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = ius.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());


        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(3)
                .hasElementAtPosition(0,iu1)
                .hasElementAtPosition(1,iu2)
                .hasElementAtPosition(2,iu3)
                .hasElement(iu1)
                .hasElement(iu2)
                .hasElement(iu3);
        isa.assertAll();
    }

    /**
     * Check if adding a list of elements works fine.
     */
    @Test
    public void shouldContainsAllAddedElements(){

        @SuppressWarnings("CloneableClassWithoutClone")
        Collection<Issue> issues = new ArrayList<Issue>(){
            {
                add(getGoodIssueBuilder().setFileName("test1").build());
                add(getGoodIssueBuilder().setFileName("test2").build());
                add(getGoodIssueBuilder().setFileName("test3").build());
            }
        };
        Issues ius = new Issues();
        Collection<? extends Issue> expected = ius.addAll(issues);

        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(3)
                .hasElements(expected)
                .hasAllElements(expected);
        isa.assertAll();
    }

    /**
     * Check if removing of a element at the beginning works fine.
     * --> Don't lost teh other.
     */
    @Test
    public void shouldRemoveSingleElementAtBeginningOtherStay(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = ius.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());
        Issue del = ius.remove(iu1.getId());
        //Check that deleted element is same as really deleted.
        IssueAssertions.assertThat(iu1).isEqualTo(del);
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasNotElement(iu1)
                .hasElement(iu2)
                .hasElement(iu3);
        isa.assertAll();
    }


    /**
     * Check if removing of a element at the end works fine.
     * --> List have to work after this.
     */
    @Test
    public void shouldRemoveSingleElementAtEndOtherStay(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = ius.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());
        Issue del = ius.remove(iu3.getId());
        //Check that deleted element is same as really deleted.
        IssueAssertions.assertThat(iu3).isEqualTo(del);
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasElement(iu1)
                .hasElement(iu2)
                .hasNotElement(iu3);
        isa.assertAll();
    }

    /**
     * Check if removing of a element in the middle works fine.
     * --> List have to work after this.
     */
    @Test
    public void shouldRemoveSingleElementInMiddleOtherStay(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = ius.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());
        Issue del = ius.remove(iu2.getId());
        //Check that deleted element is same as really deleted.
        IssueAssertions.assertThat(iu2).isEqualTo(del);
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasElement(iu1)
                .hasNotElement(iu2)
                .hasElement(iu3);
        isa.assertAll();
    }

    /**
     * Check if removing of a not existing element works fine.
     * --> List have to work after this.
     */
    @Test
    public void shouldRemoveNotExistingElementInMiddleOtherStay(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = ius.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());
        UUID notExistingUuid = UUID.randomUUID();


        Throwable exception = assertThrows(NoSuchElementException.class, () -> {
            ius.remove(notExistingUuid);
        });
        assertEquals( "No issue found with id "+notExistingUuid+".", exception.getMessage(), "Wrong text");


        //Check if list is ok
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(3)
                .hasHighPrioritySize(3)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasElement(iu1)
                .hasElement(iu2)
                .hasElement(iu3);
        isa.assertAll();
    }

    @Test
    public void shouldFindIssueById(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());
        //Check if list is ok
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasElementWithUuid(iu1.getId())
                .hasElementWithUuid(iu3.getId());
        isa.assertAll();
    }

    @Test
    public void shouldNotFindIssueById(){

        Issues ius = new Issues();
        ius.add(getGoodIssueBuilder().setFileName("test1").build());
        ius.add(getGoodIssueBuilder().setFileName("test3").build());
        UUID notExistingUuid = UUID.randomUUID();
        //Check if list is ok
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(2)
                .hasHighPrioritySize(2)
                .hasNormalPrioritySize(0)
                .hasLowPrioritySize(0)
                .hasNoElementWithUuid(notExistingUuid);
        isa.assertAll();
    }

    @Test
    public void shouldFindIssuesPerProperty(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        iu1.setFingerprint("testFP");
        Issue iu2 = ius.add(getGoodIssueBuilder().setFileName("test2").setPriority(Priority.LOW).build());
        //Check if list is ok
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasIssuesWithFileName(1,iu1.getFileName())
                .hasIssuesWithCategory(2, iu1.getCategory())
                .hasIssuesWithType(2, iu1.getType())
                .hasIssuesWithPriority(1, iu1.getPriority())
                .hasIssuesWithPriority(1, iu2.getPriority())
                .hasIssuesWithMessage(2, iu1.getMessage())
                .hasIssuesWithDescription(2, iu1.getDescription())
                .hasIssuesWithPackageName(2, iu1.getPackageName())
                .hasIssuesWithLineStart(2,iu1.getLineStart())
                .hasIssuesWithLineEnd(2, iu2.getLineEnd())
                .hasIssuesWithColumnStart(2, iu1.getColumnStart())
                .hasIssuesWithColumnEnd(2, iu2.getColumnEnd())
                .hasIssuesWithId(1, iu1.getId())
                .hasIssuesWithId(1, iu2.getId())
                .hasIssuesWithFingerprint(1, iu1.getFingerprint());
        isa.assertAll();
    }

    @Test
    public void shouldStringShowSize(){

        Issues ius = new Issues();
        ius.add(getGoodIssueBuilder().setFileName("test1").build());
        ius.add(getGoodIssueBuilder().setFileName("test2").build());
        ius.add(getGoodIssueBuilder().setFileName("test3").build());

        //Check if list is ok
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .isString(String.format("%d issues", 3));
        isa.assertAll();

    }

    @Test
    public void shouldCopyHaveElementsInSameOrder(){

        Issues ius = new Issues();
        ius.add(getGoodIssueBuilder().setFileName("test1").build());
        ius.add(getGoodIssueBuilder().setFileName("test2").build());
        ius.add(getGoodIssueBuilder().setFileName("test3").build());

        Issues copy = ius.copy();
        //Check if list is ok
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .isACopyOf(copy);
        isa.assertAll();

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
