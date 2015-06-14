package edu.hm.hafner.sokoban;

import org.junit.Test;

import static edu.hm.hafner.sokoban.Field.*;
import static edu.hm.hafner.sokoban.SokobanAssertions.*;

/**
 * Tests the class {@link BorderAsciiStringLevelConverter}.
 *
 * @author Ullrich Hafner
 */
public class BorderAsciiStringLevelConverterTest {
    /** Verifies that the "chaos.sok" level is correctly read. */
    @Test
    public void shouldReadChaosLevel() {
        String[] lines = new FileReader().readLines("chaos.sok");

        SokobanGame sokoban = new BorderAsciiStringLevelConverter().convert(lines);

        Field[][] expected = {
                {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, BACKGROUND, BACKGROUND, WALL, WALL, WALL, WALL, WALL, BACKGROUND},
                {BACKGROUND, WALL, WALL, WALL, FLOOR, FLOOR, TARGET, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, WALL, TARGET, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, WALL, TARGET, FLOOR, FLOOR, WALL, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, FLOOR, TARGET, WALL, BACKGROUND},
                {BACKGROUND, WALL, WALL, WALL, WALL, WALL, WALL, WALL, BACKGROUND},
                {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
        };
        assertThatLevelIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(4, 2));
        assertThatTreasuresAreAt(sokoban, new Point(3, 3), new Point(4, 4), new Point(5, 4), new Point(5, 6));
    }

    /** Verifies that the "minicosmos.sok" level is correctly read. */
    @Test
    public void shouldReadMiniCosmosLevel() {
        String[] lines = new FileReader().readLines("minicosmos.sok");

        SokobanGame sokoban = new BorderAsciiStringLevelConverter().convert(lines);

        Field[][] expected = {
                {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, BACKGROUND, BACKGROUND, WALL, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
                {BACKGROUND, WALL, WALL, WALL, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, WALL, FLOOR, WALL, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, WALL, FLOOR, FLOOR, TARGET, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, WALL, WALL, FLOOR, WALL, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                {BACKGROUND, BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, WALL, WALL, WALL, BACKGROUND},
                {BACKGROUND, BACKGROUND, WALL, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
        };
        assertThatLevelIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(3, 7));
        assertThatTreasuresAreAt(sokoban, new Point(3, 3));
    }
}