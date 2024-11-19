package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link ErrorProneParser}.
 *
 * @author Ullrich Hafner
 */
class ErrorProneParserTest extends AbstractParserTest {
    ErrorProneParserTest() {
        super("errorprone-maven.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(9);

        softly.assertThat(report.get(0))
                .hasFileName("/Users/hafner/Development/git/analysis-model/src/main/java/edu/hm/hafner/analysis/IssueBuilder.java")
                .hasLineStart(86)
                .hasColumnStart(74)
                .hasType("NullAway")
                .hasMessage("passing @Nullable parameter 'fileName' where @NonNull is required.")
                .hasDescription("<p><a href=\"http://t.uber.com/nullaway\">See ErrorProne documentation.</a></p>");
        softly.assertThat(report.get(2))
                .hasFileName("/Users/hafner/Development/git/analysis-model/src/main/java/edu/hm/hafner/analysis/parser/DrMemoryParser.java")
                .hasLineStart(193)
                .hasColumnStart(44)
                .hasType("StringSplitter")
                .hasMessage("String.split(String) has surprising behavior.")
                .hasDescription("Did you mean: "
                        + "<pre><code>"
                        + "for (String line : Splitter.onPattern(&quot;\\\\r?\\\\n&quot;).split(stackTrace)) {"
                        + "</code></pre>"
                        + "<p><a href=\"https://errorprone.info/bugpattern/StringSplitter\">See ErrorProne documentation.</a></p>");
    }

    @Test
    void shouldFindAllWarnings() {
        var report = parse("error-prone-maven.log");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("/Users/hafner/Development/jenkins/workspace/Model - Freestyle - New/src/main/java/edu/hm/hafner/analysis/parser/RobocopyParser.java")
                .hasLineStart(29)
                .hasColumnStart(45)
                .hasType("StringSplitter")
                .hasMessage("String.split(String) has surprising behavior.")
                .hasDescription("Did you mean: <pre><code>String file = matcher.group(4).split(&quot;\\\\s{11}&quot;, -1)[0];</code></pre><p><a href=\"http://errorprone.info/bugpattern/StringSplitter\">See ErrorProne documentation.</a></p>");
    }

    @Test
    void shouldFindAllNewWarnings() {
        var report = parse("maven-error-prone.log");

        assertThat(report).hasSize(37);
        assertThat(report.get(0))
                .hasFileName("/Users/hafner/git/hochschule/karalight-assignment/src/main/java/Assignment04.java")
                .hasLineStart(1)
                .hasType("DefaultPackage")
                .hasMessage("Java classes shouldn't use default package.")
                .hasDescription("<p><a href=\"https://errorprone.info/bugpattern/DefaultPackage\">See ErrorProne documentation.</a></p>");
    }

    @Test
    void shouldSkipJavacWarnings() {
        var report = parse("javac.log");

        assertThat(report).hasSize(4);
        assertThat(report.get(0))
                .hasFileName("/var/data/workspace/Codingstyle/src/main/java/edu/hm/hafner/util/FilteredLog.java")
                .hasLineStart(23)
                .hasColumnStart(32)
                .hasType("InlineFormatString")
                .hasMessage("Prefer to create format strings inline, instead of extracting them to a single-use constant.")
                .hasDescription("Did you mean to remove this line?<p><a href=\"https://errorprone.info/bugpattern/InlineFormatString\">See ErrorProne documentation.</a></p>");
        assertThat(report.get(1))
                .hasFileName("/var/data/workspace/Codingstyle/src/main/java/edu/hm/hafner/util/FilteredLog.java")
                .hasLineStart(183)
                .hasColumnStart(15)
                .hasType("HashCodeToString")
                .hasMessage("Classes that override hashCode should also consider overriding toString.")
                .hasDescription("<p><a href=\"https://errorprone.info/bugpattern/HashCodeToString\">See ErrorProne documentation.</a></p>");
        assertThat(report.get(2))
                .hasFileName("/var/data/workspace/Codingstyle/src/main/java/edu/hm/hafner/util/TreeStringBuilder.java")
                .hasLineStart(91)
                .hasColumnStart(17)
                .hasType("BadImport")
                .hasMessage("Importing nested classes/static methods/static fields with commonly-used names can make code harder to read, because it may not be clear from the context exactly which type is being referred to. Qualifying the name with that of the containing class can make the code clearer. Here we recommend using qualified class: Map.")
                .hasDescription("Did you mean: <pre><code>for (Map.Entry&lt;String, Child&gt; entry : children.entrySet()) {</code></pre><p><a href=\"https://errorprone.info/bugpattern/BadImport\">See ErrorProne documentation.</a></p>");
        assertThat(report.get(3))
                .hasFileName("/var/data/workspace/Codingstyle/src/test/java/edu/hm/hafner/util/SerializableTest.java")
                .hasLineStart(72)
                .hasColumnStart(43)
                .hasType("BanSerializableRead")
                .hasMessage("Deserializing user input via the `Serializable` API is extremely dangerous.")
                .hasDescription("<p><a href=\"https://errorprone.info/bugpattern/BanSerializableRead\">See ErrorProne documentation.</a></p>");
    }

    @Override
    protected IssueParser createParser() {
        return new ErrorProneParser();
    }
}
