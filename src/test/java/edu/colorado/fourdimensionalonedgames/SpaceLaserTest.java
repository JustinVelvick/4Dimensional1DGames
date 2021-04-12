package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.PopCountAfterAttackBehavior;
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

public class SpaceLaserTest {
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
        sonar = new LargeWeapon(new Reveal(), "Sonar", new PopCountAfterAttackBehavior(1));
        spaceLaser = new PenetratingSmallWeapon(new Attack(), "Space Laser");

        //Player2 places a minesweeper right at 1,1
        shipInput1 = new PlayerShipInput("Right", "Minesweeper", "1", "1");
        player2.placeShip(shipInput1);

        //Player2 places a surface boat at 3,3 right
        shipInput1 = new PlayerShipInput("Right", "Destroyer",  "3", "3");
        player2.placeShip(shipInput1);

        //Player2 places a submarine down at 3,3, submerged, right
        shipInput2 = new PlayerShipInput("Right", "Submarine",  "3", "3");
        shipInput2.setSubmergeChoice("Yes");
        player2.placeShip(shipInput2);
    }

    @Test
    void testSurfaceSpaceLaser(){

        fireInput1 = new PlayerFireInput("Space Laser", "2", "1");
        player1.addWeapon(spaceLaser);
        player1.attack(player2.getBoard(), fireInput1);

        assertTrue(player2Minesweeper.getShipTiles().get(1).shot);
        assertTrue(player2.getBoard().tiles[2][1][0].shot);
    }

    @Test
    void testSubmergedSpaceLaser(){



        //Player 1 fires a space laser at 3,3
        fireInput1 = new PlayerFireInput("Space Laser", "3", "3");
        player1.addWeapon(spaceLaser);
        player1.attack(player2.getBoard(), fireInput1);
        for(int z = 0; z < depth; z++){
            assertTrue(player2.getBoard().tiles[3][3][z].shot);
        }
        assertTrue(player2Destroyer.getShipTiles().get(0).shot);
        assertTrue(player2Submarine.getShipTiles().get(0).shot);
    }
}
