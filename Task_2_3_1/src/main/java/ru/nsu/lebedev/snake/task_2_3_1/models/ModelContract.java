package ru.nsu.lebedev.snake.task_2_3_1.models;

/**
 * Interface for classes that will be integrated into the model enum.
 * <p>
 * This interface defines a contract for model fragments, ensuring they implement a method
 * to restart and return their instance for a new game cycle.
 */
public interface ModelContract {

    /**
     * Restarts the model instance.
     * <p>
     * This method should perform any necessary reset operations and return the model fragment
     * instance, allowing it to be used for a new working cycle.
     *
     * @param <T> The type of the model fragment.
     * @return The instance of the implementing model fragment.
     */
    <T extends ModelContract> T restartModel();
}
