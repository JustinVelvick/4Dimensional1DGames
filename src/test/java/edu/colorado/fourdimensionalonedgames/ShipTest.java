package edu.colorado.fourdimensionalonedgames;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship testShip;


    @BeforeEach
    void setUp() {
        testShip = new Destroyer();
    }

    @Test
    void testDamageShip(){
        fail("Not yet implemented");
    }

    @Test
    void testShipDeath(){
        fail("Not yet implemented");
    }
}