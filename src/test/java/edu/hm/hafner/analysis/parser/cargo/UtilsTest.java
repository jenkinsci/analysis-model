package edu.hm.hafner.analysis.parser.cargo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for the Cargo utlity classes.
 *
 * @author Mike Delaney
 */
class UtilsTest {
    static final String RANDOM_STRING = "kBwaPAhwUicZeX8jwEaI6aTxzkx5tGftBCu5Zf3doc";

    @Test
    void countCharOccuranceTest() {
        final Integer countA = Utils.countChar(RANDOM_STRING, 'a');
        assertThat(countA).isEqualTo(3);
        
        final Integer countZ = Utils.countChar(RANDOM_STRING, 'Z');
        assertThat(countZ).isEqualTo(2);
    }
}
