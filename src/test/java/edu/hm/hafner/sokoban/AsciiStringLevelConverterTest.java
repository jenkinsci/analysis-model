package edu.hm.hafner.sokoban;

import org.junit.Test;

import edu.hm.hafner.util.Point;
import static edu.hm.hafner.sokoban.Field.*;
import static edu.hm.hafner.sokoban.SokobanAssertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link AsciiStringLevelConverter}.
 *
 * @author Ullrich Hafner
 */
public class AsciiStringLevelConverterTest {
    /** Verifies that the "chaos.sok" level is correctly read. */
    @Test
    public void shouldReadChaosLevel() {
        String[] lines = new FileReader().readLines("chaos.sok");

        SokobanGame sokoban = new AsciiStringLevelConverter().convert(lines);

        Field[][] expected = {
                {BACKGROUND, BACKGROUND, WALL, WALL, WALL, WALL, WALL},
                {WALL, WALL, WALL, FLOOR, FLOOR, TARGET, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, WALL, TARGET, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, TARGET, FLOOR, FLOOR, WALL, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, TARGET, WALL},
                {WALL, WALL, WALL, WALL, WALL, WALL, WALL},
        };
        assertThatLevelIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(3, 1));
        assertThatTreasuresAreAt(sokoban, new Point(2, 2), new Point(3, 3), new Point(4, 3), new Point(4, 5));
    }

    /** Verifies that the "minicosmos.sok" level is correctly read. */
    @Test
    public void shouldReadMiniCosmosLevel() {
        String[] lines = new FileReader().readLines("minicosmos.sok");

        SokobanGame sokoban = new AsciiStringLevelConverter().convert(lines);

        Field[][] expected = {
                {BACKGROUND, BACKGROUND, WALL, WALL, WALL, WALL, WALL, BACKGROUND},
                {WALL, WALL, WALL, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                {WALL, FLOOR, FLOOR, FLOOR, WALL, FLOOR, WALL, WALL},
                {WALL, FLOOR, WALL, FLOOR, FLOOR, TARGET, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL, FLOOR, WALL},
                {WALL, WALL, FLOOR, WALL, FLOOR, FLOOR, FLOOR, WALL},
                {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, WALL, WALL, WALL},
                {BACKGROUND, WALL, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
        };
        assertThatLevelIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(2, 6));
        assertThatTreasuresAreAt(sokoban, new Point(2, 2));
    }

    /** Verifies that a level with different line lengths is correctly read. */
    @Test
    public void shouldReadAsymmetricLevel() {
        StringLevelConverter converter = new AsciiStringLevelConverter();
        String[] inputLevel = {
                "####",
                "# .#",
                "#  ###",
                "#*@  #",
                "#  $ #",
                "#  ###",
                "####"
        };

        SokobanGame sokoban = converter.convert(inputLevel);

        Field[][] expected = {
                {WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
                {WALL, FLOOR, TARGET, WALL, BACKGROUND, BACKGROUND},
                {WALL, FLOOR, FLOOR, WALL, WALL, WALL},
                {WALL, TARGET, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, WALL, WALL, WALL},
                {WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
        };
        assertThatLevelIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(2, 3));
        assertThatTreasuresAreAt(sokoban, new Point(1, 3), new Point(3, 4));
    }

    /** Verifies that a rectangular level with a player on a target is correctly read. */
    @Test
    public void shouldReadRectangleLevel() {
        StringLevelConverter converter = new AsciiStringLevelConverter();
        String[] fields = {
                "######",
                "#    #",
                "# #  #",
                "# $* #",
                "# +* #",
                "#    #",
                "######"
        };

        SokobanGame sokoban = converter.convert(fields);

        assertThatRectangularLevelIsCorrect(sokoban);
    }

    /** Verifies that a rectangular level with a player on a target is correctly read if empty lines are present. */
    @Test
    public void shouldReadRectangleLevelWithCommentsAndEmptylines() {
        StringLevelConverter converter = new AsciiStringLevelConverter();
        String[] fields = {
                "######",
                "",
                "#    #",
                "::",
                "# #  #",
                "::   Hallo ",
                "# $* #",
                "# +* #",
                "#    #",
                "######"
        };

        SokobanGame sokoban = converter.convert(fields);

        assertThatRectangularLevelIsCorrect(sokoban);
    }

    private void assertThatRectangularLevelIsCorrect(final SokobanGame sokoban) {
        Field[][] expected = {
                {WALL, WALL, WALL, WALL, WALL, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, WALL, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, TARGET, FLOOR, WALL},
                {WALL, FLOOR, TARGET, TARGET, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, WALL, WALL, WALL, WALL, WALL},
        };
        assertThatLevelIsCorrect(sokoban, expected);
        assertThatPlayerIsAt(sokoban, new Point(2, 4));
        assertThatTreasuresAreAt(sokoban, new Point(2, 3), new Point(3, 3), new Point(3, 4));
    }

    /** Verifies that a level is validated. */
    @Test
    public void shouldValidateLevel() {
        assertThatThrownBy(() -> {
            StringLevelConverter converter = new AsciiStringLevelConverter();
            String[] fields = {
                    "######",
                    "#    #",
                    "# #  #",
                    "#  * #",
                    "# +* #",
                    "#    #",
                    "######"
            };
            converter.convert(fields);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    /** Verifies that an invalid character is not allowed. */
    @Test
    public void shouldThrowExceptionOnInvalidCharacter() {
        assertThatThrownBy(() -> {
            StringLevelConverter converter = new AsciiStringLevelConverter();
            String[] fields = {
                    "######",
                    "#  ? #",
                    "# #  #",
                    "# $* #",
                    "# +* #",
                    "#    #",
                    "######"
            };
            converter.convert(fields);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    /** Verifies that an invalid character is not allowed after a single colon. */
    @Test
    public void shouldThrowExceptionInvalidCharacterAfterColon() {
        assertThatThrownBy(() -> {
            StringLevelConverter converter = new AsciiStringLevelConverter();
            String[] fields = {
                    "######",
                    ":;",
                    "#    #",
                    "# #  #",
                    "# $* #",
                    "# +* #",
                    "#    #",
                    "######"
            };
            converter.convert(fields);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    /** Verifies that a single colon is not a comment. */
    @Test
    public void shouldThrowExceptionIfCommentWithOneColon() {
        assertThatThrownBy(() -> {
            StringLevelConverter converter = new AsciiStringLevelConverter();
            String[] fields = {
                    "######",
                    ":",
                    "#    #",
                    "# #  #",
                    "# $* #",
                    "# +* #",
                    "#    #",
                    "######"
            };
            converter.convert(fields);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}