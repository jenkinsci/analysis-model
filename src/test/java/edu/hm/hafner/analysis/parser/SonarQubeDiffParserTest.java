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
class SonarQubeDiffParserTest extends AbstractParserTest {

	private static final String FILENAME_DIFF = "sonarqube-differential.json";
	private static final String FILENAME_DIFF_MULTIMODULE = "sonarqube-differential-multimodule.json";

	protected SonarQubeDiffParserTest() {
		super(FILENAME_DIFF);
	}

	@Override
	protected void assertThatIssuesArePresent(Report report, SoftAssertions softly) {
		assert report.isNotEmpty();
	}

	/**
	 * Parses a differential scan report.
	 * @throws IOException
	 */
	@Test
	public void reportDifferentialTest () throws IOException {
		Report warnings = parse(FILENAME_DIFF);

		assertThat(warnings).hasSize(6);

		assertSoftly(softly -> {
			softly.assertThat(warnings.get(0))
			.hasFileName("src/com/tsystems/sbs/jenkinslib/SbsBuild.groovy")
			.hasLineStart(266);
		});
	}

	/**
	 * Parses a differential scan report. The project contains multiple subprojects.
	 * @throws IOException
	 */
	@Test
	public void reportDifferentialMultimoduleTest () throws IOException {
		Report warnings = parse(FILENAME_DIFF_MULTIMODULE);

		assertThat(warnings).hasSize(8);

		assertSoftly(softly -> {
			softly.assertThat(warnings.get(0))
			.hasFileName("cart-appclient-folder/src/main/java/javaeetutorial/cart/client/CartClient.java")
			.hasLineStart(16);
		});
	}

	@Override
	protected AbstractParser createParser() {
		return new SonarQubeDiffParser();
	}

}

