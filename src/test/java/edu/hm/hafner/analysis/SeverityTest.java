package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.Severity.*;
import static edu.hm.hafner.analysis.assertj.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests the class {@link Severity}.
 *
 * @author Ullrich Hafner
 */
class SeverityTest {
    @Test
    void shouldCreateNewSeverityThatOverridesEqualsAndToString() {
        String name = "severity";
        Severity severity = new Severity(name);

        assertThat(severity.getName()).isEqualTo(name);
        assertThat(severity).hasToString(name);

        assertThat(severity).isEqualTo(new Severity(name));
        assertThat(severity.equalsIgnoreCase("SeveriTy")).isTrue();
    }

    @Test
    void shouldReturnLinkToConstants() {
        assertThat(Severity.valueOf(Priority.HIGH)).isSameAs(Severity.WARNING_HIGH);
        assertThat(Severity.valueOf(Priority.NORMAL)).isSameAs(WARNING_NORMAL);
        assertThat(Severity.valueOf(Priority.LOW)).isSameAs(WARNING_LOW);

        assertThatThrownBy(() -> Severity.valueOf((Priority) null)).isInstanceOf(NullPointerException.class);

        assertThat(Severity.valueOf("error")).isSameAs(Severity.ERROR);
        assertThat(Severity.valueOf("high")).isSameAs(Severity.WARNING_HIGH);
        assertThat(Severity.valueOf("normal")).isSameAs(WARNING_NORMAL);
        assertThat(Severity.valueOf("low")).isSameAs(WARNING_LOW);

        String name = "severity";
        assertThat(Severity.valueOf(name)).isEqualTo(new Severity(name));
    }

    @Test
    void shouldReturnPredefinedSetOfSeverities() {
        assertThat(Severity.getPredefinedValues())
                .containsExactlyInAnyOrder(ERROR, WARNING_HIGH, WARNING_NORMAL, WARNING_LOW);
    }
}