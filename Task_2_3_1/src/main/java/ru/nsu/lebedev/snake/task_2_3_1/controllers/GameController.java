package ru.nsu.lebedev.snake.task_2_3_1.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import ru.nsu.lebedev.snake.task_2_3_1.game.GamePoint;
import ru.nsu.lebedev.snake.task_2_3_1.game.GameVector;
import ru.nsu.lebedev.snake.task_2_3_1.models.ModelEnum;
import ru.nsu.lebedev.snake.task_2_3_1.models.ModelGame;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.GameView;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.SceneEnum;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.ScenesControllerContract;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.ScenesManager;

/**
 * Controller for the Game scene.
 * <p>
 * This class manages the game loop, handles key input events for controlling the snake,
 * and updates the game view accordingly.
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

        sceneManager.getCurrentScene().setOnKeyPressed(this::keyHandler);
        sceneManager.getCurrentScene().widthProperty();
        animationTimer.start();
    }

    /**
     * Handles key events for controlling the snake.
     * <p>
     * Arrow keys change the snake's direction; the ESC key ends the game.
     *
     * @param event the key event.
     */
    private void keyHandler(KeyEvent event) {
        if (!updatedAfterKeyPressed) {
            return;
        }
        switch (event.getCode()) {
            case UP:
                gameModel.getSnake().setDirection(GameVector.UP);
                break;
            case RIGHT:
                gameModel.getSnake().setDirection(GameVector.RIGHT);
                break;
            case DOWN:
                gameModel.getSnake().setDirection(GameVector.DOWN);
                break;
            case LEFT:
                gameModel.getSnake().setDirection(GameVector.LEFT);
                break;
            case ESCAPE:
                gameOver();
                break;
            default:
                break;
        }
        updatedAfterKeyPressed = false;
    }

    /**
     * Updates the game state: repaints cells, updates the model,
     * and checks for game over condition.
     */
    private void updateSnakeCells() {
        GamePoint currentHead = gameModel.getSnake().getHead();
        gameView.setCellColor(currentHead.getX(), currentHead.getY(), GameView.CellColor.SNAKE);

        GamePoint tail = gameModel.getSnake().getTail();
        gameView.setCellColor(tail.getX(), tail.getY(), GameView.CellColor.FIELD);

        gameModel.update();
        if (gameModel.isGameOver()) {
            gameOver();
            return;
        }
        scores.setText(Integer.toString(gameModel.getScore()));

        GamePoint newHead = gameModel.getSnake().getHead();
        gameView.setCellColor(newHead.getX(), newHead.getY(), GameView.CellColor.SNAKE_HEAD);

        for (GamePoint apple : gameModel.getApples().getApples()) {
            gameView.setCellColor(apple.getX(), apple.getY(), GameView.CellColor.APPLE);
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
}
