package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

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
        softly.assertThat(report).hasSize(31);
        softly.assertThat(report.get(0))
                .hasFileName("src/com/tsystems/sbs/jenkinslib/SbsBuild.groovy")
                .hasLineStart(631);
    }

    /**
     * Parses a report taken from the SonarQube issues API. The project contains multiple sub-projects.
     */
    @Test
    void reportApiMultiModuleTest() {
        Report warnings = parse(FILENAME_API_MULTIMODULE);

        assertThat(warnings).hasSize(106);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasFileName("cart-common-folder/src/main/java/com/example/sonarqube/CloseResource.java")
                    .hasLineStart(0);
        });
    }

    @Override
    protected AbstractParser createParser() {
        return new SonarQubeIssuesParser();
    }
}

