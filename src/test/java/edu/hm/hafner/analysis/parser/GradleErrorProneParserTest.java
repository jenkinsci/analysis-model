package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link GradleErrorProneParser}.
 *
 * @author Ullrich Hafner
 */
class GradleErrorProneParserTest extends AbstractParserTest {
    GradleErrorProneParserTest() {
        super("error-prone.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);
        softly.assertThat(report.get(0))
                .hasMessage("Prefer Splitter to String.split.")
                .hasFileName("/home/bjerre/workspace/git-changelog/git-changelog-lib/src/main/java/se/bjurr/gitchangelog/internal/integrations/github/GitHubHelper.java")
                .hasDescription("Did you mean: <pre><code>for (final String part : Splitter.on(&quot;,&quot;).split(link)) {</code></pre><p><a href=\"http://errorprone.info/bugpattern/StringSplitter\">See ErrorProne documentation.</a></p>")
                .hasType("StringSplitter")
                .hasLineStart(51)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldParseGradleLogWithWindowsDrive() {
        Report report = parse("issue55846.log");

        assertThat(report).hasSize(2);
        assertThat(report.get(0))
                .hasMessage("Declaring a type parameter that is only used in the return type is a misuse of generics: operations on the type parameter are unchecked, it hides unsafe casts at invocations of the method, and it interacts badly with method overload resolution.")
                .hasFileName("D:/Jenkins/workspace/Develop Debug Branch Tests/app/src/main/java/com/zao/testapp/util/json/JSONParser.java")
                .hasDescription("<p><a href=\"https://errorprone.info/bugpattern/TypeParameterUnusedInFormals\">See ErrorProne documentation.</a></p>")
                .hasType("TypeParameterUnusedInFormals")
                .hasLineStart(35)
                .hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(1))
                .hasMessage("Declaring a type parameter that is only used in the return type is a misuse of generics: operations on the type parameter are unchecked, it hides unsafe casts at invocations of the method, and it interacts badly with method overload resolution.")
                .hasFileName("D:/Jenkins/workspace/Develop Debug Branch Tests/app/src/main/java/com/zao/testapp/util/json/impl/gson/JSONParserGson.java")
                .hasDescription("<p><a href=\"https://errorprone.info/bugpattern/TypeParameterUnusedInFormals\">See ErrorProne documentation.</a></p>")
                .hasType("TypeParameterUnusedInFormals")
                .hasLineStart(77)
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected GradleErrorProneParser createParser() {
        return new GradleErrorProneParser();
    }
}
