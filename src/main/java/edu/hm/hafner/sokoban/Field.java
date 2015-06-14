package edu.hm.hafner.sokoban;

/**
 * Type of a game field.
 *
 * @author Ullrich Hafner
 */
public enum Field {
    /** Floor that can be entered by the player or a treasure. */
    FLOOR,
    /** Wall that cannot be entered by the player or a treasure. */
    WALL,
    /** Target for a treasure. Can be entered by the player or a treasure. */
    TARGET,
    /** Background. */
    BACKGROUND
}
