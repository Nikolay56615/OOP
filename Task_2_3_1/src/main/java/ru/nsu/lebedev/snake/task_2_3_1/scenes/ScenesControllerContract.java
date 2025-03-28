package ru.nsu.lebedev.snake.task_2_3_1.scenes;

/**
 * Interface for Controllers that require access to the ScenesManager.
 * <p>
 * This interface is implemented by Controllers that need to interact with the ScenesManager,
 * typically for handling scene transitions.
 */
public interface ScenesControllerContract {

    /**
     * Links the Controller with the ScenesManager.
     * <p>
     * This method is called by the ScenesManager to pass its instance to the Controllers, enabling
     * it to manage scene transitions or perform other necessary actions.
     *
     * @param manager the SceneManager instance
     */
    void setSceneManager(ScenesManager manager);
}
