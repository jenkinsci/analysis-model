package edu.hm.hafner.edu.hm.hafner.analysis;


import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions.IssueAssertions;
import edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions.IssuesAssertions;
import edu.hm.hafner.edu.hm.hafner.analysis.edu.hm.hafner.analysis.assertions.IssuesSoftAssertions;
import edu.hm.hafner.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class IssuesTest {

    @Test
    public void shouldBeEmptyAndNoPriorities(){

        final Issues ius = new Issues();
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
              .hasSize(0)
              .hasNumberOfFiles(0)
              .hasLowPrioritySize(0)
              .hasNormalPrioritySize(0)
              .hasHighPrioritySize(0);
        isa.assertAll();
    }

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

    @Test
    public void shouldContainsSingleAddedElements(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = ius.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());


        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .hasSize(3)
                .hasElement(iu1)
                .hasElement(iu2)
                .hasElement(iu3);
        isa.assertAll();
    }

    @Test
    public void shouldContainsAllAddedElements(){

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
        assertEquals("No issue found with id "+notExistingUuid+".", exception.getMessage());


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
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());
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
                .hasIssuesWithId(1, iu2.getId());
        isa.assertAll();
    }

    @Test
    public void shouldStringShowSize(){

        Issues ius = new Issues();
        Issue iu1 = ius.add(getGoodIssueBuilder().setFileName("test1").build());
        Issue iu2 = ius.add(getGoodIssueBuilder().setFileName("test2").build());
        Issue iu3 = ius.add(getGoodIssueBuilder().setFileName("test3").build());

        //Check if list is ok
        IssuesSoftAssertions isa = new IssuesSoftAssertions();
        isa.assertThat(ius)
                .isString(String.format("%d issues", 3));
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
