package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Reveal;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.LargeWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DifferentWeaponsTest {

    Weapon singleShot, sonar;

    @BeforeEach
    void setUp() {
        singleShot = new SmallWeapon(new Attack(), "Single Shot");
        sonar = new LargeWeapon(new Reveal(), "Sonar");
    }

    @Test
    void testNames() {
        assertEquals(singleShot.getType(), "Single Shot");
        assertEquals(sonar.getType(), "Sonar");
    }
}
