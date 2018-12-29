package edu.hm.hafner.util;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link LookaheadStream}.
 *
 * @author Ullrich Hafner
 */
class LookaheadStreamTest extends ResourceTest {
    private static final String FIRST_LINE = "First Line";

    @Test
    void shouldHandleEmptyLines() {
        LookaheadStream stream = new LookaheadStream(getTextLinesAsStream(""));

        assertThat(stream.hasNext()).isFalse();
        assertThatExceptionOfType(java.util.NoSuchElementException.class).isThrownBy(stream::next);
    }

    @Test
    void shouldReturnSingleLine() {
        LookaheadStream stream = new LookaheadStream(getTextLinesAsStream(FIRST_LINE));

        assertThat(stream.hasNext()).isTrue();
        assertThat(stream.next()).isEqualTo(FIRST_LINE);

        assertThat(stream.hasNext()).isFalse();
    }

    @Test
    void shouldReturnMultipleLines() {
        LookaheadStream stream = new LookaheadStream(getTextLinesAsStream("First Line\nSecond Line"));

        assertThat(stream.hasNext()).isTrue();
        assertThat(stream.next()).isEqualTo(FIRST_LINE);
        assertThat(stream.hasNext()).isTrue();
        assertThat(stream.next()).isEqualTo("Second Line");

        assertThat(stream.hasNext()).isFalse();
    }

    @Test
    void shouldReturnLookAheadLines() {
        LookaheadStream stream = new LookaheadStream(getTextLinesAsStream("First Line\nSecond Line"));

        assertThat(stream.hasNext()).isTrue();
        assertThat(stream.hasNext(".*Line$")).isTrue();
        assertThat(stream.hasNext("Second.*")).isFalse();
        assertThat(stream.next()).isEqualTo(FIRST_LINE);

        assertThat(stream.hasNext()).isTrue();
        assertThat(stream.hasNext(".*Line$")).isTrue();
        assertThat(stream.hasNext("First.*")).isFalse();
        assertThat(stream.next()).isEqualTo("Second Line");

        assertThat(stream.hasNext()).isFalse();
        assertThat(stream.hasNext(".*")).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldCloseStream() {
        Stream<String> lines = mock(Stream.class);
        LookaheadStream stream = new LookaheadStream(lines);

        stream.close();

        verify(lines).close();
    }
}