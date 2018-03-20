package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link Priority}.
 *
 * @author Ullrich Hafner
 */
class PriorityTest {
    private static final String WRONG_NAME = "Wrong-Name";

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
    void shouldConvertToPriorityIgnoringTheCase(final String normalName) {
        Priority priority = Priority.fromString(normalName);

        assertThat(priority.equalsIgnoreCase(normalName)).isTrue();
    }

    @ParameterizedTest(name = "[{index}] Default priority = {0}")
    @EnumSource(Priority.class)
    void shouldConvertToDefault(final Priority priority) {
        assertThat(Priority.fromString(null, priority)).isSameAs(priority);
        assertThat(Priority.fromString(WRONG_NAME, priority)).isSameAs(priority);

        for (Priority valid : Priority.values()) {
            assertThat(Priority.fromString(valid.name(), priority)).isSameAs(valid);
        }
    }

    @Test
    void shouldThrowExceptionOnInvalidElement() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Priority.valueOf(null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> Priority.valueOf(WRONG_NAME));
    }
}
