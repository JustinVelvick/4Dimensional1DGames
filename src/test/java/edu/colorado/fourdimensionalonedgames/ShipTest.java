package edu.colorado.fourdimensionalonedgames;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship testShip;
    ShipTile tile1, tile2, tile3;

    @BeforeEach
    void setUp() {
        testShip = new Destroyer();
        tile1 = new ShipTile(testShip, 1,1);
        tile2 = new ShipTile(testShip, 1,2);
        tile3 = new ShipTile(testShip, 1,3);
        testShip.addTile(tile1);
        testShip.addTile(tile2);
        testShip.addTile(tile3);
    }

    @Test
    void testDamageShip() {
        assertEquals(0, testShip.damage());
        tile1.shot = true;
        assertEquals(1, testShip.damage());
        tile2.shot = true;
        tile3.shot = true;
        assertEquals(3, testShip.damage());
    }

    @Test
    void testShipDeath() {
        // test undamaged, partially damaged, and destroyed ship
        assertFalse(testShip.destroyed());
        tile1.shot = true;
        assertFalse(testShip.destroyed());
        tile2.shot = true;
        tile3.shot = true;
        assertTrue(testShip.destroyed());
    }
}