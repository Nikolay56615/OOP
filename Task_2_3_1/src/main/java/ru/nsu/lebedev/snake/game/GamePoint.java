package ru.nsu.lebedev.snake.game;

import java.util.List;
import java.util.Optional;

/**
 * Represents a point in a 2D coordinate system.
 */
public class GamePoint {

    private Integer x;
    private Integer y;

    /**
     * Constructs a point with the given coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public GamePoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate.
     *
     * @return The x value.
     */
    public Integer getX() {
        return x;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return The y value.
     */
    public Integer getY() {
        return y;
    }

    /**
     * Moves the point in the specified direction.
     *
     * @param vector The movement vector.
     */
    public void move(GameVector vector) {
        x += vector.getOffset().getX();
        y += vector.getOffset().getY();
    }

    /**
     * Moves the point within specified boundaries (wraps around when exceeding limits).
     *
     * @param vector The movement vector.
     * @param minX   Minimum x boundary.
     * @param maxX   Maximum x boundary.
     * @param minY   Minimum y boundary.
     * @param maxY   Maximum y boundary.
     */
    public void move(GameVector vector, int minX, int maxX, int minY, int maxY) {
        x += vector.getOffset().getX();
        if (x < minX) {
            x = maxX;
        } else if (x > maxX) {
            x = minX;
        }

        y += vector.getOffset().getY();
        if (y < minY) {
            y = maxY;
        } else if (y > maxY) {
            y = minY;
        }
    }

    /**
     * Creates a copy of this point.
     *
     * @return A new Point instance with the same coordinates.
     */
    public GamePoint copy() {
        return new GamePoint(x, y);
    }

    /**
     * Checks if this point is present in a given list.
     *
     * @param list The list to check.
     * @return True if the point exists in the list, false otherwise.
     */
    public boolean isInList(List<GamePoint> list) {
        if (list == null) {
            return false;
        }
        return list.contains(this);
    }

    /**
     * Finds the index of this point in a given list.
     *
     * @param list The list to check.
     * @return The index of the first occurrence wrapped in an Optional, or Optional.empty() if not
     * found.
     */
    public Optional<Integer> getListCollision(List<GamePoint> list) {
        if (list == null) {
            return Optional.empty();
        }
        for (int i = 0; i < list.size(); ++i) {
            if (this.equals(list.get(i))) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GamePoint point = (GamePoint) obj;
        return x.equals(point.x) && y.equals(point.y);
    }
}