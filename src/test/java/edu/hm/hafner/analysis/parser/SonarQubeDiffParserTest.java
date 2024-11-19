package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link SonarQubeDiffParser}.
 *
 * @author Carles Capdevila
 */
class SonarQubeDiffParserTest extends AbstractParserTest {
    private static final String FILENAME_DIFF = "sonarqube-differential.json";
    private static final String FILENAME_DIFF_MULTIMODULE = "sonarqube-differential-multimodule.json";

    SonarQubeDiffParserTest() {
        super(FILENAME_DIFF);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(6);

        softly.assertThat(report.get(0))
                .hasFileName("src/com/tsystems/sbs/jenkinslib/SbsBuild.groovy")
                .hasLineStart(266);
    }

    /**
     * Parses a differential scan report. The project contains multiple sub-projects.
     */
    @Test
    void reportDifferentialMultiModuleTest() {
        var warnings = parse(FILENAME_DIFF_MULTIMODULE);

        assertThat(warnings).hasSize(8);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("cart-appclient-folder/src/main/java/javaeetutorial/cart/client/CartClient.java")
                    .hasLineStart(16);
        }
    }

    @Test
    void shouldAcceptDifferentialFile() {
        var parser = createParser();

        assertThat(parser.accepts(createReaderFactory("sonarqube-differential.json"))).isTrue();
        assertThat(parser.accepts(createReaderFactory("sonarqube-api.json"))).isFalse();
    }

    @Override
    protected SonarQubeParser createParser() {
        return new SonarQubeDiffParser();
    }
}
