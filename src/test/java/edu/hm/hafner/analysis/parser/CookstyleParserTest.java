package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link CookstyleParser}.
 *
 * @author Akash Manna
 */
class CookstyleParserTest extends AbstractParserTest {
    CookstyleParserTest() {
        super("cookstyle-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("recipes/default.rb")
                .hasLineStart(18)
                .hasLineEnd(18)
                .hasColumnStart(3)
                .hasColumnEnd(42)
                .hasType("Chef/Modernize/PlatformFamilyEquals")
                .hasMessage("Use node['platform_family'] instead of node.platform_family?")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("recipes/default.rb")
                .hasLineStart(45)
                .hasLineEnd(45)
                .hasColumnStart(1)
                .hasColumnEnd(30)
                .hasType("Chef/Deprecations/ResourceInheritsFromCompatResource")
                .hasMessage("Resource still sets updated_by_last_action. In Chef Infra Client 13+ this is no longer necessary.")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(2))
                .hasFileName("recipes/install.rb")
                .hasLineStart(1)
                .hasLineEnd(1)
                .hasColumnStart(1)
                .hasColumnEnd(50)
                .hasType("Chef/Correctness/MetadataMissingName")
                .hasMessage("Cookbook metadata.rb does not declare a 'name' field. This is required in Chef Infra Client 12+.")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(3))
                .hasFileName("metadata.rb")
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasColumnStart(1)
                .hasColumnEnd(22)
                .hasType("Chef/Sharing/IncludePropertyDescriptions")
                .hasMessage("Add a 'license' field to cookbook metadata.rb.")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new CookstyleParser();
    }

    @Test
    void accepts() {
        assertThat(new CookstyleParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("cookstyle-report.json")))).isTrue();
        assertThat(new CookstyleParser().accepts(
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
    void shouldParseEdgeCases() {
        var report = parse("cookstyle-report-edge-cases.json");

        assertThat(report).hasSize(1);

        assertThat(report.get(0))
                .hasFileName("recipes/configure.rb")
                .hasLineStart(10)
                .hasLineEnd(10)
                .hasColumnStart(5)
                .hasColumnEnd(50)
                .hasType("Chef/Effortless/ChefVaultUsed")
                .hasMessage("Use ChefSpec::SoloRunner instead of ChefSpec::ServerRunner.")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldHandleFileWithNoOffenses() {
        var report = parseStringContent("""
                {
                    "metadata": {
                        "rubocop_version": "1.50.2"
                    },
                    "files": [
                        {
                            "path": "recipes/clean.rb",
                            "offenses": []
                        }
                    ],
                    "summary": {
                        "offense_count": 0,
                        "target_file_count": 1,
                        "inspected_file_count": 1
                    }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingFilesKey() {
        var report = parseStringContent("""
                {
                    "metadata": {
                        "rubocop_version": "1.50.2"
                    },
                    "summary": {
                        "offense_count": 0,
                        "target_file_count": 0,
                        "inspected_file_count": 0
                    }
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingLocation() {
        var report = parseStringContent("""
                {
                    "files": [
                        {
                            "path": "recipes/default.rb",
                            "offenses": [
                                {
                                    "severity": "warning",
                                    "message": "An issue without location info",
                                    "cop_name": "Chef/Style/SomeRule",
                                    "corrected": false,
                                    "correctable": false
                                }
                            ]
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("recipes/default.rb")
                .hasMessage("An issue without location info")
                .hasType("Chef/Style/SomeRule")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    /**
     * Verifies the full RuboCop/Cookstyle severity mapping applied by {@link CookstyleParser#mapSeverity(String)}.
     */
    @Test
    void shouldMapAllSeverities() {
        var report = parseStringContent("""
                {
                    "files": [
                        {
                            "path": "test.rb",
                            "offenses": [
                                {
                                    "severity": "fatal",
                                    "message": "Fatal issue",
                                    "cop_name": "Chef/Fatal/Rule",
                                    "corrected": false,
                                    "correctable": false,
                                    "location": {
                                        "start_line": 1, "start_column": 1,
                                        "last_line": 1, "last_column": 10
                                    }
                                },
                                {
                                    "severity": "error",
                                    "message": "Error issue",
                                    "cop_name": "Chef/Error/Rule",
                                    "corrected": false,
                                    "correctable": false,
                                    "location": {
                                        "start_line": 2, "start_column": 1,
                                        "last_line": 2, "last_column": 10
                                    }
                                },
                                {
                                    "severity": "warning",
                                    "message": "Warning issue",
                                    "cop_name": "Chef/Warning/Rule",
                                    "corrected": false,
                                    "correctable": false,
                                    "location": {
                                        "start_line": 3, "start_column": 1,
                                        "last_line": 3, "last_column": 10
                                    }
                                },
                                {
                                    "severity": "convention",
                                    "message": "Convention issue",
                                    "cop_name": "Chef/Convention/Rule",
                                    "corrected": false,
                                    "correctable": true,
                                    "location": {
                                        "start_line": 4, "start_column": 1,
                                        "last_line": 4, "last_column": 10
                                    }
                                },
                                {
                                    "severity": "refactor",
                                    "message": "Refactor issue",
                                    "cop_name": "Chef/Refactor/Rule",
                                    "corrected": false,
                                    "correctable": true,
                                    "location": {
                                        "start_line": 5, "start_column": 1,
                                        "last_line": 5, "last_column": 10
                                    }
                                },
                                {
                                    "severity": "info",
                                    "message": "Info issue",
                                    "cop_name": "Chef/Info/Rule",
                                    "corrected": false,
                                    "correctable": false,
                                    "location": {
                                        "start_line": 6, "start_column": 1,
                                        "last_line": 6, "last_column": 10
                                    }
                                }
                            ]
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(6);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);          
        assertThat(report.get(1)).hasSeverity(Severity.ERROR);          
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_HIGH);
        assertThat(report.get(3)).hasSeverity(Severity.WARNING_NORMAL); 
        assertThat(report.get(4)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(5)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldHandleMultipleFilesWithOffenses() {
        var report = parseStringContent("""
                {
                    "files": [
                        {
                            "path": "recipes/one.rb",
                            "offenses": [
                                {
                                    "severity": "convention",
                                    "message": "First file offense",
                                    "cop_name": "Chef/Style/Rule1",
                                    "corrected": false,
                                    "correctable": false,
                                    "location": {
                                        "start_line": 5, "start_column": 1,
                                        "last_line": 5, "last_column": 10
                                    }
                                }
                            ]
                        },
                        {
                            "path": "recipes/two.rb",
                            "offenses": [
                                {
                                    "severity": "warning",
                                    "message": "Second file offense",
                                    "cop_name": "Chef/Deprecations/Rule2",
                                    "corrected": false,
                                    "correctable": false,
                                    "location": {
                                        "start_line": 12, "start_column": 3,
                                        "last_line": 12, "last_column": 20
                                    }
                                }
                            ]
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0))
                .hasFileName("recipes/one.rb")
                .hasMessage("First file offense")
                .hasType("Chef/Style/Rule1")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(5);
        assertThat(report.get(1))
                .hasFileName("recipes/two.rb")
                .hasMessage("Second file offense")
                .hasType("Chef/Deprecations/Rule2")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(12);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("cookstyle");

        assertThat(descriptor.getPattern()).isEqualTo("**/cookstyle-report.json");
        assertThat(descriptor.getHelp()).contains("cookstyle --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/chef/cookstyle");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
        assertThat(descriptor.getType()).isEqualTo(IssueType.WARNING);
    }
}
