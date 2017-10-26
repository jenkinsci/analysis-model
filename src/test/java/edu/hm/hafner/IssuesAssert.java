package edu.hm.hafner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

import org.assertj.core.api.AbstractAssert;

/**
 * *****************************************************************
 * Hochschule Muenchen Fakultaet 07 (Informatik).		**
 * Autor: Sebastian Balz
 * Datum 16.10.2017
 *  Software Win 7 JDK8 Win 10 JDK8 Ubuntu 15.4 OpenJDK7	**
 * edu.hm.hafner.analysis
 *
 */
public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    /**
     * Issues hasNumber of Files.
     *
     * @param nFiles nuber
     * @return this
     */
    public IssuesAssert hasNumberOfFiles(int nFiles) {
        if (actual.getNumberOfFiles() != nFiles) {
            failWithMessage("\nExpecting umberOfFiles of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getNumberOfFiles(), nFiles);
        }
        return this;
    }

    /**
     * Issues hasLowPrioritySize.
     *
     * @param lprio prio
     * @return this
     */
    public IssuesAssert hasLowPrioritySize(int lprio) {
        if (actual.getLowPrioritySize() != lprio) {
            failWithMessage("\nExpecting LowPrioritySize of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getLowPrioritySize(), lprio);
        }
        return this;
    }

    /**
     * Issues hasNormalPrioritySize.
     *
     * @param nprio prio
     * @return this
     */
    public IssuesAssert hasNormalPrioritySize(int nprio) {
        if (actual.getNormalPrioritySize() != nprio) {
            failWithMessage("\nExpecting NormalPrioritySize of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getNormalPrioritySize(), nprio);
        }
        return this;
    }

    /**
     * Issues hasHighPrioritySize.
     *
     * @param hprio prio
     * @return this
     */
    public IssuesAssert hasHighPrioritySize(int hprio) {
        if (actual.getHighPrioritySize() != hprio) {
            failWithMessage("\nExpecting HighPrioritySize of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getHighPrioritySize(), hprio);
        }
        return this;
    }

    /**
     * Issues hasSize.
     * @param size size
     * @return this
     */
    public IssuesAssert hasSize(int size) {
        if (size != actual.getSize() || size != actual.size()) {
            failWithMessage("\nExpecting Size of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.size(), size);
        }
        return this;
    }

    /**
     * Issues IssuesAssert.
     * @param actual actual
     */
    static IssuesAssert assertThat(Issues actual) {
        return new IssuesAssert(actual, IssuesAssert.class);
    }

    private IssuesAssert(Issues issues, Class<?> selfType) {
        super(issues, selfType);
    }

    /**
     * Issues contains.
       * @param that actual
     */
    public IssuesAssert contains(Issue that) {
        if (!actual.all().contains(that)) {
            failWithMessage("issues does not contain " + that);
        }

        return this;
    }

    /**
     * Issues containsNot.
     * @param that that
     * @return this
     */
    public IssuesAssert containsNot(Issue that) {
        if (actual.all().contains(that)) {
            failWithMessage("issues does contain " + that);
        }

        return this;
    }

    /**
     * Issues isEqualTo.
     * @param that that
     * @return this
     */
    public IssuesAssert isEqualTo(Issues that) {
        ImmutableSet<Issue> origin = actual.all();
        ImmutableSet<Issue> should = that.all();
        if (!origin.equals(should)) {
            failWithMessage("Both Issues are not equal but should ");
        }
        return this;
    }

    /**
     * Issues isNotEqualTo.
     * @param that that
     * @return this
     */
    public IssuesAssert isNotEqualTo(Issues that) {
        ImmutableSet<Issue> origin = actual.all();
        ImmutableSet<Issue> should = that.all();
        if (origin.equals(should)) {
            failWithMessage("Both Issues are equal but should not ");
        }
        return this;
    }

}
