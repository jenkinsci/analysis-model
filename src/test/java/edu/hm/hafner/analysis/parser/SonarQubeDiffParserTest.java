package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static java.nio.charset.StandardCharsets.*;
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
        Report warnings = parse(FILENAME_DIFF_MULTIMODULE);

        assertThat(warnings).hasSize(8);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasFileName("cart-appclient-folder/src/main/java/javaeetutorial/cart/client/CartClient.java")
                    .hasLineStart(16);
        });
    }

    @Test
    void shouldAcceptDifferentialFile() {
        SonarQubeParser parser = createParser();

        assertThat(parser.accepts(getResourceAsFile("sonarqube-differential.json"), UTF_8)).isTrue();
        assertThat(parser.accepts(getResourceAsFile("sonarqube-api.json"), UTF_8)).isFalse();

    }
    
    @Override
    protected SonarQubeParser createParser() {
        return new SonarQubeDiffParser();
    }
}

