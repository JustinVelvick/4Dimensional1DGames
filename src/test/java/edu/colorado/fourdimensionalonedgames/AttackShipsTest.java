package edu.colorado.fourdimensionalonedgames;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AttackShipsTest {

    Board testBoard;
    Board testPlayerBoard;
    Player testPlayer;
    Ship testShip1, testShip2, testShip3;
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    Render renderer;
    GridPane gpane;
    AttackResult simpleMiss;

    @BeforeEach
    void setUp() {

        gpane = new GridPane();
        renderer = new Render();
        testBoard = new Board(columns, rows, renderer);
        testBoard.initializeBoard(gpane);
        testPlayerBoard = new Board(columns, rows, renderer);
        testPlayerBoard.initializeBoard(gpane);
        testPlayer = new Player(testPlayerBoard);

        simpleMiss = new AttackResult(AttackResultType.MISS, null);

        testShip1 = new Destroyer();
        testShip2 = new Minesweeper();
        testShip3 = new Battleship();

        Orientation direction = Orientation.down;
        Point2D origin = new Point2D(2,2);
        testBoard.placeShip(gpane, direction, origin, testShip1);

        direction = Orientation.right;
        origin = new Point2D(4,4);
        testBoard.placeShip(gpane, direction, origin, testShip2);
    }

    @Test
    void attackMiss() {
        Point2D attackCoords = new Point2D(1,1);
        assertEquals(testPlayer.attack(testBoard, attackCoords), simpleMiss);

        attackCoords = new Point2D(2,2);
        assertNotEquals(testPlayer.attack(testBoard, attackCoords), simpleMiss);
    }

    @Test
    void attackHit() {
        Point2D attackCoords = new Point2D(2,2);
        AttackResult result = testPlayer.attack(testBoard, attackCoords);

        // assertions after one hit
        assertSame(new AttackResult(AttackResultType.HIT, testShip1), result);
        assertEquals(1, result.ship.damage());

        // sink ship
        attackCoords = new Point2D(2,3);
        result = testPlayer.attack(testBoard, attackCoords);
        attackCoords = new Point2D(2,4);
        result = testPlayer.attack(testBoard, attackCoords);

        // assertions after ship is sunk
        assertSame(new AttackResult(AttackResultType.SUNK, testShip1), result);
        assertEquals(3, result.ship.damage());
    }

    @Test
    void offBoardAttack() {
        // deal with attacks that fall off of the game board
        assertThrows(InvalidAttackException.class, () -> {
            Point2D attackCoords = new Point2D(0,0);
            testPlayer.attack(testBoard, attackCoords);
        });

        assertThrows(InvalidAttackException.class, () -> {
            Point2D attackCoords = new Point2D(11,11);
            testPlayer.attack(testBoard, attackCoords);
        });
    }

    @Test
    void edgeBoardAttack() {
        // deal with attacks that barely don't fall off of the game board
        assertDoesNotThrow(() -> {
            Point2D attackCoords = new Point2D(1,1);
            testBoard.attack(attackCoords);
        });

        assertDoesNotThrow(() -> {
            Point2D attackCoords = new Point2D(10,10);
            testBoard.attack(attackCoords);
        });
    }

    @Test
    void duplicateAttack() {
        // attack a tile that has already been attacked
        Point2D attackCoords = new Point2D(1, 1);
        testPlayer.attack(testBoard, attackCoords);

        assertThrows(InvalidAttackException.class, () -> {
            testPlayer.attack(testBoard, attackCoords);
        });
    }

    @Test
    void gameOver() {
        assertFalse(testBoard.gameOver());

        // sink all ships on board
        Point2D attackCoords = new Point2D(2,2);
        testPlayer.attack(testBoard, attackCoords);
        attackCoords = new Point2D(2,3);
        testPlayer.attack(testBoard, attackCoords);
        attackCoords = new Point2D(2,4);
        testPlayer.attack(testBoard, attackCoords);
        attackCoords = new Point2D(4,4);
        testPlayer.attack(testBoard, attackCoords);
        attackCoords = new Point2D(5,4);
        AttackResult result = testPlayer.attack(testBoard, attackCoords);

        assertEquals(result.type, AttackResultType.SURRENDER);
    }

}
