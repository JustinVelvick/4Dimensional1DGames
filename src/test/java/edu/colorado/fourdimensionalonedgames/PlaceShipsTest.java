package edu.colorado.fourdimensionalonedgames;

import static org.junit.jupiter.api.Assertions.*;

import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

class PlaceShipsTest {

    Ship testShip, testShip2, testShip3;
    Board testBoard;
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

        testShip = new Destroyer();
        testShip2 = new Minesweeper();
        testShip3 = new Battleship();
    }

    @Test
    void placeValidShip(){
        Orientation direction = Orientation.up;
        double x = 3;
        double y = 4;
        Point2D origin = new Point2D(x,y);

        assertTrue(testBoard.placeShip(gpane, direction, origin, testShip));
    }

    @Test
    void placeInvalidShip(){
        // goes off top of board
        Orientation direction = Orientation.up;
        Point2D origin = new Point2D(1,1);
        assertFalse(testBoard.placeShip(gpane, direction, origin, testShip));

        // goes off left side of board
        direction = Orientation.left;
        assertFalse(testBoard.placeShip(gpane, direction, origin, testShip));

        // goes off bottom of board
        direction = Orientation.down;
        origin = new Point2D(10, 10);
        assertFalse(testBoard.placeShip(gpane, direction, origin, testShip));

        // goes off right side of board
        direction = Orientation.right;
        assertFalse(testBoard.placeShip(gpane, direction, origin, testShip));
    }

    @Test
    void placeOverlappingShips(){
        // place first ship
        Orientation ship1Dir = Orientation.right;
        Point2D ship1Origin = new Point2D(1,1);
        assertTrue(testBoard.placeShip(gpane, ship1Dir, ship1Origin, testShip));

        // place second, overlapping ship
        Orientation ship2Dir = Orientation.up;
        Point2D ship2Origin = new Point2D(2, 2);
        assertFalse(testBoard.placeShip(gpane, ship2Dir, ship2Origin, testShip2));
    }

}