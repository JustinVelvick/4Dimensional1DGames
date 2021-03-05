package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SingleShot;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Sonar;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DifferentWeaponsTest {

    Weapon singleShot, sonar;

    @BeforeEach
    void setUp() {
        singleShot = new SingleShot();
        sonar = new Sonar();
    }

    @Test
    void testNames() {
        assertEquals(singleShot.getType(), "Single Shot");
        assertEquals(sonar.getType(), "Sonar");
    }
}
