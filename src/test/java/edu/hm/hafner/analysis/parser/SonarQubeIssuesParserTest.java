package edu.hm.hafner.analysis.parser;

import static edu.hm.hafner.analysis.assertj.IssuesAssert.assertThat;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.assertSoftly;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link SonarQubeIssuesParser}.
 */
class SonarQubeIssuesParserTest extends AbstractParserTest {

	private static final String FILENAME_API = "sonarqube-api.json";
	private static final String FILENAME_API_MULTIMODULE = "sonarqube-api-multimodule.json";

    protected SonarQubeIssuesParserTest() {
    	super(FILENAME_API);
    }
    
    @Override
	protected void assertThatIssuesArePresent(Report report, SoftAssertions softly) {
    	assert report.isNotEmpty();
	}

    /**
     * Parses a report taken from the sonarqube issues API.
     * @throws IOException
     */
    @Test
    public void reportApiTest () throws IOException {
    	Report warnings = parse(FILENAME_API);
    	
        assertThat(warnings).hasSize(31);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasFileName("src/com/tsystems/sbs/jenkinslib/SbsBuild.groovy")
                    .hasLineStart(631);
        });
    }
    
    /**
     * Parses a report taken from the sonarqube issues API. The project contains multiple subprojects.
     * @throws IOException
     */
    @Test
    public void reportApiMultimoduleTest () throws IOException {
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

