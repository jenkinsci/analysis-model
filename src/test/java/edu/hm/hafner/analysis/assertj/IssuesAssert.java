package edu.hm.hafner.analysis.assertj;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AbstractAssert;

import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;

/**
 * Created by Sarah on 19.10.2017.
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {
    public IssuesAssert(Issues actual){super(actual, IssuesAssert.class);}

    public static IssuesAssert assertThat(Issues actual){return new IssuesAssert(actual);}

    public IssuesAssert hasElements (ImmutableSet<Issues> elements){
        isNotNull();
        if (!actual.all().equals(elements)){
            failWithMessage("Expected elements to be <%s> but were <%s>",
                    elements.toString(), actual.all().toString());
        }
        return this;
    }

    public IssuesAssert hasSize (int size){
        isNotNull();
        if(actual.size() != size){
            failWithMessage("Expected size to be <%d> but was <%d>",
                    size, actual.getSize());
        }
        return this;
    }

    public IssuesAssert hasHighPrioritySize(int sizeOfPriority){
        isNotNull();
        if(actual.getHighPrioritySize() != sizeOfPriority){
            failWithMessage("Expected high priority size to be <%d> but was <%d>",
                    sizeOfPriority, actual.getHighPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasNormalPrioritySize(int sizeOfPriority){
        isNotNull();
        if(actual.getNormalPrioritySize() != sizeOfPriority){
            failWithMessage("Expected normal priority size to be <%d> but was <%d>",
                    sizeOfPriority, actual.getNormalPrioritySize());
        }
        return this;
    }

    public IssuesAssert hasLowPrioritySize (int sizeOfPriority){
        isNotNull();
        if(actual.getLowPrioritySize() != sizeOfPriority){
            failWithMessage("Expected low priority size to be <%d> but was <%d>",
                    sizeOfPriority, actual.getLowPrioritySize());
        }
        return this;
    }
}
