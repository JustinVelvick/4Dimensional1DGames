package edu.colorado.fourdimensionalonedgames;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ColorTest {

    Ship testMinesweeper, testDestroyer, testBattleship;
    Board testBoard;
    Render renderer;
    GridPane gpane;
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;

    @BeforeEach
    void setUp() {
        gpane = new GridPane();
        renderer = new Render();
        testBoard = new Board(columns, rows, renderer);
        testBoard.initializeBoard(gpane);

        testBattleship = new Battleship();
        testDestroyer = new Destroyer();
        testMinesweeper = new Minesweeper();

        Orientation direction = Orientation.down;
        Point2D origin = new Point2D(1, 1);
        testBoard.placeShip(gpane, direction, origin, testMinesweeper);

        origin = new Point2D(2, 1);
        testBoard.placeShip(gpane, direction, origin, testDestroyer);

        origin = new Point2D(3, 1);
        testBoard.placeShip(gpane, direction, origin, testBattleship);
    }

    @Test
    void testShipColors() {
        assertEquals(Color.BLUE, testDestroyer.getColor());
        assertEquals(Color.INDIGO, testBattleship.getColor());
        assertEquals(Color.GREEN, testMinesweeper.getColor());
    }

    @Test
    void testDestroyedShipColors() {
        Point2D origin;
        for (int x = 1; x <= 3; x++) {
            for (int y = 1; y <= x + 1; y++) {
                origin = new Point2D(x, y);
                testBoard.attack(origin);
            }
        }
        assertEquals(Color.RED, testDestroyer.getColor());
        assertEquals(Color.RED, testBattleship.getColor());
        assertEquals(Color.RED, testMinesweeper.getColor());
    }

    @Test
    void testDamagedTileColors() {
        Point2D origin;
        for (int x = 1; x <= 3; x++) {
            origin = new Point2D(x, 1);
            testBoard.attack(origin);
        }
        assertEquals(Color.HOTPINK, testBoard.tiles[1][1].getColor());
        assertEquals(Color.HOTPINK, testBoard.tiles[2][1].getColor());
        assertEquals(Color.HOTPINK, testBoard.tiles[3][1].getColor());
        assertEquals(Color.GREEN, testBoard.tiles[1][2].getColor());
        assertEquals(Color.BLUE, testBoard.tiles[2][2].getColor());
        assertEquals(Color.INDIGO, testBoard.tiles[3][2].getColor());
    }

    @Test
    void testNonShipTileColors() {
        assertEquals(Color.CORNFLOWERBLUE, testBoard.tiles[1][3].getColor());
        assertEquals(Color.TRANSPARENT, testBoard.tiles[0][0].getColor());
    }
}