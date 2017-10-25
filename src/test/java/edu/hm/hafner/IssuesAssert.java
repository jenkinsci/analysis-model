package edu.hm.hafner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
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
 * <p>
 * static IssueAssert assertThat(Issue actual){
 * IssueAssert i = new IssueAssert(actual,IssueAssert.class);
 * return i;
 * }
 * <p>
 * public IssueAssert(Issue issue, Class<?> selfType) {
 * super(issue, selfType);
 * }
 */
class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    /**
     * Issues hasNumber of Files.
     */
    IssuesAssert hasNumberOfFiles(int nFiles) {
        if (actual.getNumberOfFiles() != nFiles)
            failWithMessage("actual HighPrioritySize is: " + actual.getNumberOfFiles() + " but shout be " + nFiles);
        return this;
    }

    /**
     * Issues hasLowPrioritySize.
     */
    IssuesAssert hasLowPrioritySize(int lprio) {
        if (actual.getLowPrioritySize() != lprio)
            failWithMessage("actualLowPrioritySize is: " + actual.getLowPrioritySize() + " but shout be " + lprio);
        return this;
    }

    /**
     * Issues hasNormalPrioritySize.
     */
    IssuesAssert hasNormalPrioritySize(int nprio) {
        if (actual.getNormalPrioritySize() != nprio)
            failWithMessage("actual NormalPrioritySize is: " + actual.getNormalPrioritySize() + " but shout be " + nprio);
        return this;
    }

    /**
     * Issues hasHighPrioritySize.
     */
    IssuesAssert hasHighPrioritySize(int hprio) {
        if (actual.getHighPrioritySize() != hprio)
            failWithMessage("actual HighPrioritySize is: " + actual.getHighPrioritySize() + " but shout be " + hprio);
        return this;
    }

    /**
     * Issues hasSize.
     */
    IssuesAssert hasSize(int size) {
        if (size != actual.getSize() || size != actual.size())
            failWithMessage("actual size is: " + actual.size() + " but shout be " + size);
        return this;
    }

    /**
     * Issues IssuesAssert.
     */
    static IssuesAssert assertThat(Issues actual) {
        return new IssuesAssert(actual, IssuesAssert.class);
    }

    private IssuesAssert(Issues issues, Class<?> selfType) {
        super(issues, selfType);
    }

    /**
     * Issues contains.
     */
    IssuesAssert contains(Issue that) {
        if (!actual.all().contains(that))
            failWithMessage("issues does not contain " + that);

        return this;
    }

    /**
     * Issues containsNot.
     */
    IssuesAssert containsNot(Issue that) {
        if (actual.all().contains(that))
            failWithMessage("issues does contain " + that);

        return this;
    }

    /**
     * Issues isEqualTo.
     */
    IssuesAssert isEqualTo(Issues that) {
        ImmutableSet<Issue> origin = actual.all();
        ImmutableSet<Issue> should = that.all();
        if (!origin.equals(should))
            failWithMessage("Both Issues are not equal but should ");
        return this;
    }

    /**
     * Issues isNotEqualTo.
     */
    IssuesAssert isNotEqualTo(Issues that) {
        ImmutableSet<Issue> origin = actual.all();
        ImmutableSet<Issue> should = that.all();
        if (origin.equals(should))
            failWithMessage("Both Issues are equal but should not ");
        return this;
    }

}
