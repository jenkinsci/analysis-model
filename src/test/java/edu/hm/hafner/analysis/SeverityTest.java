package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nl.jqno.equalsverifier.EqualsVerifier;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link Severity}.
 *
 * @author Ullrich Hafner
 */
class SeverityTest {
    @Test
    void shouldAssignSeverities() {
        assertThat(Severity.guessFromString("error")).isEqualTo(Severity.ERROR);
        assertThat(Severity.guessFromString("ERROR")).isEqualTo(Severity.ERROR);
        assertThat(Severity.guessFromString("[ERROR]")).isEqualTo(Severity.ERROR);

        assertThat(Severity.guessFromString("info")).isEqualTo(Severity.WARNING_LOW);
        assertThat(Severity.guessFromString("INFO")).isEqualTo(Severity.WARNING_LOW);
        assertThat(Severity.guessFromString("[INFO]")).isEqualTo(Severity.WARNING_LOW);

        assertThat(Severity.guessFromString("warning")).isEqualTo(Severity.WARNING_NORMAL);
        assertThat(Severity.guessFromString("WARNING")).isEqualTo(Severity.WARNING_NORMAL);
        assertThat(Severity.guessFromString("[WARNING]")).isEqualTo(Severity.WARNING_NORMAL);

        assertThat(Severity.guessFromString("something")).isEqualTo(Severity.WARNING_LOW);
        assertThat(Severity.guessFromString("")).isEqualTo(Severity.WARNING_LOW);
        assertThat(Severity.guessFromString("critical thing")).isEqualTo(Severity.ERROR);
        assertThat(Severity.guessFromString("severe problem")).isEqualTo(Severity.ERROR);

        assertThat(Severity.guessFromString("note")).isEqualTo(Severity.WARNING_LOW);
        assertThat(Severity.guessFromString("NOTE")).isEqualTo(Severity.WARNING_LOW);
        assertThat(Severity.guessFromString("[NOTE]")).isEqualTo(Severity.WARNING_LOW);
    }

    @Test @SuppressWarnings("PMD.LiteralsFirstInComparisons")
    void shouldCreateNewSeverityThatOverridesEqualsAndToString() {
        var name = "severity";
        var severity = new Severity(name);

        assertThat(severity.getName()).isEqualTo(name);
        assertThat(severity).hasToString(name);

        assertThat(severity).isEqualTo(new Severity(name));
        assertThat(severity.equalsIgnoreCase("SeveriTy")).isTrue();
    }

    @Test
    void shouldReturnLinkToConstants() {
        assertThat(Severity.valueOf("error")).isSameAs(Severity.ERROR);
        assertThat(Severity.valueOf("high")).isSameAs(Severity.WARNING_HIGH);
        assertThat(Severity.valueOf("normal")).isSameAs(Severity.WARNING_NORMAL);
        assertThat(Severity.valueOf("low")).isSameAs(Severity.WARNING_LOW);

        var name = "severity";
        assertThat(Severity.valueOf(name)).isEqualTo(new Severity(name));
    }

    @Test
    void shouldReturnPredefinedSetOfSeverities() {
        assertThat(Severity.getPredefinedValues()).containsExactlyInAnyOrder(
                Severity.ERROR, Severity.WARNING_HIGH, Severity.WARNING_NORMAL, Severity.WARNING_LOW);
    }

    @Test
    void shouldCollectSeverities() {
        assertThat(Severity.collectSeveritiesFrom(Severity.ERROR))
                .containsExactly(Severity.ERROR);
        assertThat(Severity.collectSeveritiesFrom(Severity.WARNING_HIGH))
                .containsExactly(Severity.ERROR, Severity.WARNING_HIGH);
        assertThat(Severity.collectSeveritiesFrom(Severity.WARNING_NORMAL))
                .containsExactlyInAnyOrder(Severity.ERROR, Severity.WARNING_HIGH, Severity.WARNING_NORMAL);
        assertThat(Severity.collectSeveritiesFrom(Severity.WARNING_LOW))
                .containsExactlyInAnyOrder(Severity.ERROR, Severity.WARNING_HIGH, Severity.WARNING_NORMAL, Severity.WARNING_LOW);
    }

    @ParameterizedTest(name = "[{index}] Default severity = {0}")
    @ValueSource(strings = "error, high, normal, low")
    void shouldConvertToDefault(final String severityValue) {
        assertThat(Severity.valueOf(null, Severity.WARNING_LOW)).isSameAs(Severity.WARNING_LOW);
        assertThat(Severity.valueOf("wrong-name", Severity.WARNING_LOW)).isSameAs(Severity.WARNING_LOW);

        for (Severity valid : Severity.getPredefinedValues()) {
            assertThat(Severity.valueOf(valid.getName(), Severity.ERROR)).isSameAs(valid);
        }
    }

    @Test
    void shouldObeyEqualsContract() {
        EqualsVerifier.simple().forClass(Severity.class).verify();
    }
}
