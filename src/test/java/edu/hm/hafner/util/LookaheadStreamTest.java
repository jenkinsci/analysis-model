package edu.hm.hafner.util;

import org.junit.jupiter.api.Test;

/**
 * Tests the class {@link LookaheadStream}.
 *
 * @author Ullrich Hafner
 */
class LookaheadStreamTest extends ResourceTest {
    @Test
    void shouldReturnAllLines() {
        LookaheadStream stream = new LookaheadStream(getTextLinesAsStream("First Line"));


    }
}