package ru.nsu.lebedev.snake.task_2_3_1.scenes;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.lebedev.snake.task_2_3_1.AppEntryPoint;

/**
 * Scene manager class.
 * <p>
 * Used for easily switching between scenes in the application. Handles loading and switching
 * scenes, and binds controllers to the scenes.
 */
public class ScenesManager {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 700;
    private final Stage primaryStage;

    /**
     * Constructor.
     *
     * @param primaryStage the stage on which the scenes will be displayed.
     */
    public ScenesManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Closes the primary stage.
     */
    public void close() {
        primaryStage.close();
    }

    /**
     * Loads a scene from the specified FXML file and binds the controller.
     * <p>
     * Sets up the scene with the given FXML, assigns the appropriate controller, and binds it with
     * the scene manager.
     *
     * @param fxmlPath the path to the FXML file.
     * @throws IOException if there is an issue with loading the FXML file.
     */
    private void loadScene(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            AppEntryPoint.class.getResource(fxmlPath)
        );

        Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        ScenesControllerContract controller = loader.getController();
        controller.setSceneManager(this);
    }

    /**
     * Changes the current scene to the specified one.
     * <p>
     * Switches to the specified scene by loading the corresponding FXML file.
     *
     * @param sceneEnum the enum value representing the scene to switch to.
     */
    public void changeScene(SceneEnum sceneEnum) {
        try {
            switch (sceneEnum) {
                case MENU:
                    loadScene("game-menu-scene-view.fxml");
                    break;
                case SETTINGS:
                    loadScene("game-settings-scene-view.fxml");
                    break;
                case GAME:
                    loadScene("game-scene-view.fxml");
                    break;
                case GAME_OVER:
                    loadScene("game-over-scene-view.fxml");
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }

    /**
     * Returns the current scene displayed in the primary stage.
     *
     * @return the current scene.
     */
    public Scene getCurrentScene() {
        return primaryStage.getScene();
    }
}
