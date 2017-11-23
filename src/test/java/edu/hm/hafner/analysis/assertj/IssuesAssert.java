package edu.hm.hafner.analysis.assertj;

import java.util.List;

import org.assertj.core.api.AbstractAssert;

import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

/**
 * Created by Sarah on 19.10.2017.
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    public IssuesAssert(Issues actual){super(actual, IssuesAssert.class);}

    public static IssuesAssert assertThat(Issues actual){return new IssuesAssert(actual);}

    public IssuesAssert hasElements (ImmutableSet<Issues> elements){
        isNotNull();
        if (!actual.all().equals(elements)){
            failWithMessage("\nExpected elements of:\n <%s>\nto be:\n <%s>\nbut were:\n <%s>",
                    actual, elements.toString(), actual.all().toString());
        }
        return this;
    }

    public IssuesAssert hasSize (int size){
        isNotNull();
        if(actual.size() != size){
            failWithMessage("\nExpected size of:\n <%s>\nto be:\n <%d>\nbut was:\n <%d>",
                    actual, size, actual.getSize());
        }
        return this;
    }

    public IssuesAssert hasHighPrioritySize(int sizeOfPriority){
        isNotNull();
        if(actual.getHighPrioritySize() != sizeOfPriority){
            failWithMessage("\nExpected high priority size of:\n <%s>\nto be:\n <%d>\nbut was:\n <%d>",
                    actual, sizeOfPriority, actual.getHighPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasNormalPrioritySize(int sizeOfPriority){
        isNotNull();
        if(actual.getNormalPrioritySize() != sizeOfPriority){
            failWithMessage("\nExpected normal priority size of:\n <%s>\nto be:\n <%d>\nbut was:\n <%d>",
                    actual, sizeOfPriority, actual.getNormalPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasLowPrioritySize (int sizeOfPriority){
        isNotNull();
        if(actual.getLowPrioritySize() != sizeOfPriority){
            failWithMessage("\nExpected low priority size of:\n <%s>\nto be:\n <%d>\nbut was:\n <%d>",
                    actual, sizeOfPriority, actual.getLowPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasNumberOfFiles(int numberOfFiles){
        isNotNull();
        if(actual.getNumberOfFiles() != numberOfFiles){
            failWithMessage("\nExpected number of files of:\n <%s>\nto be:\n <%d>\nbut was:\n <%d>",
                    actual, numberOfFiles, actual.getNumberOfFiles());
        }
        return this;
    }
}
