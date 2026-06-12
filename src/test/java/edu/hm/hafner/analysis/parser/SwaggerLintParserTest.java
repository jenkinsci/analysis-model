package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link SwaggerLintParser}.
 *
 * @author Akash Manna
 */
class SwaggerLintParserTest extends AbstractParserTest {
    SwaggerLintParserTest() {
        super("swagger-lint-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasType("object-prop-casing")
                .hasMessage("Property 'UserName' does not match the required casing 'camel'")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);

        softly.assertThat(report.get(0).getDescription())
                .contains("definitions")
                .contains("UserProfile")
                .contains("properties")
                .contains("UserName");

        softly.assertThat(report.get(1))
                .hasType("latin-definitions-only")
                .hasMessage("Definition name 'utilisateur' contains non-latin characters")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("-");

        softly.assertThat(report.get(1).getDescription())
                .contains("definitions")
                .contains("utilisateur");

        softly.assertThat(report.get(2))
                .hasType("properties-for-object-type")
                .hasMessage("Object type should have properties defined")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("-");

        softly.assertThat(report.get(2).getDescription())
                .contains("paths")
                .contains("/users")
                .contains("get")
                .contains("responses")
                .contains("200")
                .contains("schema");

        softly.assertThat(report.get(3))
                .hasType("no-empty-object-type")
                .hasMessage("Empty object schema found without any properties")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("-");

        softly.assertThat(report.get(3).getDescription())
                .contains("definitions")
                .contains("EmptyModel");
    }

    @Override
    protected IssueParser createParser() {
        return new SwaggerLintParser();
    }

    @Test
    void accepts() {
        assertThat(new SwaggerLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("swagger-lint-report.json")))).isTrue();
        assertThat(new SwaggerLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() {
        var report = parse("swagger-lint-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleSingleIssueWithoutLocation() {
        var report = parseStringContent("""
                [
                    {
                        "name": "no-path-with-empty-body",
                        "msg": "Path has no request body defined"
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("no-path-with-empty-body")
                .hasMessage("Path has no request body defined")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("-")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldHandleMissingNameAndMessage() {
        var report = parseStringContent("""
                [
                    {}
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasMessage("")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldSkipNonObjectArrayEntries() {
        var report = parseStringContent("""
                [
                    42,
                    {
                        "name": "valid-rule",
                        "msg": "Valid issue"
                    },
                    "some-string",
                    null
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("valid-rule")
                .hasMessage("Valid issue");
    }

    @Test
    void shouldBuildLocationDescriptionWithSingleSegment() {
        var report = parseStringContent("""
                [
                    {
                        "name": "rule-x",
                        "msg": "Issue at top level",
                        "location": ["definitions"]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .contains("definitions")
                .contains("Location");
    }

    @Test
    void shouldBuildLocationDescriptionWithDeepPath() {
        var report = parseStringContent("""
                [
                    {
                        "name": "deep-rule",
                        "msg": "Deep path issue",
                        "location": ["paths", "/api/v1/users", "post", "parameters", "0", "schema", "properties"]
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .contains("paths")
                .contains("/api/v1/users")
                .contains("post")
                .contains("parameters")
                .contains("schema")
                .contains("properties");
    }

    @Test
    void shouldHandleEmptyLocationArray() {
        var report = parseStringContent("""
                [
                    {
                        "name": "empty-location-rule",
                        "msg": "Issue with empty location",
                        "location": []
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldHandleNullLocationArray() {
        var report = parseStringContent("""
                [
                    {
                        "name": "null-location-rule",
                        "msg": "Issue with null location",
                        "location": null
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldHandleMultipleIssuesWithSameRuleName() {
        var report = parseStringContent("""
                [
                    {
                        "name": "object-prop-casing",
                        "msg": "Property 'FirstName' does not match the required casing 'camel'",
                        "location": ["definitions", "User", "properties", "FirstName"]
                    },
                    {
                        "name": "object-prop-casing",
                        "msg": "Property 'LastName' does not match the required casing 'camel'",
                        "location": ["definitions", "User", "properties", "LastName"]
                    }
                ]
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0))
                .hasType("object-prop-casing")
                .hasMessage("Property 'FirstName' does not match the required casing 'camel'");
        assertThat(report.get(0).getDescription()).contains("FirstName");

        assertThat(report.get(1))
                .hasType("object-prop-casing")
                .hasMessage("Property 'LastName' does not match the required casing 'camel'");
        assertThat(report.get(1).getDescription()).contains("LastName");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("swagger-lint");

        assertThat(descriptor.getPattern()).isEqualTo("**/swagger-lint-report.json");
        assertThat(descriptor.getHelp()).contains("swaggerlint");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/antonk52/swaggerlint");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
        assertThat(descriptor.getName()).isEqualTo("Swagger Lint");
        assertThat(descriptor.getId()).isEqualTo("swagger-lint");
    }
}
