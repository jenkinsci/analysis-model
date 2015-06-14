package edu.hm.hafner.sokoban;

import org.junit.Test;

import edu.hm.hafner.util.Point;
import static edu.hm.hafner.sokoban.Field.*;
import static edu.hm.hafner.sokoban.SokobanAssertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Sokoban}.
 */
public class SokobanTest {
    private static final Point MIDDLE = new Point(3, 3);
    private static final Point LEFT = new Point(2, 3);
    private static final Point RIGHT = new Point(4, 3);
    private static final Point UP = new Point(3, 2);
    private static final Point DOWN = new Point(3, 4);

    /** Verifies that a treasure can be moved. */
    @Test
    public void shouldMoveTreasureToTarget() {
        SokobanGame sokoban = createLevel();
        assertThatPlayerIsAt(sokoban, new Point(3, 4));
        assertThatTreasuresAreAt(sokoban, new Point(2, 4), new Point(4, 5));

        sokoban.moveLeft();
        assertThatPlayerIsAt(sokoban, new Point(3, 4));

        sokoban.moveDown();
        assertThatPlayerIsAt(sokoban, new Point(3, 5));

        sokoban.moveLeft();
        assertThatPlayerIsAt(sokoban, new Point(2, 5));

        sokoban.moveUp();
        assertThatPlayerIsAt(sokoban, new Point(2, 4));
        assertThatTreasuresAreAt(sokoban, new Point(2, 3), new Point(4, 5));

        sokoban.moveRight();
        sokoban.moveRight();
        sokoban.moveRight();
        sokoban.moveDown();
        sokoban.moveLeft();
        assertThatPlayerIsAt(sokoban, new Point(4, 5));
        assertThatTreasuresAreAt(sokoban, new Point(2, 3), new Point(3, 5));
    }

    /**
     * Verifies that the player move methods work in the corner.
     */
    @Test
    public void shouldDetectCorners() {
        // Given
        Field[][] fields = {
                {WALL, WALL, WALL, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, WALL, WALL, WALL},
        };
        Point player = new Point(1, 1);
        SokobanGame sokoban = new Sokoban(fields, player, new PointSet(new Point(2, 1)));

        // When and Then
        sokoban.moveUp();
        assertThatPlayerIsAt(sokoban, player);

        // When and Then
        sokoban.moveDown();
        assertThatPlayerIsAt(sokoban, player);

        // When and Then
        sokoban.moveLeft();
        assertThatPlayerIsAt(sokoban, player);
    }

    private SokobanGame createLevel() {
        return new Sokoban(getLevel(), new Point(3, 4),
                new PointSet(new Point(2, 4), new Point(4, 5)));
    }

    /** Verifies that null values are not stored. */
    @Test
    public void shouldThrowNpeIfLevelIsNull() {
        assertThatThrownBy(() -> {
            // Given When
            new Sokoban(null, new Point(3, 4),
                    new PointSet(new Point(2, 4), new Point(4, 5)));
        }).isInstanceOf(NullPointerException.class); // Then
    }

    private Field[][] getLevel() {
        return new Field[][]{
                        {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
                        {BACKGROUND, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                        {BACKGROUND, WALL, FLOOR, TARGET, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                        {BACKGROUND, WALL, FLOOR, FLOOR, WALL, WALL, WALL, BACKGROUND},
                        {BACKGROUND, WALL, TARGET, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                        {BACKGROUND, WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL, BACKGROUND},
                        {BACKGROUND, WALL, FLOOR, FLOOR, WALL, WALL, WALL, BACKGROUND},
                        {BACKGROUND, WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND, BACKGROUND},
                        {BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND, BACKGROUND},
                };
    }

    /** Verifies that null values are not stored. */
    @Test
    public void shouldThrowNpeIfFieldIsNull() {
        assertThatThrownBy(() -> {
            // Given
            Field[][] level = getLevel();
            level[0][0] = null;  // NOPMD

            // When
            new Sokoban(level, new Point(3, 4),
                    new PointSet(new Point(2, 4), new Point(4, 5)));
        }).isInstanceOf(NullPointerException.class); // Then
    }

    /** Verifies that null values are not stored. */
    @Test
    public void shouldThrowNpeIfPlayerIsNull() {
        assertThatThrownBy(() -> {
            // Given When
            new Sokoban(getLevel(), null,
                    new PointSet(new Point(2, 4), new Point(4, 5)));
        }).isInstanceOf(NullPointerException.class); // Then
    }

    /** Verifies that null values are not stored. */
    @Test
    public void shouldThrowNpeIfTreasuresAreNull() {
        assertThatThrownBy(() -> {
            // Given When
            new Sokoban(getLevel(), new Point(3, 4), null);
        }).isInstanceOf(NullPointerException.class); // Then
    }

    /** Verifies that null values are not stored. */
    @Test
    public void shouldThrowNpeIfATreasureIsNull() {
        assertThatThrownBy(() -> {
            // Given When
            new Sokoban(getLevel(), new Point(3, 4),
                    new PointSet(new Point(2, 4), null));
        }).isInstanceOf(NullPointerException.class); // Then
    }

    /** Verifies that the player is not placed on a wall. */
    @Test
    public void shouldDetectInvalidWorldWithPlayerOnWall() {
        assertThatThrownBy(() -> {
            // Given When
            createSokobanWithOneTreasure(new Point(0, 0), new Point(1, 2));
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that no treasure is placed on a wall. */
    @Test
    public void shouldDetectInvalidWorldWithTreasureOnWall() {
        assertThatThrownBy(() -> {
            // Given When
            createSokobanWithOneTreasure(new Point(1, 1), new Point(0, 0));
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the player is not placed on a treasure. */
    @Test
    public void shouldDetectInvalidWorldWithPlayerOnTreasure() {
        assertThatThrownBy(() -> {
            // Given When
            createSokobanWithOneTreasure(new Point(1, 1), new Point(1, 1));
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the number of targets is equal to the number of treasures. */
    @Test
    public void shouldDetectInvalidWorldThatHasNotEnoughTargets() {
        assertThatThrownBy(() -> {
            // Given When
            createSokobanWithOneTreasure(new Point(2, 3), new Point(3, 3), new Point(3, 4));
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the number of targets is equal to the number of treasures. */
    @Test
    public void shouldDetectInvalidWorldThatHasNotEnoughTreasures() {
        assertThatThrownBy(() -> {
            // Given When
            createSokobanWithOneTreasure(new Point(2, 3));
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that the field array is regular, i.e. the size of each line is the width. */
    @Test
    public void shouldDetectIrregularArray() {
        assertThatThrownBy(() -> {
            // Given
            new Sokoban(createIrregularArray(), new Point(1, 1), new PointSet(new Point(1, 2)));
        }).isInstanceOf(IllegalArgumentException.class); // Then
    }

    /** Verifies that duplicate treasures are skipped. */
    @Test
    public void shouldSkipDuplicateTreasure() {
        // Given
        Sokoban sokoban = createSokobanWithOneTreasure(new Point(1, 1), new Point(1, 2), new Point(1, 2));

        // Whem
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isFalse();
    }

    /** Verifies that setting a player outside of the visible level is correctly detected. */
    @Test
    public void shouldDetectPlayerOutsideOfLevel() {
        shouldDetectPlayersOutsideOfField(
                new Point(-1, 0), new Point(0, -1), // Links oben
                new Point(2, 0), new Point(1, -1), // Rechts oben
                new Point(2, 1), new Point(1, 2),  // Rechts unten
                new Point(-1, 1), new Point(0, 2)   // Links unten
        );
    }

    private void shouldDetectPlayersOutsideOfField(final Point... outsidePlayers) {
        for (Point outsidePoint : outsidePlayers) {
            assertThatThrownBy(() -> {
                // Given When
                new Sokoban(new Field[][]{
                        {WALL, WALL},
                        {WALL, TARGET},
                }, outsidePoint, new PointSet(new Point(1, 1)));
            }).isInstanceOf(IllegalArgumentException.class); // Then
        }
    }

    /** Verifies that setting a treasure outside of the visible level is correctly detected. */
    @Test
    public void shouldDetectTreasureOutsideOfLevel() {
        shouldDetectTreasuresOutsideOfField(
                new Point(-1, 0), new Point(0, -1), // Links oben
                new Point(2, 0), new Point(1, -1), // Rechts oben
                new Point(2, 1), new Point(1, 2),  // Rechts unten
                new Point(-1, 1), new Point(0, 2)   // Links unten
        );
    }

    private void shouldDetectTreasuresOutsideOfField(final Point... outsidePlayers) {
        for (Point outsidePoint : outsidePlayers) {
            assertThatThrownBy(() -> {
                // Given When
                new Sokoban(new Field[][]{
                        {WALL, WALL},
                        {WALL, TARGET},
                }, new Point(1, 1), new PointSet(outsidePoint));
            }).isInstanceOf(IllegalArgumentException.class); // Then
        }
    }

    /** Verifies that a valid world with one treasure is correctly detected. */
    @Test
    public void shouldValidateCorrectWorldWithOneTreasure() {
        // Given
        Point player = new Point(2, 3);
        Point treasure = new Point(3, 4);
        Sokoban sokoban = createSokobanWithOneTreasure(player, treasure);

        // When
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isFalse();

        assertThatPlayerIsAt(sokoban, player);
        assertThatTreasuresAreAt(sokoban, treasure);
        assertThatLevelIsCorrect(sokoban, createLevelWithOneTreasure());
    }

    /** Verifies that a valid world with two treasures is correctly detected. */
    @Test
    public void shouldValidateCorrectWorldWithTwoTreasures() {
        // Given
        Point player = new Point(1, 1);
        Point treasure1 = new Point(1, 2);
        Point treasure2 = new Point(1, 3);
        Sokoban sokoban = createSokobanWithTwoTreasures(player, treasure1, treasure2);

        // When
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isFalse();

        assertThatPlayerIsAt(sokoban, player);
        assertThatTreasuresAreAt(sokoban, treasure1, treasure2);
        assertThatLevelIsCorrect(sokoban, createLevelWithTwoTreasures());
    }

    /** Verifies that a level has been solved. */
    @Test
    public void shouldDetectSolvedWorldWithOneTreasure() {
        // Given
        Sokoban sokoban = createSokobanWithOneTreasure(new Point(2, 3), new Point(2, 1));

        // When
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isTrue();

        // Then
        assertThat(sokoban.isSolved()).isTrue();
    }

    /** Verifies that a level has been solved. */
    @Test
    public void shouldDetectSolvedWorldWithTwoTreasures() {
        // Given
        Sokoban sokoban = createSokobanWithTwoTreasures(new Point(1, 1), new Point(2, 1), new Point(2, 2));

        // When
        boolean isSolved = sokoban.isSolved();

        // Then
        assertThat(isSolved).isTrue();
    }

    /** Verifies that a level will be copied internally. */
    @Test
    public void shouldCopyLevel() {
        // Given
        Field[][] level = createLevelWithOneTreasure();
        Sokoban sokoban = new Sokoban(level, new Point(2, 3), new PointSet(new Point(2, 1)));

        // When
        fillArrayWithTarget(level);

        // Then
        assertThat(sokoban.isSolved()).isTrue();
    }

    private Sokoban createSokobanWithOneTreasure(final Point player, final Point... treasures) {
        return new Sokoban(createLevelWithOneTreasure(), player, new PointSet(treasures));
    }

    private Sokoban createSokobanWithTwoTreasures(final Point player, final Point... treasures) {
        return new Sokoban(createLevelWithTwoTreasures(), player, new PointSet(treasures));
    }

    private void fillArrayWithTarget(final Field[][] level) {
        for (Field[] fields : level) {
            for (int i = 0; i < fields.length; i++) {
                fields[i] = TARGET;
            }
        }
    }

    private Field[][] createIrregularArray() {
        return new Field[][]{
                {WALL, WALL, WALL, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, FLOOR, FLOOR, WALL, WALL, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, WALL},
                {WALL, WALL, WALL, WALL},
        };
    }

    private Field[][] createLevelWithOneTreasure() {
        return new Field[][]{
                {WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
                {WALL, FLOOR, TARGET, WALL, BACKGROUND, BACKGROUND},
                {WALL, FLOOR, FLOOR, WALL, WALL, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, WALL, BACKGROUND, BACKGROUND},
                {WALL, WALL, WALL, WALL, BACKGROUND, BACKGROUND},
        };
    }

    private Field[][] createLevelWithTwoTreasures() {
        return new Field[][]{
                {WALL, WALL, WALL, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, FLOOR, TARGET, WALL},
                {WALL, FLOOR, FLOOR, WALL},
                {WALL, WALL, WALL, WALL},
        };
    }


    /**
     * Verifies that we can reset to the initial state. The player moves into each direction, moving all treasures
     * to the neighbor field.
     */
    @Test
    public void shouldResetAllMovements() {
        SokobanGame sokoban = createUndoableLevel();

        sokoban.moveLeft();
        sokoban.moveRight();

        sokoban.moveRight();
        sokoban.moveLeft();

        sokoban.moveUp();
        sokoban.moveDown();

        sokoban.moveDown();

        assertThatPlayerIsAt(sokoban, MIDDLE.moveDown());
        assertThatTreasuresAreAt(sokoban, LEFT.moveLeft(), UP.moveUp(), DOWN.moveDown(), RIGHT.moveRight());

        sokoban.restart();
        assertThatLevelIsInInitialState(sokoban);
    }

    /**
     * Verifies that the first movement is undoable.
     */
    @Test
    public void shouldUndoFirstMovement() {
        SokobanGame sokoban = createUndoableLevel();
        assertThatLevelIsInInitialState(sokoban);

        moveLeft(sokoban);
        assertThatUndoRestoresToInitialState(sokoban);

        moveRight(sokoban);
        assertThatUndoRestoresToInitialState(sokoban);

        moveDown(sokoban);
        assertThatUndoRestoresToInitialState(sokoban);

        moveUp(sokoban);
        assertThatUndoRestoresToInitialState(sokoban);
    }

    /**
     * Verifies that the second movement is undoable.
     */
    @Test
    public void shouldUndoSecondMovement() {
        SokobanGame sokoban = createUndoableLevel();

        sokoban.moveLeft();
        sokoban.moveRight();
        sokoban.undo();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveLeft());
        assertThatTreasuresAreAt(sokoban, LEFT.moveLeft(), UP, DOWN, RIGHT);
        sokoban.moveRight();

        sokoban.moveRight();
        sokoban.moveLeft();
        sokoban.undo();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveRight());
        assertThatTreasuresAreAt(sokoban, LEFT.moveLeft(), UP, DOWN, RIGHT.moveRight());
        sokoban.moveLeft();

        sokoban.moveDown();
        sokoban.moveUp();
        sokoban.undo();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveDown());
        assertThatTreasuresAreAt(sokoban, LEFT.moveLeft(), UP, DOWN.moveDown(), RIGHT.moveRight());
        sokoban.moveUp();

        sokoban.moveUp();
        sokoban.moveDown();
        sokoban.undo();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveUp());
        assertThatTreasuresAreAt(sokoban, LEFT.moveLeft(), UP.moveUp(), DOWN.moveDown(), RIGHT.moveRight());
    }

    /**
     * Verifies that undo does nothing in the beginning.
     */
    @Test
    public void shouldSkipUndoIfInInitialState() {
        SokobanGame sokoban = createUndoableLevel();

        assertThatUndoRestoresToInitialState(sokoban);
    }

    /**
     * Verifies that the undo state is not changed if the player actually did not move because of an obstacle.
     */
    @Test
    public void shouldSkipMovementIfNothingChanged() {
        SokobanGame sokoban = createUndoableLevel();

        moveLeft(sokoban);
        moveLeft(sokoban);
        assertThatUndoRestoresToInitialState(sokoban);

        moveRight(sokoban);
        moveRight(sokoban);
        assertThatUndoRestoresToInitialState(sokoban);

        moveDown(sokoban);
        moveDown(sokoban);
        assertThatUndoRestoresToInitialState(sokoban);

        moveUp(sokoban);
        moveUp(sokoban);
        assertThatUndoRestoresToInitialState(sokoban);
    }

    /**
     * Verifies that undo itself is undoable.
     */
    @Test
    public void shouldUndoUndo() {
        SokobanGame sokoban = createUndoableLevel();

        sokoban.moveLeft();
        assertThatUndoRestoresToInitialState(sokoban);
        sokoban.undo();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveLeft());
        assertThatTreasuresAreAt(sokoban, LEFT.moveLeft(), UP, DOWN, RIGHT);
        assertThatUndoRestoresToInitialState(sokoban);
    }

    private void moveUp(final SokobanGame sokoban) {
        sokoban.moveUp();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveUp());
        assertThatTreasuresAreAt(sokoban, LEFT, UP.moveUp(), DOWN, RIGHT);
    }

    private void moveDown(final SokobanGame sokoban) {
        sokoban.moveDown();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveDown());
        assertThatTreasuresAreAt(sokoban, LEFT, UP, DOWN.moveDown(), RIGHT);
    }

    private void moveRight(final SokobanGame sokoban) {
        sokoban.moveRight();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveRight());
        assertThatTreasuresAreAt(sokoban, LEFT, UP, DOWN, RIGHT.moveRight());
    }

    private void moveLeft(final SokobanGame sokoban) {
        sokoban.moveLeft();
        assertThatPlayerIsAt(sokoban, MIDDLE.moveLeft());
        assertThatTreasuresAreAt(sokoban, LEFT.moveLeft(), UP, DOWN, RIGHT);
    }

    private void assertThatUndoRestoresToInitialState(final SokobanGame sokoban) {
        sokoban.undo();
        assertThatLevelIsInInitialState(sokoban);
    }

    private void assertThatLevelIsInInitialState(final SokobanGame sokoban) {
        assertThatPlayerIsAt(sokoban, MIDDLE);
        assertThatTreasuresAreAt(sokoban, LEFT, UP, DOWN, RIGHT);
    }

    private SokobanGame createUndoableLevel() {
        Field[][] fields = {
                {WALL, WALL, WALL, WALL, WALL, WALL, WALL},
                {WALL, FLOOR, FLOOR, TARGET, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, TARGET, FLOOR, FLOOR, FLOOR, TARGET, WALL},
                {WALL, FLOOR, FLOOR, FLOOR, FLOOR, FLOOR, WALL},
                {WALL, FLOOR, FLOOR, TARGET, FLOOR, FLOOR, WALL},
                {WALL, WALL, WALL, WALL, WALL, WALL, WALL},
        };
        return new Sokoban(fields, MIDDLE, new PointSet(LEFT, RIGHT, UP, DOWN));
    }
}