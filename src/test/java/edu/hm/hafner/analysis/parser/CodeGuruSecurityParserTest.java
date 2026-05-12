package edu.hm.hafner.analysis.parser;

import java.util.List;
import java.nio.file.FileSystems;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import org.json.JSONObject;

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
 * Tests the class {@link CodeGuruSecurityParser}.
 *
 * @author Akash Manna
 */
class CodeGuruSecurityParserTest extends AbstractParserTest {
    CodeGuruSecurityParserTest() {
        super("codeguru-security-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(3).hasDuplicatesSize(0);

        softly.assertThat(report.get(0))
                .hasFileName("src/main/java/com/example/UserDao.java")
                .hasLineStart(42)
                .hasLineEnd(44)
                .hasType("CGS-001")
                .hasMessage("SQL injection")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(0).getDescription())
                .contains("Prepared statements")
                .contains("https://cwe.mitre.org/data/definitions/89.html")
                .contains("Use prepared statements")
                .contains("Code snippet")
                .contains("42: String query =");

        softly.assertThat(report.get(1))
                .hasFileName("src/main/java/com/example/ReflectionUtil.java")
                .hasLineStart(18)
                .hasLineEnd(18)
                .hasType("CGS-002")
                .hasMessage("Arbitrary class loading")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(1).getDescription())
                .contains("Avoid reflective class loading")
                .contains("https://docs.aws.amazon.com/codeguru/latest/security-ug/")
                .contains("Suggested fixes");

        softly.assertThat(report.get(2))
                .hasFileName("Application.kt")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasType("-")
                .hasMessage("Information disclosure")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(2).getDescription())
                .contains("Sensitive data should not be logged")
                .contains("https://example.com/codeguru/reference/info-disclosure");
    }

    @Override
    protected IssueParser createParser() {
        return new CodeGuruSecurityParser();
    }

    @Test
    void accepts() {
        assertThat(new CodeGuruSecurityParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("codeguru-security-report.json")))).isTrue();
        assertThat(new CodeGuruSecurityParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void shouldHandleMissingFindings() {
        var report = parseStringContent("{}\n");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingFilePathAndSeverity() {
        var report = parseStringContent("""
                {
                  "findings": [
                    {
                      "title": "Missing file path",
                      "description": "No file path available",
                      "vulnerability": {}
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("-")
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(0)
                .hasLineEnd(0);
    }

    @Test
    void shouldSkipNonObjectFindingsAndHandleMissingVulnerability() {
        var report = parseStringContent("""
                {
                    "findings": [
                        42,
                        {
                            "title": "Fallback finding",
                            "severity": "Info"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("-")
                .hasMessage("Fallback finding")
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(0)
                .hasLineEnd(0);

        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldHandleBlankNestedValues() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "title": "",
                            "description": "",
                            "severity": "Medium",
                            "ruleId": "CGS-BLANK",
                            "vulnerability": {
                                "filePath": {
                                    "name": "Example.kt",
                                    "path": "src/main/kotlin/Example.kt",
                                    "startLine": 0,
                                    "endLine": 0
                                },
                                "referenceUrls": [],
                                "codeSnippet": [
                                    null,
                                    {
                                        "number": 0,
                                        "content": ""
                                    }
                                ]
                            },
                            "recommendation": {
                                "text": "",
                                "url": ""
                            },
                            "suggestedFixes": [
                                null,
                                {
                                    "title": "",
                                    "description": ""
                                }
                            ]
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/main/kotlin/Example.kt")
                .hasType("CGS-BLANK")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("");

        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldHandleMissingNestedCollections() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "title": "Nested collections missing",
                            "severity": "Low",
                            "vulnerability": {
                                "filePath": {
                                    "name": "Nested.kt",
                                    "path": "src/main/kotlin/Nested.kt",
                                    "startLine": 7,
                                    "endLine": 9
                                }
                            }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/main/kotlin/Nested.kt")
                .hasType("-")
                .hasMessage("Nested collections missing")
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(7)
                .hasLineEnd(9);
    }

    @Test
    void shouldHandleBlankReferenceUrls() {
        var report = parseStringContent("""
                {
                    "findings": [
                        {
                            "title": "Blank reference URLs",
                            "severity": "Medium",
                            "vulnerability": {
                                "filePath": {
                                    "path": "src/main/kotlin/BlankRefs.kt",
                                    "startLine": 1,
                                    "endLine": 2
                                },
                                "referenceUrls": [
                                    "   ",
                                    "https://example.com/codeguru/reference/blank-refs"
                                ]
                            }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("src/main/kotlin/BlankRefs.kt")
                .hasType("-")
                .hasMessage("Blank reference URLs")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(1)
                .hasLineEnd(2);

        assertThat(report.get(0).getDescription())
                .contains("https://example.com/codeguru/reference/blank-refs");
    }

    @Test
    void shouldCoverPrivateHelperBranches() throws Exception {
        var parser = new CodeGuruSecurityParser();

        var appendReferenceUrls = CodeGuruSecurityParser.class.getDeclaredMethod(
                "appendReferenceUrls", List.class, JSONObject.class);
        appendReferenceUrls.setAccessible(true);

        var sections = new ArrayList<String>();
        appendReferenceUrls.invoke(parser, sections, new JSONObject().put("referenceUrls", new JSONArray()));
        assertThat(sections).isEmpty();

        sections = new ArrayList<>();
        appendReferenceUrls.invoke(parser, sections, new JSONObject().put("referenceUrls", new JSONArray()
                .put("   ")
                .put("\t")));
        assertThat(sections).isEmpty();

        sections = new ArrayList<>();
        appendReferenceUrls.invoke(parser, sections, new JSONObject().put("referenceUrls", new JSONArray()
                .put("   ")
                .put("https://example.com/codeguru/reference/helper")));
        assertThat(sections).containsExactly("References: https://example.com/codeguru/reference/helper");

        var appendSuggestedFixes = CodeGuruSecurityParser.class.getDeclaredMethod(
                "appendSuggestedFixes", List.class, JSONArray.class);
        appendSuggestedFixes.setAccessible(true);

        sections = new ArrayList<>();
        appendSuggestedFixes.invoke(parser, sections, new JSONArray());
        assertThat(sections).isEmpty();

        sections = new ArrayList<>();
        appendSuggestedFixes.invoke(parser, sections, new JSONArray()
                .put(JSONObject.NULL)
                .put(new JSONObject().put("title", "Helper fix").put("description", "Explain helper fix"))
                .put(new JSONObject().put("title", "Only title").put("description", ""))
                .put(new JSONObject().put("title", "").put("description", "Only description")));
        assertThat(sections).hasSize(1);
        assertThat(sections.get(0))
                .contains("Suggested fixes: Helper fix: Explain helper fix | Only title | Only description");

        var appendCodeSnippets = CodeGuruSecurityParser.class.getDeclaredMethod(
                "appendCodeSnippets", List.class, JSONArray.class);
        appendCodeSnippets.setAccessible(true);

        sections = new ArrayList<>();
        appendCodeSnippets.invoke(parser, sections, new JSONArray());
        assertThat(sections).isEmpty();

        sections = new ArrayList<>();
        appendCodeSnippets.invoke(parser, sections, new JSONArray()
                .put(JSONObject.NULL)
                .put(new JSONObject().put("number", 0).put("content", "index-based value;"))
                .put(new JSONObject().put("number", 5).put("content", "return value;")));
        assertThat(sections).hasSize(1);
        assertThat(sections.get(0))
                .contains("Code snippet:")
                .contains("index-based value;")
                .contains("5: return value;");
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("codeguru-security");

        assertThat(descriptor.getPattern()).isEqualTo("**/codeguru-security-report.json");
        assertThat(descriptor.getHelp()).contains("aws codeguru-security get-findings");
        assertThat(descriptor.getUrl()).isEqualTo("https://docs.aws.amazon.com/cli/latest/reference/codeguru-security/get-findings.html");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
