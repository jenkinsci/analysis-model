package edu.hm.hafner.sokoban;

/**
 * Represents the game field of Sokoban.
 *
 * @author Ullrich Hafner
 */
public class Sokoban implements SokobanGame {
    private Field[][] level;

    private int moves;

    private int width;
    private int height;

    private PointSet treasures = new PointSet();

    private Point player;
    private int targetCount;

    private final Point initialPlayer;
    private final PointSet initialTreasures;

    private Point lastPlayer;
    private PointSet lastTreasures;

    /**
     * Creates a new instance of {@link Sokoban}.
     *
     * @param level     the level
     * @param player    the initial player position
     * @param treasures the initial treasure positions
     *
     * @throws IllegalArgumentException if the level is not valid
     * @throws NullPointerException if a paramter is {@code null}
     */
    public Sokoban(final Field[][] level, final Point player, final PointSet treasures) {
        setLevel(level);
        setPlayer(player);
        setTreasures(treasures);
        validate();

        initialPlayer = player;
        initialTreasures = new PointSet(treasures);

        storeUndoState(initialPlayer, initialTreasures);

        moves = 0;
    }

    private void setLevel(final Field[][] level) {
        width = level[0].length;
        height = level.length;
        targetCount = 0;

        this.level = new Field[height][width];
        for (int y = 0; y < height; y++) {
            Field[] line = level[y];
            if (line.length != width) {
                throw new IllegalArgumentException(
                        String.format("Line %d has not the width %d of previous line.", y, width));
            }
            for (int x = 0; x < width; x++) {
                Field field = level[y][x];
                if (field == null) {
                    throw new NullPointerException("Field is null at " + new Point(x, y));
                }
                if (field == Field.TARGET) {
                    targetCount++;
                }
                this.level[y][x] = field;
            }
        }
    }

    private void setPlayer(final Point point) {
        if (point == null) {
            throw new NullPointerException("Player must not be null.");
        }

        player = point;
    }

    private void setTreasures(final PointSet treasures) {
        this.treasures = new PointSet(treasures);
    }

    /**
     * Validates this level.
     *
     * @throws IllegalArgumentException if the level is not valid
     */
    private void validate() throws IllegalArgumentException {
        ensureThatPlayerIsSet();
        ensureThatNoWallBelowPlayer();
        ensureThatTreasuresAreInField();
        ensureThatNoTreasureBelowPlayer();
        ensureThatNoWallBelowTreasures();
        ensureThatTreasuresAndTargetsMatch();
    }

    private void ensureThatNoTreasureBelowPlayer() {
        if (treasures.contains(player)) {
            throw new IllegalArgumentException("Player is on treasure: " + player);
        }
    }

    private void ensureThatTreasuresAndTargetsMatch() {
        if (targetCount != treasures.size()) {
            throw new IllegalArgumentException(
                    String.format("#Treasures (%d) !=  #Targets (%d)", treasures.size(), targetCount));
        }
    }

    private void ensureThatPlayerIsSet() {
        if (player == null) {
            throw new IllegalArgumentException("Player is not set!");
        }
        assertThatPointIsInField("Player", player);
    }

    private void assertThatPointIsInField(final String name, final Point point) {
        if (isOutside(point)) {
            throw new IllegalArgumentException(
                    String.format("%s %s is not set on field of size %dx%d: ", name, player, width, height));
        }
    }

    @SuppressWarnings("OverlyComplexBooleanExpression")
    private boolean isOutside(final Point point) {
        return point.getX() < 0 || point.getX() >= width
                || point.getY() < 0 || point.getY() >= height;
    }

    private void ensureThatNoWallBelowPlayer() {
        if (isWallAt(player)) {
            throw new IllegalArgumentException("Player is on wall: " + player);
        }
    }

    private void ensureThatTreasuresAreInField() {
        for (Point treasure : treasures) {
            assertThatPointIsInField("Treasure", treasure);
        }
    }

    private void ensureThatNoWallBelowTreasures() {
        for (Point treasure : treasures) {
            if (isWallAt(treasure)) {
                throw new IllegalArgumentException("Treasure is on wall: " + treasure);
            }
        }
    }

    private boolean isWallAt(final Point position) {
        return level[position.getY()][position.getX()] == Field.WALL;
    }

    /**
     * Returns whether this level has been solved. The level is solved, if each treasure covers a target.
     *
     * @return {@code true} if this level has been solved, {@code false} otherwise
     */
    public boolean isSolved() {
        for (Point treasure : treasures) {
            if (level[treasure.getY()][treasure.getX()] != Field.TARGET) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Field getField(final Point point) {
        return level[point.getY()][point.getX()];
    }

    @Override
    public Point getPlayer() {
        return player;
    }

    @Override
    public Iterable<Point> getTreasures() {
        return treasures;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void moveLeft() {
        Point beforePlayer = getPlayer();
        PointSet beforeTreasures = new PointSet(treasures);

        Point point = getPlayer().moveLeft();
        move(point, point.moveLeft());

        storePreviousState(beforePlayer, beforeTreasures);
    }

    @Override
    public void moveRight() {
        Point beforePlayer = getPlayer();
        PointSet beforeTreasures = new PointSet(treasures);

        Point point = getPlayer().moveRight();
        move(point, point.moveRight());

        storePreviousState(beforePlayer, beforeTreasures);
    }

    @Override
    public void moveUp() {
        Point beforePlayer = getPlayer();
        PointSet beforeTreasures = new PointSet(treasures);

        Point point = getPlayer().moveUp();
        move(point, point.moveUp());

        storePreviousState(beforePlayer, beforeTreasures);
    }

    @Override
    public void moveDown() {
        Point beforePlayer = getPlayer();
        PointSet beforeTreasures = new PointSet(treasures);

        Point point = getPlayer().moveDown();
        move(point, point.moveDown());

        storePreviousState(beforePlayer, beforeTreasures);
    }

    private void move(final Point current, final Point next) {
        if (isEmpty(current)) {
            move(current);
        }
        else if (containsTreasure(current) && isEmpty(next)) {
            move(current);
            moveTreasure(current, next);
        }
    }

    private void moveTreasure(final Point from, final Point to) {
        treasures.remove(from);
        treasures.add(to);
    }

    private boolean containsTreasure(final Point point) {
        return treasures.contains(point);
    }

    private void move(final Point current) {
        setPlayer(current);
        if (!isSolved()) {
            moves++;
        }
    }

    private boolean isEmpty(final Point current) {
        return getField(current) != Field.WALL && !containsTreasure(current);
    }

    @Override
    public String toString() {
        if (isSolved()) {
            return "Solved in " + moves + " moves!";
        }
        else {
            return moves + " moves...";
        }
    }

    @Override
    public void restart() {
        resetPlayerAndTreasures(initialPlayer, initialTreasures);
    }

    private void resetPlayerAndTreasures(final Point resetPlayer, final PointSet resetTreasures) {
        setPlayer(resetPlayer);
        treasures.setElements(resetTreasures);
    }

    private void storePreviousState(final Point beforePlayer, final PointSet beforeTreasures) {
        if (!beforePlayer.equals(player)) {
            storeUndoState(beforePlayer, beforeTreasures);
        }
    }

    private void storeUndoState(final Point beforePlayer, final PointSet beforeTreasures) {
        lastPlayer = beforePlayer;
        lastTreasures = new PointSet(beforeTreasures);
    }

    @Override
    public void undo() {
        Point undoPlayer = getPlayer();
        PointSet undoTreasures = new PointSet(treasures);

        resetPlayerAndTreasures(lastPlayer, lastTreasures);

        lastPlayer = undoPlayer;
        lastTreasures = undoTreasures;
    }
}