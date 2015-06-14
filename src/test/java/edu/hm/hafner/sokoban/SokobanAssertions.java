package edu.hm.hafner.sokoban;

import java.util.Set;

import com.google.common.collect.Sets;

import static org.assertj.core.api.Assertions.*;

/**
 * Provides several assertions to simplify testing of a Sokoban level.
 *
 * @author Ullrich Hafner
 */
public final class SokobanAssertions {
    private SokobanAssertions() {
        // prevents instantiation
    }

    /**
     * Verifies that the treasures of the given Sokoban game are at the expected position.
     *
     * @param sokoban   the subject under test
     * @param treasures the expected treasure positions
     */
    public static void assertThatTreasuresAreAt(final SokobanGame sokoban, final Point... treasures) {
        Set<Point> actualTreasures = Sets.newHashSet(sokoban.getTreasures());
        Set<Point> expectedTreasures = Sets.newHashSet(treasures);
        assertThat(actualTreasures).as("Wrong set of treasures").isEqualTo(expectedTreasures);
    }

    /**
     * Verifies that the player of the given Sokoban game is at the expected position.
     *
     * @param sokoban the subject under test
     * @param player  the expected player position
     */
    public static void assertThatPlayerIsAt(final SokobanGame sokoban, final Point player) {
        assertThat(sokoban.getPlayer()).as("Player in not at position " + player).isEqualTo(player);
    }

    /**
     * Verifies that the Sokoban level is correctly set.
     *
     * @param sokoban the subject under test
     * @param level   the expected fields of the level
     */
    public static void assertThatLevelIsCorrect(final SokobanGame sokoban, final Field[][] level) {
        for (int y = 0; y < sokoban.getHeight(); y++) {
            for (int x = 0; x < sokoban.getWidth(); x++) {
                assertThat(sokoban.getField(new Point(x, y)))
                        .as("Field (%d, %d)", x, y)
                        .isEqualTo(level[y][x]);
            }
        }
    }

}
