package edu.colorado.fourdimensionalonedgames;

import static org.junit.jupiter.api.Assertions.*;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AttackShipsTest {

    Board testBoard;
    Ship testShip1, testShip2, testShip3;
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    Render renderer;
    GridPane gpane;

    @BeforeEach
    void setUp(){

        gpane = new GridPane();
        renderer = new Render();
        testBoard = new Board(columns, rows, renderer);
        testBoard.initializeBoard(gpane);

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
    void attackMiss(){
        Point2D attackCoords = new Point2D(1,1);
        assertNull(testBoard.attack(attackCoords));

        attackCoords = new Point2D(2,2);
        assertNotNull(testBoard.attack((attackCoords)));
    }

    @Test
    void attackHit(){
        Point2D attackCoords = new Point2D(2,2);
        Ship hitShip = testBoard.attack(attackCoords);

        // assertions after one hit
        assertSame(testShip1, hitShip);
        assertEquals(1, hitShip.damage());
        assertFalse(hitShip.destroyed());

        // sink ship
        attackCoords = new Point2D(2,3);
        hitShip = testBoard.attack(attackCoords);
        attackCoords = new Point2D(2,4);
        hitShip = testBoard.attack(attackCoords);

        // assertions after ship is sunk
        assertSame(testShip1, hitShip);
        assertEquals(3, hitShip.damage());
        assertTrue(hitShip.destroyed());
    }

    @Test
    void offBoardAttack(){
        // deal with attacks that fall off of the game board
        assertThrows(InvalidAttackException.class, () -> {
            Point2D attackCoords = new Point2D(0,0);
            testBoard.attack(attackCoords);
        });
    }

    @Test
    void duplicateAttack(){
        // attack a tile that has already been attacked
        Point2D attackCoords = new Point2D(1, 1);
        testBoard.attack(attackCoords);

        assertThrows(InvalidAttackException.class, () -> {
            testBoard.attack(attackCoords);
        });
    }

    @Test
    void gameOver(){
        assertFalse(testBoard.gameOver());

        // sink all ships on board
        Point2D attackCoords = new Point2D(2,2);
        testBoard.attack(attackCoords);
        attackCoords = new Point2D(2,3);
        testBoard.attack(attackCoords);
        attackCoords = new Point2D(2,4);
        testBoard.attack(attackCoords);
        attackCoords = new Point2D(4,4);
        testBoard.attack(attackCoords);
        attackCoords = new Point2D(5,4);
        testBoard.attack(attackCoords);

        assertTrue(testBoard.gameOver());
    }

}
