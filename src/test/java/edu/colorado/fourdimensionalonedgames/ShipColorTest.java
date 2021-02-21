package edu.colorado.fourdimensionalonedgames;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ShipColorTest {

    Ship testMinesweeper, testDestroyer, testBattleship;

    @BeforeEach
    void setUp() {
        testBattleship = new Battleship();
        testDestroyer = new Destroyer();
        testMinesweeper = new Minesweeper();
    }

    @Test
    void testShipColors() {
        assertEquals(Color.BLUE, testDestroyer.getColor());
        assertEquals(Color.RED, testBattleship.getColor());
        assertEquals(Color.GREEN, testMinesweeper.getColor());
    }
}
