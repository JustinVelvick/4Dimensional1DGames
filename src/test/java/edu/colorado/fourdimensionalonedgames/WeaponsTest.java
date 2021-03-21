package edu.colorado.fourdimensionalonedgames;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_MULTIPLYPeer;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Reveal;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.PenetratingSmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.LargeWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeaponsTest {
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    static int depth = 2;
    static int numberOfPlayers = 2;

    GridPane player1gpane;
    GridPane player1enemygpane;
    GridPane player2gpane;
    GridPane player2enemygpane;

    Render renderer;

    Game game;

    Player player1;
    Player player2;

    PlayerShipInput shipInput1;
    PlayerShipInput shipInput2;
    PlayerShipInput shipInput3;

    PlayerFireInput fireInput1;
    PlayerFireInput fireInput2;
    PlayerFireInput fireInput3;

    AttackResult simpleMiss;
    Ship player2Minesweeper;
    Ship player2Destroyer;
    Ship player2Battleship;
    Ship player2Submarine;
    Weapon singleShot, sonar, spaceLaser;

    @BeforeEach
    void setUp(){
        renderer = new Render();
        game = new Game(renderer, numberOfPlayers, tileSize, columns, rows, depth);
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);

        player2Minesweeper = player2.getShipsToPlace().get(0);
        player2Destroyer = player2.getShipsToPlace().get(1);
        player2Submarine = player2.getShipsToPlace().get(3);

        singleShot = new SmallWeapon(new Attack(), "Single Shot");
        sonar = new LargeWeapon(new Reveal(), "Sonar");
        spaceLaser = new PenetratingSmallWeapon(new Attack(), "Space Laser");

        shipInput1 = new PlayerShipInput("Down", "Minesweeper", "1", "1");
        player2.placeShip(shipInput1);
    }
    @Test
    void testNames() {
        assertEquals(singleShot.getType(), "Single Shot");
        assertEquals(sonar.getType(), "Sonar");
        assertEquals(spaceLaser.getType(), "Space Laser");
    }

    @Test
    void testWeaponCount(){
        //only single shot should be available at the start of the game
        assertEquals(1, player1.getWeapons().size());

        //one shots minesweeper, unlocks space laser and 2 sonar pulses
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        player1.attack(player2.getBoard(), fireInput1);
        game.checkUpgrades();

        //should have Space Laser (one object infinite uses) and 2 Sonar Pulse objects
        assertEquals(3, player1.getWeapons().size());

        //using space laser should do nothing to your total weapon count since it has infinite uses
        fireInput1 = new PlayerFireInput("Space Laser", "5", "8");
        player1.attack(player2.getBoard(), fireInput1);

        //should have Space Laser (one object infinite uses) and 2 Sonar Pulse objects
        assertEquals(3, player1.getWeapons().size());

        //using space laser should do nothing to your total weapon count since it has infinite uses
        fireInput1 = new PlayerFireInput("Sonar Pulse", "4", "6");
        player1.attack(player2.getBoard(), fireInput1);

        //should have Space Laser (one object infinite uses) and 1 sonar pulse remaining
        assertEquals(2, player1.getWeapons().size());
    }
}
