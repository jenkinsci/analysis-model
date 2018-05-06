package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.IterableAssert;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Custom assertions for {@link Report} and {@link Issue}.
 *
 * @author Ullrich Hafner
 */
@SuppressFBWarnings("NM")
public class Assertions extends org.assertj.core.api.Assertions {
    /**
     * Creates a new {@link IssueAssert} to make assertions on actual {@link Issue}.
     *
     * @param actual
     *         the issue we want to make assertions on
     *
     * @return a new {@link IssueAssert}
     */
    public static IssueAssert assertThat(final Issue actual) {
        return new IssueAssert(actual);
    }

    /**
     * An entry point for {@link IssuesAssert} to follow AssertJ standard {@code assertThat()}. With a static import,
     * one can write directly {@code assertThat(myIssues)} and get a specific assertion with code completion.
     *
     * @param actual
     *         the issues we want to make assertions on
     *
     * @return a new {@link IssuesAssert}
     */
    public static IssuesAssert assertThat(final Report actual) {
        return new IssuesAssert(actual);
    }

    /**
     * An entry point for {@link ImmutableSortedSet} to follow AssertJ standard {@code assertThat()}. With a static
     * import, one can write directly {@code assertThat(set)} and get a specific assertion with code completion.
     *
     * @param actual
     *         the issues we want to make assertions on
     * @param <T>
     *         type of the collection elements
     *
     * @return a new {@link IterableAssert}
     */
    public static <T> IterableAssert<T> assertThat(final ImmutableSortedSet<T> actual) {
        return new IterableAssert<>(actual.castToSortedSet());
    }
}
