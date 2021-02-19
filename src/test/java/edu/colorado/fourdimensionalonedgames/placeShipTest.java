package edu.colorado.fourdimensionalonedgames;

import static org.junit.jupiter.api.Assertions.*;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

class shipPlacementTest {

    Ship testShip;
    Board testBoard;
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    Render renderer;

    @BeforeEach
    void setUp(){
        renderer = new Render();
        testBoard = new Board(columns, rows, renderer);
        testShip = new Destroyer();
    }

    @Test
    void placeValidShip(){
        Orientation direction = Orientation.up;
        double x = 3;
        double y = 4;
        Point2D origin = new Point2D(x,y);

        assertTrue(testBoard.placeShip(direction, origin, testShip));
    }



}