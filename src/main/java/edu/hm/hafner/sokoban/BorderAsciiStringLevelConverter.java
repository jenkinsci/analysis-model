package edu.hm.hafner.sokoban;

/**
 * Reads and creates a Sokoban level in ASCII format. Each level is decorated with a border using {@link
 * Field#BACKGROUND}.
 *
 * @author Ullrich Hafner
 */
public class BorderAsciiStringLevelConverter extends AsciiStringLevelConverter {
    @Override
    public SokobanGame convert(final String[] lines) {
        SokobanGame game = super.convert(lines);

        PointSet treasures = new PointSet();
        for (Point treasure : game.getTreasures()) {
            treasures.add(translate(treasure));
        }

        return new Sokoban(createBorder(game), translate(game.getPlayer()), treasures);
    }

    private Field[][] createBorder(final SokobanGame game) {
        int height = game.getHeight() + 2;
        int width = game.getWidth() + 2;
        Field[][] fields = new Field[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isBorderPixel(x, y, width, height)) {
                    fields[y][x] = Field.BACKGROUND;
                }
                else {
                    fields[y][x] = game.getField(new Point(x - 1, y - 1));
                }
            }
        }
        return fields;
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    private boolean isBorderPixel(final int x, final int y, final int width, final int height) {
        return x == 0 || x == width - 1 || y == 0 || y == height - 1;
    }

    private Point translate(final Point treasure) {
        return treasure.moveDown().moveRight();
    }
}
