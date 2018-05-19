package edu.hm.hafner.analysis.assertj;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.AbstractStandardSoftAssertions;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.SoftAssertionError;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import static org.assertj.core.api.Assertions.*;

/**
 * Custom soft assertions for {@link Report} and {@link Issue}.
 *
 * @author Ullrich Hafner
 */
public class SoftAssertions extends AbstractStandardSoftAssertions {
    /**
     * Verifies that no proxied assertion methods have failed.
     *
     * @throws SoftAssertionError if any proxied assertion objects threw
     */
    public void assertAll() {
        List<Throwable> errors = errorsCollected();
        if (!errors.isEmpty()) {
            throw new SoftAssertionError(extractProperty("message", String.class).from(errors));
        }
    }

    /**
     * Creates a new {@link IssueAssert} to make assertions on actual {@link Issue}.
     *
     * @param actual the issue we want to make assertions on
     * @return a new {@link IssueAssert}
     */
    public IssueAssert assertThat(final Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
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
    @SuppressWarnings("unchecked")
    public <T> IterableAssert<T> assertThat(final ImmutableSortedSet<T> actual) {
        return proxy(IterableAssert.class, Iterator.class, actual.iterator());
    }

    /**
     * An entry point for {@link IssuesAssert} to follow AssertJ standard {@code assertThat()}. With a static import,
     * one can write directly {@code assertThat(myIssues)} and get a specific assertion with code completion.
     *
     * @param actual the issues we want to make assertions on
     * @return a new {@link IssuesAssert}
     */
    public IssuesAssert assertThat(final Report actual) {
        return proxy(IssuesAssert.class, Report.class, actual);
    }

    /**
     * Use this to avoid having to call assertAll manually.
     *
     * <pre><code class='java'> &#064;Test
     * public void host_dinner_party_where_nobody_dies() {
     *   Mansion mansion = new Mansion();
     *   mansion.hostPotentiallyMurderousDinnerParty();
     *   SoftAssertions.assertSoftly(softly -> {
     *     softly.assertThat(mansion.guests()).as(&quot;Living Guests&quot;).isEqualTo(7);
     *     softly.assertThat(mansion.kitchen()).as(&quot;Kitchen&quot;).isEqualTo(&quot;clean&quot;);
     *     softly.assertThat(mansion.library()).as(&quot;Library&quot;).isEqualTo(&quot;clean&quot;);
     *     softly.assertThat(mansion.revolverAmmo()).as(&quot;Revolver Ammo&quot;).isEqualTo(6);
     *     softly.assertThat(mansion.candlestick()).as(&quot;Candlestick&quot;).isEqualTo(&quot;pristine&quot;);
     *     softly.assertThat(mansion.colonel()).as(&quot;Colonel&quot;).isEqualTo(&quot;well kempt&quot;);
     *     softly.assertThat(mansion.professor()).as(&quot;Professor&quot;).isEqualTo(&quot;well kempt&quot;);
     *   });
     * }</code></pre>
     *
     * @param softly the SoftAssertions instance that you can call your own assertions on.
     * @throws SoftAssertionError if any proxied assertion objects threw
     */
    public static void assertSoftly(final Consumer<SoftAssertions> softly) {
        SoftAssertions assertions = new SoftAssertions();
        softly.accept(assertions);
        assertions.assertAll();
    }
}
