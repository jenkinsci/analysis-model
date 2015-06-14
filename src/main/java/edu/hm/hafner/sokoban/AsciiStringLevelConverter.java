package edu.hm.hafner.sokoban;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.util.Point;

/**
 * Reads and creates a Sokoban level in ASCII format.
 *
 * @author Ullrich Hafner
 */
public class AsciiStringLevelConverter implements StringLevelConverter {
    private static final String COMMENT = "::";

    @Override @SuppressWarnings({"IfStatementWithTooManyBranches", "PMD.CyclomaticComplexity"})
    public SokobanGame convert(final String[] lines) {
        int width = computeWidth(lines);
        int height = computeHeight(lines);

        Field[][] game = new Field[height][width];
        Point player = null;
        PointSet treasures = new PointSet();

        int y = 0;
        for (String line : lines) {
            if (hasContent(line)) {
                boolean isLineBeginning = false;
                for (int x = 0; x < line.length(); x++) {
                    char fileElement = line.charAt(x);
                    Field field;
                    if (fileElement == '#') {
                        field = Field.WALL;
                        isLineBeginning = true;
                    }
                    else if (fileElement == '.') {
                        field = Field.TARGET;
                    }
                    else if (fileElement == '@') {
                        field = Field.FLOOR;
                        player = new Point(x, y);
                    }
                    else if (fileElement == '+') {
                        field = Field.TARGET;
                        player = new Point(x, y);
                    }
                    else if (fileElement == '$') {
                        field = Field.FLOOR;
                        treasures.add(new Point(x, y));
                    }
                    else if (fileElement == '*') {
                        field = Field.TARGET;
                        treasures.add(new Point(x, y));
                    }
                    else  if (fileElement == ' ') {
                        if (isLineBeginning) {
                            field = Field.FLOOR;
                        }
                        else {
                            field = Field.BACKGROUND;
                        }
                    }
                    else {
                        throw new IllegalArgumentException(String.format(
                                "Illegal character detected at position (%d, %d): %c", x, y, fileElement));
                    }
                    game[y][x] = field;
                }
                for (int x = line.length(); x < width; x++) {
                    game[y][x] = Field.BACKGROUND;
                }
                y++;
            }
        }
        return new Sokoban(game, player, treasures);
    }

    private int computeWidth(final String[] lines) {
        int width = 0;
        for (String line : lines) {
            if (isNoComment(line)) {
                width = Math.max(width, line.length());
            }
        }
        return width;
    }

    private int computeHeight(final String[] lines) {
        int height = 0;
        for (String line : lines) {
            if (hasContent(line)) {
                height++;
            }
        }
        return height;
    }

    private boolean hasContent(final String line) {
        return isNoComment(line) && StringUtils.isNotBlank(line);
    }

    private boolean isNoComment(final String line) {
        return !line.trim().startsWith(COMMENT);
    }
}
