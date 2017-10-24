package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

import java.util.Collection;
/**
 * This class provides custom AssertJ assertion for {@link Issues}
 *
 * @author slausch
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert,Issues> {

    public IssuesAssert(Issues actual){
        super(actual,IssuesAssert.class);
    }

    public static IssuesAssert assertThat(Issues actual) {
        return new IssuesAssert(actual);
    }
    /**
     * Checks whether this Issues contains the specified Issue elements.
     * @param issues - Issue[] specifying searched Issue elements.
     * @return this
     */
    public IssuesAssert contains(Issue ... issues){
        Collection<Issue> savedIssues = actual.all();
        for(Issue issue: issues){
            if(!savedIssues.contains(issue))
                failWithMessage("Does not contain <%s>",issue.toString());
        }
        return this;
    }
    /**
     * Checks whether this Issues contains the specified Issue element at the given position.
     * @param issue - Issue specifying searched Issue element.
     * @param index - int specifying position of searched Issue element.
     * @return this
     */
    public IssuesAssert contains(Issue issue, int index){
        if(!actual.get(index).equals(issue)) {
            failWithMessage("Searched <%s> but found <%s> at index %d",
                    issue.toString(), actual.get(index).toString(), index);
        }
        return this;
    }
    /**
     * Checks whether this Issues contains the specified Issue element.
     * @param issue - Issue specifying searched Issue element.
     * @return this
     */
    public IssuesAssert contains(Issue issue){
        Collection<Issue> savedIssues = actual.all();
        if(!savedIssues.contains(issue))
            failWithMessage("Does not contain <%s>",issue.toString());
        return this;
    }
    /**
     * Checks whether this Issues contains a specified number of Issue elements.
     * @param size - int specifying number of Issue elements.
     * @return this
     */
    public IssuesAssert hasSize(int size){
        isNotNull();
        if(actual.getSize()!=size){
            failWithMessage("Expected size to be <%d> but was <%d>",size,actual.getSize());
        }
        return this;
    }
    /**
     * Checks whether this Issues contains a specified number of Issue elements with High priority.
     * @param prioritySize - int specifying number of Issue elements with High priority.
     * @return this
     */
    public IssuesAssert hasHighPrioritySize(int prioritySize){
        isNotNull();
        if(actual.getHighPrioritySize()!=prioritySize){
            failWithMessage("Expected size of high-prio to be <%d> but was <%d>",
                    prioritySize,actual.getHighPrioritySize());
        }
        return this;
    }
    /**
     * Checks whether this Issues contains a specified number of Issue elements with Normal priority.
     * @param prioritySize - int specifying number of Issue elements with Normal priority.
     * @return this
     */
    public IssuesAssert hasNormalPrioritySize(int prioritySize){
        isNotNull();
        if(actual.getNormalPrioritySize()!=prioritySize){
            failWithMessage("Expected size of normal-prio to be <%d> but was <%d>",
                    prioritySize,actual.getNormalPrioritySize());
        }
        return this;
    }
    /**
     * Checks whether this Issues contains a specified number of Issue elements with Low priority.
     * @param prioritySize - int specifying number of Issue elements with Low priority.
     * @return this
     */
    public IssuesAssert hasLowPrioritySize(int prioritySize){
        isNotNull();
        if(actual.getLowPrioritySize()!=prioritySize){
            failWithMessage("Expected size of low-prio to be <%d> but was <%d>",
                    prioritySize,actual.getLowPrioritySize());
        }
        return this;
    }
    /**
     * Checks whether this Issues contains a specified number of files.
     * @param numberOfFiles - int specifying number of files.
     * @return this
     */
    public IssuesAssert hasNumberOfFiles(int numberOfFiles){
        isNotNull();
        if(actual.getNumberOfFiles()!=numberOfFiles){
            failWithMessage("Expected number of files to be <%d> but was <%d>",
                    numberOfFiles,actual.getNumberOfFiles());
        }
        return this;
    }
}
