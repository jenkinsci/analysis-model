package edu.hm.hafner.analysis;

import java.util.Iterator;

import com.google.common.collect.ImmutableSet;

import org.assertj.core.api.AbstractAssert;

/**
 * AssertJ for Issues. This class allows fluent interface for Issues
 *
 * @author Sebastian Balz
 */
final public class IssuesAssert extends AbstractAssert<IssuesAssert, Issues> {

    /**
     * Issues hasNumber of Files.
     *
     * @param nFiles nuber
     * @return this
     */
    public IssuesAssert hasNumberOfFiles(int nFiles) {
        isNotNull();
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
        isNotNull();
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
        isNotNull();
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
        isNotNull();
        if (actual.getHighPrioritySize() != hprio) {
            failWithMessage("\nExpecting HighPrioritySize of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.getHighPrioritySize(), hprio);
        }
        return this;
    }

    /**
     * Issues hasSize.
     *
     * @param size size
     * @return this
     */
    public IssuesAssert hasSize(int size) {
        isNotNull();
        if (size != actual.getSize() || size != actual.size()) {
            failWithMessage("\nExpecting Size of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>", actual, actual.size(), size);
        }
        return this;
    }

    /**
     * Issues IssuesAssert.
     *
     * @param actual actual
     */
    static IssuesAssert assertThat(Issues actual) {
        return new IssuesAssert(actual, IssuesAssert.class);
    }

    private IssuesAssert(Issues issues, Class<?> selfType) {
        super(issues, selfType);
    }

    /**
     *      Issues contains.
     *
     * @param that actual
     * @return this
     */
    public IssuesAssert contains(Issue that) {
        isNotNull();
        if (!actual.all().contains(that)) {
            failWithMessage("\nExpecting that \n<%s> does not captains <%s>\n", actual, that);
        }

        return this;
    }

    /**
     * Issues containsNot.
     *
     * @param that that
     * @return this
     */
    public IssuesAssert containsNot(Issue that) {
        isNotNull();
        if (actual.all().contains(that)) {
            failWithMessage("\nExpecting \n <%s>\n does not captains :\n <%s>", actual, that);
        }

        return this;
    }

    /**
     * Issues isEqualTo.
     *
     * @param that that
     * @return this
     */
    public IssuesAssert isEqualTo(Issues that) {
        isNotNull();
        ImmutableSet<Issue> origin = actual.all();
        ImmutableSet<Issue> should = that.all();

        if (!origin.equals(should)) {
            failWithMessage("\nExpecting \n <%s>\n to be equal with:\n <%s>", actual, that);
        }
        return this;
    }

    /**
     * Issues isNotEqualTo.
     *
     * @param that that
     * @return this
     */
    public IssuesAssert isNotEqualTo(Issues that) {
        isNotNull();
        ImmutableSet<Issue> origin = actual.all();
        ImmutableSet<Issue> should = that.all();

        if (origin.equals(should)) {
            failWithMessage("\nExpecting \n <%s>\n NOT to be equal with:\n <%s>", actual, that);
        }
        return this;
    }

    /**
     * does the Issues obj contains exactly the given issue.
     * @param issues
     * @return
     */
    public IssuesAssert containsExactly(Issue... issues) {
        isNotNull();
        Iterator<Issue> i = actual.iterator();
        for (int n = 0; n < issues.length; n++) {
            if (!i.hasNext()) {
                failWithMessage("\nExpecting \n <%s>\n has <%s> element  \nbut has : <%s>", actual, issues.length, n);
            }
            Issue current = i.next();
            if (!current.equals(issues[n])) {
                failWithMessage("\nExpecting \n <%s>\n has at <%s> \n the element \n <%s> but it was \n <%s>", actual, n, issues[n], current);
            }

        }
        if (i.hasNext()) {
            failWithMessage("\nExpecting \n <%s>\n has <%s> \n element  but has more", actual, issues.length);
        }

        return this;
    }


}
