package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShipFormControllerTest {

    PlayerShipInput input;
    PlayerShipInput badInput;
    ShipChoiceFormController controller;

    @BeforeEach
    void setUp() {
        controller = new ShipChoiceFormController();
    }

    @Test
    void testValidateForm(){
        input = new PlayerShipInput("Down","Destroyer","B", "3");
        badInput = new PlayerShipInput("", "Destroyer", "Applesauce", "032485*");
        assertTrue(controller.validateForm(input));
        assertFalse(controller.validateForm(badInput));
    }
}
