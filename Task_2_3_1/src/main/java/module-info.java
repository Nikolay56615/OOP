module ru.nsu.lebedev.snake.task_2_3_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens ru.nsu.lebedev.snake.task_2_3_1 to javafx.fxml;
    exports ru.nsu.lebedev.snake.task_2_3_1;
    exports ru.nsu.lebedev.snake.task_2_3_1.scenes;
    opens ru.nsu.lebedev.snake.task_2_3_1.scenes to javafx.fxml;
    exports ru.nsu.lebedev.snake.task_2_3_1.game;
    opens ru.nsu.lebedev.snake.task_2_3_1.game to javafx.fxml;

    exports ru.nsu.lebedev.snake.task_2_3_1.controllers to com.fasterxml.jackson.databind;
    opens ru.nsu.lebedev.snake.task_2_3_1.controllers to javafx.fxml;

    exports ru.nsu.lebedev.snake.task_2_3_1.json to com.fasterxml.jackson.databind;
}