package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link SonarQubeIssuesParser}.
 *
 * @author Carles Capdevila
 */
class SonarQubeIssuesParserTest extends AbstractParserTest {
    private static final String FILENAME_API = "sonarqube-api.json";
    private static final String FILENAME_API_MULTIMODULE = "sonarqube-api-multimodule.json";

    SonarQubeIssuesParserTest() {
        super(FILENAME_API);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(32).hasDuplicatesSize(0);
        softly.assertThat(report.get(0))
                .hasFileName("src/com/tsystems/sbs/jenkinslib/SbsBuild.groovy")
                .hasLineStart(631)
                .hasLineEnd(631);
    }

    /**
     * Parses a report taken from the SonarQube issues API. The project contains multiple sub-projects.
     */
    @Test
    void reportApiMultiModuleTest() {
        Report warnings = parse(FILENAME_API_MULTIMODULE);

        assertThat(warnings).hasSize(106);

        try (SoftAssertions softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasFileName("cart-common-folder/src/main/java/com/example/sonarqube/CloseResource.java")
                    .hasLineStart(0)
                    .hasLineEnd(0);
        }
    }

    @Test
    void shouldAcceptDifferentialFile() {
        SonarQubeParser parser = createParser();

        assertThat(parser.accepts(createReaderFactory("sonarqube-differential.json"))).isFalse();
        assertThat(parser.accepts(createReaderFactory("sonarqube-api.json"))).isTrue();
    }

    @Override
    protected SonarQubeParser createParser() {
        return new SonarQubeIssuesParser();
    }
}
