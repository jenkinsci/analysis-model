package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static j2html.TagCreator.*;

/**
 * Tests the class {@link MavenConsoleParser}.
 *
 * @author Ullrich Hafner
 */
class MavenConsoleParserTest extends AbstractParserTest {
    private static final String XREF_LINK_DISABLED = "Unable to locate Source XRef to link to - DISABLED";

    /**
     * Creates a new instance of {@link MavenConsoleParserTest}.
     */
    protected MavenConsoleParserTest() {
        super("maven-console.txt");
    }

    @Test
    void shouldAssignTypesFromGoals() {
        Report warnings = parse("maven-goals.log");

        assertThat(warnings).hasSize(3);

        assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(44).hasLineEnd(47)
                .hasType("maven-pmd-plugin:pmd");
        assertThatDescriptionIs(warnings, 0,
                XREF_LINK_DISABLED, XREF_LINK_DISABLED, XREF_LINK_DISABLED, XREF_LINK_DISABLED);

        assertThat(warnings.get(1)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(50).hasLineEnd(53)
                .hasType("maven-pmd-plugin:cpd");
        assertThatDescriptionIs(warnings, 1,
                XREF_LINK_DISABLED, XREF_LINK_DISABLED, XREF_LINK_DISABLED, XREF_LINK_DISABLED);

        assertThat(warnings.get(2)).hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(56).hasLineEnd(56)
                .hasType("maven-checkstyle-plugin:checkstyle");
        assertThatDescriptionIs(warnings, 2, XREF_LINK_DISABLED);
    }

    /**
     * Ignore `maven-javadoc-plugin` warnings
     *
     * @see <a href="https://issues.jenkins.io/browse/JENKINS-64284">Issue 64284</a>
     */
    @Test
    void issue64284() {
        Report warnings = parse("issue64284.log");
        assertThat(warnings).hasSize(3);
    }

    private void assertThatDescriptionIs(final Report warnings, final int index, final String... messages) {
        assertThat(warnings.get(index).getDescription()).isEqualTo(
                pre().with(code().withText(String.join("\n", messages))).render());
    }

    @Test
    void shouldLocateCorrectLineNumber() {
        Report warnings = parse("maven-line-number.log");

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0)).hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(45);
        assertThatDescriptionIs(warnings, 0,
                "The project edu.hm.hafner:analysis-model:jar:1.0.0-SNAPSHOT uses prerequisites "
                        + "which is only intended for maven-plugin projects but not for non maven-plugin projects. "
                        + "For such purposes you should use the maven-enforcer-plugin. "
                        + "See https://maven.apache.org/enforcer/enforcer-rules/requireMavenVersion.html");
    }

    /**
     * Pars>nored because they are blank.
     *
     * @see <a href="https://issues.jenkins-ci.org/browse/JENKINS-16826">Issue 16826</a>
     */
    @Test
    void issue16826() {
        Report warnings = parse("issue16826.txt");

        assertThat(warnings).hasSize(1);
    }

    @Test
    void shouldDetectEnforcerMessages() {
        Report warnings = parse("maven-enforcer.log");

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0))
                .hasDescription(
                        "<pre><code>Rule 4: org.apache.maven.plugins.enforcer.RequireUpperBoundDeps failed with message:\n"
                                + "Failed while enforcing RequireUpperBoundDeps. The error(s) are [\n"
                                + "Require upper bound dependencies error for org.jenkins-ci:annotation-indexer:1.11 paths to dependency are:\n"
                                + "+-io.jenkins.plugins:warnings-ng:2.2.0-SNAPSHOT\n"
                                + "  +-org.jenkins-ci.plugins:git:3.9.1\n"
                                + "    +-org.jenkins-ci:annotation-indexer:1.11\n"
                                + "and\n"
                                + "+-io.jenkins.plugins:warnings-ng:2.2.0-SNAPSHOT\n"
                                + "  +-org.jenkins-ci.main:jenkins-core:2.89.1\n"
                                + "    +-org.jenkins-ci:annotation-indexer:1.12\n"
                                + "and\n"
                                + "+-io.jenkins.plugins:warnings-ng:2.2.0-SNAPSHOT\n"
                                + "  +-org.kohsuke:access-modifier-annotation:1.15\n"
                                + "    +-org.jenkins-ci:annotation-indexer:1.4\n"
                                + "and\n"
                                + "+-io.jenkins.plugins:warnings-ng:2.2.0-SNAPSHOT\n"
                                + "  +-org.jenkins-ci.plugins:git:3.9.1\n"
                                + "    +-com.infradna.tool:bridge-method-annotation:1.17\n"
                                + "      +-org.jenkins-ci:annotation-indexer:1.4\n"
                                + "and\n"
                                + "+-io.jenkins.plugins:warnings-ng:2.2.0-SNAPSHOT\n"
                                + "  +-org.jenkins-ci.main:jenkins-core:2.89.1\n"
                                + "    +-org.jenkins-ci:bytecode-compatibility-transformer:1.8\n"
                                + "      +-org.jenkins-ci:annotation-indexer:1.4\n"
                                + ",\n"
                                + "Require upper bound dependencies error for commons-net:commons-net:3.5 paths to dependency are:\n"
                                + "+-io.jenkins.plugins:warnings-ng:2.2.0-SNAPSHOT\n"
                                + "  +-org.jenkins-ci.main:maven-plugin:3.1.2\n"
                                + "    +-org.apache.maven.wagon:wagon-ftp:3.0.0\n"
                                + "      +-commons-net:commons-net:3.5\n"
                                + "and\n"
                                + "+-io.jenkins.plugins:warnings-ng:2.2.0-SNAPSHOT\n"
                                + "  +-org.jenkins-ci.main:jenkins-test-harness:2.42\n"
                                + "    +-org.jenkins-ci.main:jenkins-test-harness-htmlunit:2.31-1\n"
                                + "      +-commons-net:commons-net:3.6\n"
                                + "]</code></pre>");
    }

    @Test
    void shouldDetectEnforcerMessagesWithTimeStamp() {
        Report warnings = parse("maven-enforcer2.log");

        assertThat(warnings).hasSize(2);
        assertThat(warnings.get(0))
                .hasDescription("<pre><code>\n"
                        + "Dependency convergence error for com.google.http-client:google-http-client-jackson2:1.22.0 paths to dependency are:\n"
                        + "+-com.&lt;org&gt;:web:165.0-SNAPSHOT\n"
                        + "  +-com.&lt;org&gt;:notestore:165.0-SNAPSHOT\n"
                        + "    +-com.&lt;org&gt;.service:gcs-lib:2.1-SNAPSHOT\n"
                        + "      +-com.google.cloud:google-cloud-storage:1.3.1\n"
                        + "        +-com.google.cloud:google-cloud-core-http:1.3.1\n"
                        + "          +-com.google.http-client:google-http-client-jackson2:1.22.0\n"
                        + "and\n"
                        + "+-com.&lt;org&gt;:web:165.0-SNAPSHOT\n"
                        + "  +-com.&lt;org&gt;:notestore:165.0-SNAPSHOT\n"
                        + "    +-com.google.cloud:google-cloud-spanner:0.21.1-beta\n"
                        + "      +-com.google.api:gax-grpc:0.22.0\n"
                        + "        +-com.google.auth:google-auth-library-oauth2-http:0.7.0\n"
                        + "          +-com.google.http-client:google-http-client-jackson2:1.19.0\n"
                        + "and\n"
                        + "+-com.&lt;org&gt;:web:165.0-SNAPSHOT\n"
                        + "  +-com.google.api-client:google-api-client:1.22.0\n"
                        + "    +-com.google.http-client:google-http-client-jackson2:1.22.0\n"
                        + "</code></pre>");
        assertThat(warnings.get(1))
                .hasDescription("<pre><code>\n"
                        + "Dependency convergence error for com.google.code.gson:gson:2.2.1 paths to dependency are:\n"
                        + "+-com.&lt;org&gt;:web:165.0-SNAPSHOT\n"
                        + "  +-com.&lt;org&gt;:notestore:165.0-SNAPSHOT\n"
                        + "    +-com.&lt;org&gt;:telesign:1.1\n"
                        + "      +-com.google.code.gson:gson:2.2.1</code></pre>");
    }

    @Test
    void shouldIdentifyMavenModules() {
        Report warnings = parse("maven-multimodule.log");

        assertThat(warnings).hasSize(2);
        assertThat(warnings.get(0)).hasModuleName("edu.hm.hafner:analysis-model");
        assertThat(warnings.get(1)).hasModuleName("edu.hm.hafner:some-other-plugin");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);
        assertThatReportHasSeverities(report, 2, 0, 3, 0);
    }

    @Override
    protected MavenConsoleParser createParser() {
        return new MavenConsoleParser();
    }
}

