package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.PopCountAfterAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Reveal;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.LargeWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.PenetratingSmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SonarWeaponTest {
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

    PlayerShipInput testInput1;
    PlayerShipInput testInput2;
    PlayerShipInput testInput3;

    PlayerFireInput fireInput1;
    PlayerFireInput fireInput2;
    PlayerFireInput fireInput3;

    String singleShotString;
    AttackResult simpleMiss;
    Ship player2Minesweeper;
    Ship player2Destroyer;
    Ship player2Battleship;

    Weapon spaceLaser = new PenetratingSmallWeapon(new Attack(), "Space Laser");
    Weapon sonarPulse = new LargeWeapon(new Reveal(), "Sonar Pulse", new PopCountAfterAttackBehavior(1));
    Weapon singleShot = new SmallWeapon(new Attack(), "Single Shot");

    @BeforeEach
    void setUp() {

        renderer = new Render();
        game = new Game(renderer, numberOfPlayers, tileSize, columns, rows, depth);
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);

        player1gpane = new GridPane();
        player1enemygpane = new GridPane();
        player2gpane = new GridPane();
        player2enemygpane = new GridPane();

        player2Minesweeper = player2.getShipsToPlace().get(0);
        player2Destroyer = player2.getShipsToPlace().get(1);
        player2Battleship = player2.getShipsToPlace().get(2);

        singleShotString = "Single Shot";
        simpleMiss = new AttackResult(AttackResultType.MISS, null);

        //placing a minsweeper at 1,1 down
        testInput1 = new PlayerShipInput("Down", "Minesweeper", "1", "1");
        player2.placeShip(testInput1);


        //placing a destroyer at 4,4 down
        testInput2 = new PlayerShipInput("Down", "Destroyer", "4", "4");
        player2.placeShip(testInput2);


        //placing a battleship at 5,5 down
        testInput3 = new PlayerShipInput("Down", "Battleship", "5", "5");
        player2.placeShip(testInput3);
    }

    @Test
    void testSonar() {
        fireInput1 = new PlayerFireInput("Sonar Pulse", "5", "5");
        player1.addWeapon(sonarPulse);
        player1.attack(player2.getBoard(), fireInput1);
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                // math to check if tiles in the diamond are revealed
                if (!((Math.abs(x) == 1 && Math.abs(y) == 2) || (Math.abs(x) == 2 && Math.abs(y) == 1)) && (Math.abs(x) != 2 || Math.abs(y) != 2)) {
                    assertTrue(player2.getBoard().tiles[5+x][5+y][0].revealed);
                } else {
                    assertFalse(player2.getBoard().tiles[5+x][5+y][0].revealed);
                }
            }
        }
    }
}
