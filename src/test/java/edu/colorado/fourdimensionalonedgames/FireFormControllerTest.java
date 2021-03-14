package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FireFormControllerTest {

    PlayerFireInput input;
    PlayerFireInput badInput;
    FireFormController controller;

    @BeforeEach
    void setUp() {
        controller = new FireFormController();
    }

    @Test
    void testValidateForm(){
        input = new PlayerFireInput("Space Laser","2","2");
        badInput = new PlayerFireInput("Space Laser", "Destroyer", "");
        assertTrue(controller.validateForm(input));
        assertFalse(controller.validateForm(badInput));
    }
}
