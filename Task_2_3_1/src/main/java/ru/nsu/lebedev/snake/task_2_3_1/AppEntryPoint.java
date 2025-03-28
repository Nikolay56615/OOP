package ru.nsu.lebedev.snake.task_2_3_1;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.SceneEnum;
import ru.nsu.lebedev.snake.task_2_3_1.scenes.ScenesManager;

/**
 * Entry point for all game.
 */
public class AppEntryPoint extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Snake");
        stage.setResizable(false);
        ScenesManager sceneManager = new ScenesManager(stage);
        sceneManager.changeScene(SceneEnum.MENU);
    }

    /**
     * Main function that launch the game.
     */
    public static void main(String[] args) {
        launch();
    }
}