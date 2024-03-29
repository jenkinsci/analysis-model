package edu.hm.hafner.analysis.registry;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.registry.ParserDescriptor.Option;
import edu.hm.hafner.util.ResourceTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link ParserRegistry}.
 *
 * @author Ullrich Hafner
 */
class ParserRegistryTest extends ResourceTest {
    public static final String SPOTBUGS = "spotbugs";
    public static final String CHECKSTYLE = "checkstyle";
    public static final String PMD = "pmd";

    @Test
    void shouldThrowExceptionIfParserNotFound() {
        ParserRegistry parserRegistry = new ParserRegistry();

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> parserRegistry.get("-"));
    }

    @Test
    void shouldFindSomeParsers() {
        ParserRegistry parserRegistry = new ParserRegistry();

        assertThat(parserRegistry).hasIds(SPOTBUGS, CHECKSTYLE, PMD).hasNames("SpotBugs", "CheckStyle", "PMD");
        assertThat(parserRegistry.get(SPOTBUGS)).hasId(SPOTBUGS).hasName("SpotBugs");
        assertThat(parserRegistry.contains(SPOTBUGS)).isTrue();
        assertThat(parserRegistry.contains("nothing")).isFalse();
        List<ParserDescriptor> descriptors = parserRegistry.getAllDescriptors();
        assertThat(descriptors).filteredOn(d-> "spotbugs".equals(d.getId())).hasSize(1);
        descriptors.forEach(d-> assertThat(d.createParser()).isNotNull());
    }

    @Test
    void shouldConfigureCpdParser() {
        ParserRegistry parserRegistry = new ParserRegistry();
        ParserDescriptor cpdDescriptor = parserRegistry.get("cpd");

        IssueParser parser = cpdDescriptor.createParser();

        Report report = parser.parse(new FileReaderFactory(getResourceAsFile("one-cpd.xml")));
        assertThat(report).hasSize(2).hasSeverities(Severity.WARNING_NORMAL);

        IssueParser highParser = cpdDescriptor.createParser(
                new Option(CpdDescriptor.HIGH_OPTION_KEY, "20"),
                new Option(CpdDescriptor.NORMAL_OPTION_KEY, "10"));

        Report highReport = highParser.parse(new FileReaderFactory(getResourceAsFile("one-cpd.xml")));
        assertThat(highReport).hasSize(2).hasSeverities(Severity.WARNING_HIGH);

        IssueParser lowParser = cpdDescriptor.createParser(
                new Option(CpdDescriptor.HIGH_OPTION_KEY, "100"),
                new Option(CpdDescriptor.NORMAL_OPTION_KEY, "50"));

        Report lowReport = lowParser.parse(new FileReaderFactory(getResourceAsFile("one-cpd.xml")));
        assertThat(lowReport).hasSize(2).hasSeverities(Severity.WARNING_LOW);
    }

    @Test
    void shouldAssignCorrectSeverityForSpotBugs() {
        verifyPriority("CONFIDENCE", 1, 11, 0);
        verifyPriority("RANK", 0, 0, 12);
    }

    private void verifyPriority(final String type, final int expectedHighSize, final int expectedNormalSize,
            final int expectedLowSize) {
        ParserRegistry parserRegistry = new ParserRegistry();
        ParserDescriptor findbugsDescriptor = parserRegistry.get("findbugs");

        IssueParser parser = findbugsDescriptor.createParser(new Option(FindBugsDescriptor.PRIORITY_OPTION_KEY, type));

        Report confidenceReport = parser.parse(new FileReaderFactory(getResourceAsFile("findbugs-severities.xml")));
        assertThat(confidenceReport).hasSize(12);
        assertThat(confidenceReport.getSizeOf(Severity.WARNING_HIGH)).isEqualTo(expectedHighSize);
        assertThat(confidenceReport.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(expectedNormalSize);
        assertThat(confidenceReport.getSizeOf(Severity.WARNING_LOW)).isEqualTo(expectedLowSize);
    }
}
