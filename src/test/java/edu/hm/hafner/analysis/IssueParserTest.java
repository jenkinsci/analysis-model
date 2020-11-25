package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the utilities of {@link IssueParser}.
 *
 * @author Ullrich Hafner
 */
public class IssueParserTest {
    @Test
    void shouldCompareWithoutConsideringCase() {
        assertThat(IssueParser.equalsIgnoreCase("a", "b")).isFalse();
        assertThat(IssueParser.equalsIgnoreCase("a", " a")).isFalse();
        assertThat(IssueParser.equalsIgnoreCase("a", "a ")).isFalse();

        assertThat(IssueParser.equalsIgnoreCase("a", "a")).isTrue();
        assertThat(IssueParser.equalsIgnoreCase("a", "A")).isTrue();
        assertThat(IssueParser.equalsIgnoreCase("A", "a")).isTrue();
        assertThat(IssueParser.equalsIgnoreCase("Aa", "aA")).isTrue();

        assertThat(IssueParser.equalsIgnoreCase(null, "a")).isFalse();
        assertThat(IssueParser.equalsIgnoreCase("a", null)).isFalse();
        assertThat(IssueParser.equalsIgnoreCase(null, null)).isTrue();
    }
}
