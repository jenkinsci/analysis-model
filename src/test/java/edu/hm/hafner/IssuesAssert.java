package edu.hm.hafner;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import org.assertj.core.api.AbstractAssert;

/**
 * *****************************************************************
 * Hochschule Muenchen Fakultaet 07 (Informatik)		**
 * Praktikum fuer Softwareentwicklung 1 IF1B  WS15/16	**
 * *****************************************************************
 * Autor: Sebastian Balz					**
 * Datum 16.10.2017											**
 * Software Win 7 JDK8 Win 10 JDK8 Ubuntu 15.4 OpenJDK7	**
 * edu.hm.hafner.analysis                **
 * *****************************************************************
 * **
 * *****************************************************************

static IssueAssert assertThat(Issue actual){
        IssueAssert i = new IssueAssert(actual,IssueAssert.class);
        return i;
        }

public IssueAssert(Issue issue, Class<?> selfType) {
        super(issue, selfType);
        }
 *
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert,Issues> {



    IssuesAssert hasNumberOfFiles(int nFiles){
        if(actual.getNumberOfFiles() != nFiles)
            failWithMessage("actual HighPrioritySize is: "+actual.getNumberOfFiles() +  " but shout be "+ nFiles);
        return this;
    }

    IssuesAssert hasLowPrioritySize(int lprio){
        if(actual.getLowPrioritySize() != lprio)
            failWithMessage("actualLowPrioritySize is: "+actual.getLowPrioritySize() +  " but shout be "+ lprio);
        return this;
    }
    IssuesAssert hasNormalPrioritySize(int nprio){
        if(actual.getNormalPrioritySize() != nprio)
            failWithMessage("actual NormalPrioritySize is: "+actual.getNormalPrioritySize() +  " but shout be "+ nprio);
        return this;
    }
    IssuesAssert hasHighPrioritySize(int hprio){
       if(actual.getHighPrioritySize() != hprio)
           failWithMessage("actual HighPrioritySize is: "+actual.getHighPrioritySize() +  " but shout be "+ hprio);
        return this;
    }
    IssuesAssert hasSize(int size){
       if(size != actual.size())
           failWithMessage("actual size is: "+actual.size() +  " but shout be "+size);
        return this;
    }
    static IssuesAssert assertThat(Issues actual){
        return new IssuesAssert(actual,IssuesAssert.class);
    }
    public IssuesAssert(Issues issues, Class<?> selfType) {
        super(issues, selfType);
    }
    IssuesAssert contains(Issue that){
        if(!actual.all().contains(that))
            failWithMessage("issues does not contain "+that);

        return this;
    }
    IssuesAssert containsNot(Issue that){
        if(actual.all().contains(that))
            failWithMessage("issues does contain "+that);

        return this;
    }

}
