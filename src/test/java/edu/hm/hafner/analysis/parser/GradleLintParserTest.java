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
 * Tests the class {@link GradleLintParser}.
 *
 * @author Akash Manna
 */
class GradleLintParserTest extends AbstractParserTest {
    GradleLintParserTest() {
        super("gradle-lint-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("build.gradle")
                .hasType("dependency-parentheses")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(14)
                .hasMessage("In Gradle, dependency declarations do not need to be surrounded by parentheses");

        softly.assertThat(report.get(0).getDescription())
                .contains("compile('com.google.guava:guava:28.0-jre')");

        softly.assertThat(report.get(1))
                .hasFileName("build.gradle")
                .hasType("unused-dependency")
                .hasSeverity(Severity.ERROR)
                .hasLineStart(18)
                .hasMessage("this dependency is unused and can be removed");

        softly.assertThat(report.get(1).getDescription())
                .contains("testImplementation('junit:junit:4.12')");

        softly.assertThat(report.get(2))
                .hasFileName("subproject/build.gradle")
                .hasType("minimum-dependency-version")
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(9)
                .hasMessage("junit:junit is below the minimum version of 5.0.0");

        softly.assertThat(report.get(2).getDescription())
                .contains("testImplementation 'junit:junit:4.12'");

        softly.assertThat(report.get(3))
                .hasFileName("build.gradle")
                .hasType("dependency-tuple")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(22)
                .hasMessage("Replace the String dependency notation with the map notation");
    }

    @Override
    protected IssueParser createParser() {
        return new GradleLintParser();
    }

    @Test
    void accepts() {
        assertThat(new GradleLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("gradle-lint-report.json")))).isTrue();
        assertThat(new GradleLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("issues-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleEmptyViolationsArray() {
        var report = parseStringContent("""
                {
                    "violations": []
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingViolationsKey() {
        var report = parseStringContent("""
                {
                    "summary": "No violations found"
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldSkipNullElementsInViolationsArray() {
        var report = parseStringContent("""
                {
                    "violations": [
                        null,
                        {
                            "buildFile": "build.gradle",
                            "ruleName": "unused-dependency",
                            "priority": 1,
                            "lineNumber": 5,
                            "message": "this dependency is unused and can be removed"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasFileName("build.gradle").hasType("unused-dependency");
    }

    @Test
    void shouldFallbackToHyphenWhenBuildFileIsBlank() {
        var report = parseStringContent("""
                {
                    "violations": [
                        {
                            "buildFile": "",
                            "ruleName": "unused-dependency",
                            "priority": 1,
                            "lineNumber": 5,
                            "message": "this dependency is unused and can be removed"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasFileName("-");
    }

    @Test
    void shouldFallbackToHyphenWhenRuleNameIsBlank() {
        var report = parseStringContent("""
                {
                    "violations": [
                        {
                            "buildFile": "build.gradle",
                            "ruleName": "",
                            "priority": 2,
                            "lineNumber": 10,
                            "message": "unknown lint violation"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasType("-");
    }

    @Test
    void shouldMapPriorityOneToError() {
        var report = parseStringContent("""
                {
                    "violations": [
                        {
                            "buildFile": "build.gradle",
                            "ruleName": "unused-dependency",
                            "priority": 1,
                            "lineNumber": 5,
                            "message": "this dependency is unused and can be removed"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldMapPriorityTwoToWarningNormal() {
        var report = parseStringContent("""
                {
                    "violations": [
                        {
                            "buildFile": "build.gradle",
                            "ruleName": "dependency-parentheses",
                            "priority": 2,
                            "lineNumber": 12,
                            "message": "dependency declarations do not need parentheses"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldMapPriorityThreeAndAboveToWarningLow() {
        var report = parseStringContent("""
                {
                    "violations": [
                        {
                            "buildFile": "build.gradle",
                            "ruleName": "minimum-dependency-version",
                            "priority": 3,
                            "lineNumber": 8,
                            "message": "dependency is below the minimum version"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldIncludeSourceLineInDescription() {
        var report = parseStringContent("""
                {
                    "violations": [
                        {
                            "buildFile": "build.gradle",
                            "ruleName": "dependency-parentheses",
                            "priority": 2,
                            "lineNumber": 14,
                            "message": "dependency declarations do not need parentheses",
                            "sourceLine": "    compile('org.springframework:spring-core:5.3.9')"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .contains("compile('org.springframework:spring-core:5.3.9')");
    }

    @Test
    void shouldHaveEmptyDescriptionWhenSourceLineAbsent() {
        var report = parseStringContent("""
                {
                    "violations": [
                        {
                            "buildFile": "build.gradle",
                            "ruleName": "unused-dependency",
                            "priority": 1,
                            "lineNumber": 7,
                            "message": "one or more classes in guava are required by your code directly"
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("gradle-lint");

        assertThat(descriptor.getPattern()).isEqualTo("**/gradle-lint-report.json");
        assertThat(descriptor.getHelp()).contains("gradleLint");
        assertThat(descriptor.getHelp()).contains("generateGradleLintReport");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/nebula-plugins/gradle-lint-plugin");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}