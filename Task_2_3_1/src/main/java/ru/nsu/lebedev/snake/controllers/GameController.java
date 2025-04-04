package ru.nsu.lebedev.snake.controllers;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import ru.nsu.lebedev.snake.game.GamePoint;
import ru.nsu.lebedev.snake.game.GameVector;
import ru.nsu.lebedev.snake.models.ModelEnum;
import ru.nsu.lebedev.snake.models.ModelGame;
import ru.nsu.lebedev.snake.scenes.GameView;
import ru.nsu.lebedev.snake.scenes.SceneEnum;
import ru.nsu.lebedev.snake.scenes.ScenesControllerContract;
import ru.nsu.lebedev.snake.scenes.ScenesManager;

/**
 * Controller for the Game scene. This class manages the game loop, handles key input events for
 * controlling the snake, and updates the game view accordingly.
 */
public class GameController implements ScenesControllerContract {

    private ScenesManager sceneManager;

    @FXML
    private Label scores;

    @FXML
    private GridPane fieldGrid;

    private boolean updatedAfterKeyPressed = false;
    private ModelGame gameModel;
    private GameView gameView;

    /**
     * Animation timer that updates the game state at a fixed interval.
     */
    private final AnimationTimer animationTimer = new AnimationTimer() {
        public static final long UPDATE_MS = 50;
        private long lastUpdateTimestamp = 0;

        @Override
        public void handle(long nanoSecTimestamp) {
            long msTimestamp = nanoSecTimestamp / 1_000_000;
            if (msTimestamp - lastUpdateTimestamp < UPDATE_MS) {
                return;
            }
            updateSnakeCells();
            lastUpdateTimestamp = msTimestamp;
        }
    };

    /**
     * Sets the ScenesManager, initializes the game model and view, and starts the animation timer.
     *
     * @param sceneManager the ScenesManager instance to manage scene transitions.
     */
    @Override
    public void setSceneManager(ScenesManager sceneManager) {
        this.sceneManager = sceneManager;
        this.gameModel = ModelEnum.GAME.get().restartModel();
        this.gameView = new GameView(
            fieldGrid,
            gameModel.getCurrentFieldHeight(),
            gameModel.getCurrentFieldWidth()
        );

        scores.textProperty().bind(Bindings.convert(gameModel.scoreProperty()));

        sceneManager.getCurrentScene().setOnKeyPressed(this::keyHandler);
        sceneManager.getCurrentScene().widthProperty();
        animationTimer.start();
    }

    /**
     * Handles key events for controlling the snake. Arrow keys change the snake's direction; the
     * ESC key ends the game.
     *
     * @param event the key event.
     */
    private void keyHandler(KeyEvent event) {
        if (!updatedAfterKeyPressed) {
            return;
        }
        GameVector direction = switch (event.getCode()) {
            case UP -> GameVector.UP;
            case RIGHT -> GameVector.RIGHT;
            case DOWN -> GameVector.DOWN;
            case LEFT -> GameVector.LEFT;
            default -> null;
        };

        if (direction != null) {
            gameModel.getSnake().setDirection(direction);
            updatedAfterKeyPressed = false;
        }
    }

    /**
     * Updates the game state: repaints cells, updates the model, and checks for game over
     * condition.
     */
    private void updateSnakeCells() {
        GamePoint currentHead = gameModel.getSnake().getHead();
        gameView.setCellColor(currentHead, GameView.CellColor.SNAKE);

        GamePoint tail = gameModel.getSnake().getTail();
        gameView.setCellColor(tail, GameView.CellColor.FIELD);

        gameModel.update();
        if (gameModel.isGameOver()) {
            gameOver();
            return;
        }
        if (gameModel.getSnake().getSize()
            == gameModel.getCurrentFieldWidth() * gameModel.getCurrentFieldHeight()) {
            win();
            return;
        }

        GamePoint newHead = gameModel.getSnake().getHead();
        gameView.setCellColor(newHead, GameView.CellColor.SNAKE_HEAD);

        for (GamePoint apple : gameModel.getApples()) {
            gameView.setCellColor(apple, GameView.CellColor.APPLE);
        }

        updatedAfterKeyPressed = true;
    }

    /**
     * Ends the game by stopping the animation timer and switching to the Game Over scene.
     */
    public void gameOver() {
        animationTimer.stop();
        sceneManager.changeScene(SceneEnum.GAME_OVER);
    }

    /**
     * Ends the game by stopping the animation timer and switching to the Game Win scene.
     */
    private void win() {
        animationTimer.stop();
        sceneManager.changeScene(SceneEnum.GAME_WIN);
    }
}
