package ru.nsu.lebedev.snake.scenes;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Utility class for managing the game view.
 * It builds a grid of cells (Rectangles) inside a GridPane and allows changing cell colors.
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
    private final DoubleProperty cellSizeProperty = new SimpleDoubleProperty();

    /**
     * Constructs the game view with the specified grid and dimensions.
     *
     * @param grid    the GridPane container for the cells.
     * @param numRows the number of rows.
     * @param numCols the number of columns.
     * @param width   the width of the game area.
     * @param height  the height of the game area.
     */
    public GameView(GridPane grid, int numRows, int numCols, double width, double height) {
        this.grid = grid;
        this.numRows = numRows;
        this.numCols = numCols;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            grid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            grid.getRowConstraints().add(rowConst);
        }
        initializeField();
    }

    /**
     * Creates a new cell (StackPane) with default field color.
     *
     * @return a new StackPane representing a cell.
     */
    private StackPane createCell() {
        StackPane cellPane = new StackPane();
        Rectangle rectangle = new Rectangle();
        rectangle.widthProperty().bind(cellPane.widthProperty());
        rectangle.heightProperty().bind(cellPane.heightProperty());
        rectangle.setFill(Paint.valueOf(CellColor.FIELD.getHex()));
        rectangle.getStyleClass().add("cell");
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
        if (cellPane.getChildren().size() > 1) {
            cellPane.getChildren().remove(1, cellPane.getChildren().size());
        }

        Rectangle background = (Rectangle) cellPane.getChildren().get(0);

        if (color == CellColor.SNAKE) {
            Rectangle snakeBody = new Rectangle();
            snakeBody.widthProperty().bind(cellSizeProperty);
            snakeBody.heightProperty().bind(cellSizeProperty);
            snakeBody.setFill(Paint.valueOf(CellColor.SNAKE.getHex()));
            snakeBody.arcWidthProperty().bind(cellSizeProperty.multiply(0.5));
            snakeBody.arcHeightProperty().bind(cellSizeProperty.multiply(0.5));
            cellPane.getChildren().add(snakeBody);
        } else if (color == CellColor.SNAKE_HEAD) {
            Rectangle snakeHead = new Rectangle();
            snakeHead.widthProperty().bind(cellSizeProperty);
            snakeHead.heightProperty().bind(cellSizeProperty);
            snakeHead.setFill(Paint.valueOf(CellColor.SNAKE_HEAD.getHex()));
            snakeHead.arcWidthProperty().bind(cellSizeProperty.multiply(0.7));
            snakeHead.arcHeightProperty().bind(cellSizeProperty.multiply(0.7));
            cellPane.getChildren().add(snakeHead);

            Circle leftEye = new Circle();
            leftEye.radiusProperty().bind(cellSizeProperty.multiply(0.13));
            leftEye.setFill(Color.WHITE);
            leftEye.translateXProperty().bind(cellSizeProperty.multiply(-0.2));
            leftEye.translateYProperty().bind(cellSizeProperty.multiply(-0.2));

            Circle rightEye = new Circle();
            rightEye.radiusProperty().bind(cellSizeProperty.multiply(0.13));
            rightEye.setFill(Color.WHITE);
            rightEye.translateXProperty().bind(cellSizeProperty.multiply(0.2));
            rightEye.translateYProperty().bind(cellSizeProperty.multiply(-0.2));

            cellPane.getChildren().addAll(leftEye, rightEye);
        } else if (color == CellColor.APPLE) {
            Circle apple = new Circle();
            apple.radiusProperty().bind(cellSizeProperty.multiply(0.3));
            apple.setFill(Color.valueOf(CellColor.APPLE.getHex()));
            apple.setStroke(Color.BLACK);
            apple.strokeWidthProperty().bind(cellSizeProperty.multiply(0.05));
            cellPane.getChildren().add(apple);
        } else {
            background.setFill(Paint.valueOf(color.getHex()));
        }
    }
}
