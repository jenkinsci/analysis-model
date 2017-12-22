package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link Priority}.
 *
 * @author Ullrich Hafner
 */
class PriorityTest {
    @Test
    void shouldCollectPriorities() {
        assertThat(Priority.collectPrioritiesFrom(Priority.HIGH))
                .containsExactly(Priority.HIGH);
        assertThat(Priority.collectPrioritiesFrom(Priority.NORMAL))
                .containsExactlyInAnyOrder(Priority.HIGH, Priority.NORMAL);
        assertThat(Priority.collectPrioritiesFrom(Priority.LOW))
                .containsExactlyInAnyOrder(Priority.HIGH, Priority.NORMAL, Priority.LOW);
    }

    @ParameterizedTest(name = "[{index}] Priority name = {0}")
    @ValueSource(strings = {"NORMAL", "normal", "NorMal"})
    void shouldConvert(final String normalName) {
        Priority priority = Priority.fromString(normalName);

        assertThat(priority.equalsIgnoreCase(normalName)).isTrue();
    }
}