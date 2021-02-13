package edu.colorado.fourdimensionalonedgames;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship testShip;


    @BeforeEach
    void setUp() {
        testShip = new Ship();
    }
    @Test
    void testAdd(){
        int result = testShip.add(4,3);
        assertEquals(7,result);
    }

    @Test
    void testDamageShip(){

    }
}