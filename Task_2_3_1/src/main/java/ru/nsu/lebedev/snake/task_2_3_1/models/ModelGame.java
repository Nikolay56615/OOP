package ru.nsu.lebedev.snake.task_2_3_1.models;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.lebedev.snake.task_2_3_1.game.GameAppleList;
import ru.nsu.lebedev.snake.task_2_3_1.game.GamePoint;
import ru.nsu.lebedev.snake.task_2_3_1.game.GameSnake;
import ru.nsu.lebedev.snake.task_2_3_1.game.GameVector;

/**
 * Represents the game model, managing the game state, including the snake, apples, and field.
 * <p>
 * This class is responsible for updating the game state, handling game logic such as snake
 * movement, apple spawning, and checking if the game is over. It also provides methods to retrieve
 * game data like score and available field cells.
 */
public class ModelGame implements ModelContract {

    private int currentFieldWidth;
    private int currentFieldHeight;
    private final List<GamePoint> fieldPoints = new ArrayList<>();
    private static final int INITIAL_SNAKE_SIZE = 1;
    private GameSnake snake;
    private GameAppleList apples;

    /**
     * Restarts the game model by resetting the field dimensions, constructing the field points,
     * creating the snake, and adding apples.
     *
     * @return the current instance of the ModelGame with the restarted state.
     */
    @Override
    public ModelGame restartModel() {
        var settingsModel = (ModelSettings) ModelEnum.SETTINGS.get();
        currentFieldWidth = settingsModel.getFieldWidth();
        currentFieldHeight = settingsModel.getFieldHeight();
        constructFieldPoints();

        snake = new GameSnake(
            INITIAL_SNAKE_SIZE,
            new GamePoint(currentFieldWidth / 2, currentFieldHeight / 2),
            GameVector.RIGHT, this
        );

        apples = new GameAppleList(this);
        apples.addRandomApples(settingsModel.getApplesCount());

        return this;
    }

    /**
     * Initializes the field points by creating a list of all possible points in the game field.
     */
    private void constructFieldPoints() {
        fieldPoints.clear();
        for (int x = 0; x < currentFieldWidth; ++x) {
            for (int y = 0; y < currentFieldHeight; ++y) {
                fieldPoints.add(new GamePoint(x, y));
            }
        }
    }

    /**
     * Returns a copy of the list of all field points.
     * <p>
     * This method provides a duplicate of the current field points list, ensuring that changes to
     * the copy do not affect the original list.
     *
     * @return A list containing copies of all field points.
     */
    private List<GamePoint> getFieldPointsCopy() {
        List<GamePoint> copy = new ArrayList<>();
        for (var point : fieldPoints) {
            copy.add(point.copy());
        }
        return copy;
    }

    /**
     * Retrieves a list of non-lethal points on the field, i.e., points that are not occupied by the
     * snake's body.
     *
     * @param snake The snake to check against.
     * @return A list of points that are safe for the snake to move into.
     */
    public List<GamePoint> getNonKillingCells(GameSnake snake) {
        List<GamePoint> fieldCopy = getFieldPointsCopy();
        fieldCopy.removeAll(snake.getBody());
        return fieldCopy;
    }

    /**
     * Retrieves a list of free cells on the field where objects (like apples) can be placed.
     * <p>
     * Free cells are those that are not occupied by the snake or apples.
     *
     * @return A list of available cells for object placement.
     */
    public List<GamePoint> getFreeFieldCells() {
        List<GamePoint> fieldCopy = getFieldPointsCopy();
        fieldCopy.removeAll(snake.getWholeBody());
        fieldCopy.removeAll(apples.getApples());
        return fieldCopy;
    }

    /**
     * Retrieves the current score, which is equivalent to the snake's current size.
     *
     * @return The current score of the game (snake size).
     */
    public int getScore() {
        return snake.getSize();
    }

    /**
     * Updates the game state, executing necessary actions such as moving the snake and checking for
     * apple collisions.
     */
    public void update() {
        snake.move();
        if (apples.checkSnakeGrowth(snake)) {
            apples.addRandomApple();
        }
    }

    /**
     * Checks whether the game is over. The game ends if the snake dies.
     *
     * @return True if the game is over (i.e., the snake is dead), otherwise false.
     */
    public boolean isGameOver() {
        return snake.isDead();
    }

    // Getters for the fields

    /**
     * Retrieves the current width of the game field.
     *
     * @return The current width of the field.
     */
    public int getCurrentFieldWidth() {
        return currentFieldWidth;
    }

    /**
     * Retrieves the current height of the game field.
     *
     * @return The current height of the field.
     */
    public int getCurrentFieldHeight() {
        return currentFieldHeight;
    }

    /**
     * Retrieves the current snake object in the game.
     *
     * @return The snake object.
     */
    public GameSnake getSnake() {
        return snake;
    }

    /**
     * Retrieves the list of apples currently on the field.
     *
     * @return The apples object containing all apples on the field.
     */
    public GameAppleList getApples() {
        return apples;
    }
}
