package ru.nsu.lebedev.snake.task_2_3_1.scenes;

import java.util.ArrayList;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Utility class for managing the game view. It builds a grid of cells (Rectangles) inside a
 * GridPane and allows changing cell colors.
 */
public class GameView {

    /**
     * Enum representing the available cell colors.
     */
    public enum CellColor {
        FIELD("#A8E6CF"),
        SNAKE("#4A90E2"),
        SNAKE_HEAD("#003366"),
        APPLE("#FF4F4F");

        private final String hex;

        CellColor(String hex) {
            this.hex = hex;
        }

        /**
         * Returns the hex string of the color.
         *
         * @return the hex value.
         */
        public String getHex() {
            return hex;
        }
    }

    private final GridPane grid;
    private ArrayList<ArrayList<StackPane>> cellPanes;
    private final int numRows;
    private final int numCols;

    /**
     * Constructs the game view with the specified grid and dimensions.
     *
     * @param grid    the GridPane container for the cells.
     * @param numRows the number of rows.
     * @param numCols the number of columns.
     */
    public GameView(GridPane grid, int numRows, int numCols) {
        this.grid = grid;
        this.numRows = numRows;
        this.numCols = numCols;
        initializeField();
    }

    /**
     * Creates a new cell (StackPane) with default field color.
     *
     * @return a new StackPane representing a cell.
     */
    private static StackPane createCell() {
        Rectangle rectangle = new Rectangle(30, 30, Paint.valueOf(CellColor.FIELD.getHex()));
        rectangle.getStyleClass().add("cell");

        StackPane cellPane = new StackPane();
        cellPane.getChildren().add(rectangle);

        return cellPane;
    }

    /**
     * Initializes the game field by constructing a grid of StackPanes and adding them to the
     * GridPane.
     */
    private void initializeField() {
        cellPanes = new ArrayList<>();

        for (int row = 0; row < numRows; row++) {
            ArrayList<StackPane> rowCells = new ArrayList<>();
            for (int col = 0; col < numCols; col++) {
                StackPane cell = createCell();
                rowCells.add(cell);
                grid.add(cell, col, row);
            }
            cellPanes.add(rowCells);
        }
    }

    /**
     * Sets the color of a specific cell.
     *
     * @param x     the column index.
     * @param y     the row index.
     * @param color the new color from the CellColor enum.
     */
    public void setCellColor(int x, int y, CellColor color) {
        StackPane cellPane = cellPanes.get(y).get(x);

        // Удаляем все дочерние элементы, кроме фона (первого элемента)
        if (cellPane.getChildren().size() > 1) {
            cellPane.getChildren().remove(1, cellPane.getChildren().size());
        }

        Rectangle background = (Rectangle) cellPane.getChildren().get(0);

        if (color == CellColor.SNAKE) {
            // Рисуем тело змеи
            Rectangle snakeBody = new Rectangle(30, 30, Paint.valueOf(CellColor.SNAKE.getHex()));
            snakeBody.setArcWidth(15);
            snakeBody.setArcHeight(15);
            cellPane.getChildren().add(snakeBody);
        } else if (color == CellColor.SNAKE_HEAD) {
            // Рисуем голову змеи
            Rectangle snakeHead = new Rectangle(30, 30,
                Paint.valueOf(CellColor.SNAKE_HEAD.getHex()));
            snakeHead.setArcWidth(20);
            snakeHead.setArcHeight(20);
            cellPane.getChildren().add(snakeHead);

            // Добавляем глаза
            Circle leftEye = new Circle(4, Color.WHITE);
            leftEye.setTranslateX(-6);
            leftEye.setTranslateY(-6);

            Circle rightEye = new Circle(4, Color.WHITE);
            rightEye.setTranslateX(6);
            rightEye.setTranslateY(-6);

            cellPane.getChildren().addAll(leftEye, rightEye);
        } else if (color == CellColor.APPLE) {
            // Рисуем яблоко
            Circle apple = new Circle(10, Color.valueOf(CellColor.APPLE.getHex()));
            apple.setStroke(Color.BLACK);
            apple.setStrokeWidth(2);
            cellPane.getChildren().add(apple);
        } else {
            // Устанавливаем цвет фона для остальных клеток
            background.setFill(Paint.valueOf(color.getHex()));
        }
    }
}
