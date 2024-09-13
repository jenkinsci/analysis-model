package edu.hm.hafner.analysis.parser.pylint;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link PyLintDescriptions}.
 *
 * @author Ullrich Hafner
 */
class PyLintDescriptionsTest {
    @Test
    void shouldReadAllRules() {
        var descriptions = new PyLintDescriptions();

        assertThat(descriptions.size()).isEqualTo(274);
        assertThat(descriptions.getDescription("C0112"))
                .isEqualTo(
                        "Used when a module, function, class or method has an empty docstring (it wouldbe too easy ;).");
    }
}
