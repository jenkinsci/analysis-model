package edu.hm.hafner.analysis.registry;

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
        assertThat(parserRegistry.getAllDescriptors()).filteredOn(d-> "spotbugs".equals(d.getId())).hasSize(1);
    }
}
