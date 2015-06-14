package edu.hm.hafner.sokoban;

/**
 * Converts an array of strings to a new Sokoban level.
 *
 * @author Ullrich Hafner
 */
public interface StringLevelConverter {
    /**
     * Converts the array of strings to a Sokoban level.
     *
     * @param lines the rows of the level
     * @return the created level
     * @throws IllegalArgumentException if the level is not valid
     */
    SokobanGame convert(String[] lines) throws IllegalArgumentException;
}
