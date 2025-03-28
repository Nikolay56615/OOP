package ru.nsu.lebedev.snake.task_2_3_1.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.nsu.lebedev.snake.task_2_3_1.models.ModelEnum;
import ru.nsu.lebedev.snake.task_2_3_1.models.ModelGame;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.SceneEnum;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.ScenesControllerContract;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.ScenesManager;

/**
 * Controller for the Game Over scene.
 */
public class GameOverController implements ScenesControllerContract {

    private ScenesManager sceneManager;

    @FXML
    private Label score;

    /**
     * Sets the ScenesManager and updates the score label with the current game score.
     *
     * @param sceneManager the ScenesManager instance
     */
    @Override
    public void setSceneManager(ScenesManager sceneManager) {
        this.sceneManager = sceneManager;
        // Retrieve the game model and update the score label
        ModelGame gameModel = (ModelGame) ModelEnum.GAME.get();
        score.setText(Integer.toString(gameModel.getScore()));
    }

    /**
     * Handler for the back button. Switches the scene back to the MENU scene.
     */
    @FXML
    protected void back() {
        sceneManager.changeScene(SceneEnum.MENU);
    }
}

