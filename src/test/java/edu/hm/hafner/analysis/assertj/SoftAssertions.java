package edu.hm.hafner.analysis.assertj;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.ErrorCollector;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.internal.cglib.proxy.Enhancer;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

import static org.assertj.core.groups.Properties.*;

/**
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
public class SoftAssertions {
    /**
     * Collects error messages of all AssertionErrors thrown by the proxied method.
     */
    private final ErrorCollector collector = new ErrorCollector();

    /**
     * Verifies that no proxied assertion methods have failed.
     *
     * @throws SoftAssertionError if any proxied assertion objects threw
     */
    public void assertAll() {
        List<Throwable> errors = collector.errors();
        if (!errors.isEmpty()) {
            throw new SoftAssertionError(extractProperty("message", String.class).from(errors));
        }
    }

    @SuppressWarnings("unchecked")
    public <T, V> V proxy(Class<V> assertClass, Class<T> actualClass, T actual) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(assertClass);
        enhancer.setCallback(collector);
        return (V) enhancer.create(new Class[]{actualClass}, new Object[]{actual});
    }

    /**
     * Use this to avoid having to call assertAll manually.
     * <p>
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
     * @since 3.6.0
     */
    public static void assertSoftly(Consumer<SoftAssertions> softly) {
        SoftAssertions assertions = new SoftAssertions();
        softly.accept(assertions);
        assertions.assertAll();
    }
}