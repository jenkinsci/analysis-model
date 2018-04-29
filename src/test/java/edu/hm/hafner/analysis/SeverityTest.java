package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

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
        assertThat(Severity.of(Priority.HIGH)).isSameAs(Severity.WARNING_HIGH);
        assertThat(Severity.of(Priority.NORMAL)).isSameAs(Severity.WARNING_NORMAL);
        assertThat(Severity.of(Priority.LOW)).isSameAs(Severity.WARNING_LOW);

        assertThatThrownBy(() -> Severity.of((Priority) null)).isInstanceOf(NullPointerException.class);

        assertThat(Severity.of("error")).isSameAs(Severity.ERROR);
        assertThat(Severity.of("high")).isSameAs(Severity.WARNING_HIGH);
        assertThat(Severity.of("normal")).isSameAs(Severity.WARNING_NORMAL);
        assertThat(Severity.of("low")).isSameAs(Severity.WARNING_LOW);

        String name = "severity";
        assertThat(Severity.of(name)).isEqualTo(new Severity(name));


    }
}