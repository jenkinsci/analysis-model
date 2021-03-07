package edu.hm.hafner.analysis.registry;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link ParserRegistry}.
 *
 * @author Ullrich Hafner
 */
class ParserRegistryTest {
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
        List<ParserDescriptor> descriptors = parserRegistry.getAllDescriptors();
        assertThat(descriptors).filteredOn(d-> "spotbugs".equals(d.getId())).hasSize(1);
        descriptors.forEach(d-> assertThat(d.createParser()).isNotNull());
    }

    @Test
    void shouldCreateSupportedFormats() throws IOException {
        ParserRegistry.main(new String[0]);
    }
}
