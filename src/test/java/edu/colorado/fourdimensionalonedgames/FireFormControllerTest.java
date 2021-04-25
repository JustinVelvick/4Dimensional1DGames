package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FireFormControllerTest {

    PlayerFireInput goodInput1;
    PlayerFireInput goodInput2;
    PlayerFireInput badInput1;
    PlayerFireInput badInput2;
    FireFormController controller;

    @BeforeEach
    void setUp() {
        controller = new FireFormController();
    }

    @Test
    void testValidateForm() {
        goodInput1 = new PlayerFireInput("Space Laser", "B", "2");
        goodInput2 = new PlayerFireInput("Space Laser", "b", "7");
        badInput1 = new PlayerFireInput("Space Laser", "Destroyer", "");
        badInput2 = new PlayerFireInput("Space Laser", "C", "78");
        assertTrue(controller.validateForm(goodInput1));
        assertTrue(controller.validateForm(goodInput2));
        assertFalse(controller.validateForm(badInput1));
        assertFalse(controller.validateForm(badInput2));
    }
}
