package ru.nsu.lebedev.snake.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.lebedev.snake.models.ModelEnum;
import ru.nsu.lebedev.snake.models.ModelGame;

/**
 * Tests for GameModel model fragment. Contains almost whole methods of internal objects
 */
public class GameTest {

    @Test
    void testValidSnakeDirectionChanges() {
        ModelGame gameModel = ModelEnum.GAME.get().restartModel();
        gameModel.getSnake().setDirection(GameVector.RIGHT);
        Assertions.assertEquals(GameVector.RIGHT, gameModel.getSnake().getDirection());
        gameModel.getSnake().setDirection(GameVector.UP);
        Assertions.assertEquals(GameVector.UP, gameModel.getSnake().getDirection());
        gameModel.getSnake().setDirection(GameVector.LEFT);
        Assertions.assertEquals(GameVector.LEFT, gameModel.getSnake().getDirection());
        gameModel.getSnake().setDirection(GameVector.DOWN);
        Assertions.assertEquals(GameVector.DOWN, gameModel.getSnake().getDirection());
    }

    @Test
    void testInvalidSnakeDirectionChanges() {
        ModelGame gameModel = ModelEnum.GAME.get().restartModel();

        gameModel.getSnake().setDirection(GameVector.RIGHT);
        gameModel.getSnake().setDirection(GameVector.LEFT);
        Assertions.assertEquals(GameVector.RIGHT, gameModel.getSnake().getDirection());

        gameModel.getSnake().setDirection(GameVector.UP);
        gameModel.getSnake().setDirection(GameVector.DOWN);
        Assertions.assertEquals(GameVector.UP, gameModel.getSnake().getDirection());

        gameModel.getSnake().setDirection(GameVector.LEFT);
        gameModel.getSnake().setDirection(GameVector.RIGHT);
        Assertions.assertEquals(GameVector.LEFT, gameModel.getSnake().getDirection());

        gameModel.getSnake().setDirection(GameVector.DOWN);
        gameModel.getSnake().setDirection(GameVector.UP);
        Assertions.assertEquals(GameVector.DOWN, gameModel.getSnake().getDirection());
    }

    @Test
    void testSnakeGrowth() {
        ModelGame game = ModelEnum.GAME.get().restartModel();
        GamePoint applePosition = game.getSnake().getHead().copy();
        applePosition.move(game.getSnake().getDirection());
        game.getApples().add(applePosition);

        Assertions.assertEquals(1, game.getScore());
        Assertions.assertEquals(game.getSnake().getHead(), game.getSnake().getTail());

        game.update();
        Assertions.assertEquals(2, game.getScore());
        Assertions.assertEquals(game.getSnake().getBody().get(0), game.getSnake().getTail());

        applePosition = game.getSnake().getHead().copy();
        applePosition.move(game.getSnake().getDirection());
        game.getApples().add(applePosition);

        game.update();
        Assertions.assertEquals(3, game.getScore());
        Assertions.assertNotEquals(game.getSnake().getBody().get(0), game.getSnake().getHead());
        Assertions.assertNotEquals(game.getSnake().getBody().get(0), game.getSnake().getTail());
    }

    @Test
    void testGameOverCondition() {
        ModelGame gameModel = ModelEnum.GAME.get().restartModel();
        gameModel.getSnake().setDirection(GameVector.RIGHT);
        for (int i = 0; i < 5; ++i) {
            gameModel.getSnake().move();
            gameModel.getSnake().grow();
        }
        gameModel.getSnake().setDirection(GameVector.UP);
        gameModel.getSnake().move();
        gameModel.getSnake().setDirection(GameVector.LEFT);
        gameModel.getSnake().move();
        gameModel.getSnake().setDirection(GameVector.DOWN);
        gameModel.getSnake().move();
        Assertions.assertTrue(gameModel.isGameOver());
    }
}
