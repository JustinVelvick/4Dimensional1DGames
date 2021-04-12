package edu.colorado.fourdimensionalonedgames;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_MULTIPLYPeer;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.Fleet;
import edu.colorado.fourdimensionalonedgames.game.ship.FleetControl;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {

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

    String singleShot;
    AttackResult simpleMiss;
    Ship player2Minesweeper;
    Ship player2Destroyer;
    Ship player2Battleship;


    @BeforeEach
    void setUp(){
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

        singleShot = "Single Shot";
        simpleMiss = new AttackResult(AttackResultType.MISS, null);
        /////////////////////////////////////////////////////////////////////////////////////

        //Player 1 placing a minesweeper at 1,1 down
        testInput1 = new PlayerShipInput("Down", "Minesweeper", "1", "1");
        player1.placeShip(testInput1);


        //Player 1 placing a destroyer at 4,4 down
        testInput2 = new PlayerShipInput("Down", "Destroyer", "4", "4");
        player1.placeShip(testInput2);


        //Player 1 placing a battleship at 5,5 down
        testInput3 = new PlayerShipInput("Down", "Battleship", "5", "5");
        player1.placeShip(testInput3);



        //Player 2 placing a minesweeper at 1,1 down
        testInput1 = new PlayerShipInput("Down", "Minesweeper", "1", "1");
        player2.placeShip(testInput1);


        //Player 2 placing a destroyer at 4,4 down
        testInput2 = new PlayerShipInput("Down", "Destroyer", "4", "4");
        player2.placeShip(testInput2);


        //Player 2 placing a battleship at 5,5 down
        testInput3 = new PlayerShipInput("Down", "Battleship", "5", "5");
        player2.placeShip(testInput3);
    }

    @Test
    void gameOver() {
        player1.placeShip(testInput1);
        assertFalse(game.gameOver());

        // sink all ships on board
        ///////////////////////////////  player1's attacks ///////////////////////////////////////////////
        //1 shot minesweeper by hitting 1,1 CQ
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        player1.attack(player2.getBoard(), fireInput1);

        //Destroyer at 4,4 down (CQ at 4,5)
        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");

        player1.attack(player2.getBoard(), fireInput1);
        player1.attack(player2.getBoard(), fireInput1);

        //Battleship at 5,5 down (CQ at 5,7)
        fireInput1 = new PlayerFireInput("Single Shot", "5", "7");
        player1.attack(player2.getBoard(), fireInput1);
        player1.attack(player2.getBoard(), fireInput1);


        ///////////////////////////////  player2's attacks ///////////////////////////////////////////////
        //1 shot minesweeper by hitting 1,1 CQ
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        player2.attack(player1.getBoard(), fireInput1);

        //Destroyer at 4,4 down (CQ at 4,5)
        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");

        player2.attack(player1.getBoard(), fireInput1);
        player2.attack(player1.getBoard(), fireInput1);

        //Battleship at 5,5 down (CQ at 5,7)
        fireInput1 = new PlayerFireInput("Single Shot", "5", "7");

        player2.attack(player1.getBoard(), fireInput1);
        player2.attack(player1.getBoard(), fireInput1);



        assertTrue(game.gameOver());
    }

    @Test
    //make sure that sonar pulse is only available AFTER successfully sinking the first enemy ship.
    //only 2 get added to their player object
    void sonarAvaliableTest(){

        //start of the game, do nothing, player should not have Sonar Pulse
        boolean hasSonar = false;
        for(Weapon weapon : player1.getWeapons()){
            if(weapon.getType().equals("Sonar Pulse")){
                hasSonar = true;
            }
        }

        assertFalse(hasSonar);

        //player1 sinking player2's minesweeper
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        player1.attack(player2.getBoard(), fireInput1);

        //now player1 should have access to 2 sonar pulse weapon objects
        //update game to see that player1 has now unlocked Sonar Pulse
        game.checkUpgrades();
        int sonarPulseCount = 0;
        for(Weapon weapon : player1.getWeapons()){
            if(weapon.getType().equals("Sonar Pulse")){
                sonarPulseCount += weapon.getCount();
            }
        }

        assertEquals(2, sonarPulseCount);
    }

    @Test
        //FROM THE REQUIREMENTS OF SPACE LASER:
            //The player receives the activation codes for the space laser only after sinking the first
            //enemy ship (i.e. this weapon is an upgrade, and replaces the conventional bomb in the
            //player’s arsenal).
    void spaceLaserAvaliableTest(){
        //start of the game, do nothing, player should not have Sonar Pulse
        boolean hasSingleShot = false;
        boolean hasSpaceLaser = false;
        for(Weapon weapon : player1.getWeapons()){
            if(weapon.getType().equals("Single Shot")){
                hasSingleShot = true;
            }
            if(weapon.getType().equals("Space Laser")){
                hasSpaceLaser = true;
            }
        }

        assertTrue(hasSingleShot);
        assertFalse(hasSpaceLaser);

        //now player1 will sink player2's minesweeper and should have single shot replaced with space laser
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        player1.attack(player2.getBoard(), fireInput1);


        //now player1 should have access to 2 sonar pulse weapon objects
        //update game to see that player1 has now unlocked space laser and sonar pulses
        game.checkUpgrades();

        hasSingleShot = false;
        hasSpaceLaser = false;
        for(Weapon weapon : player1.getWeapons()){
            if(weapon.getType().equals("Single Shot")){
                hasSingleShot = true;
            }
            if(weapon.getType().equals("Space Laser")){
                hasSpaceLaser = true;
            }
        }

        assertTrue(hasSpaceLaser);
        assertFalse(hasSingleShot);
    }

    @Test
    void passTurnSimulationTest(){

        assertTrue(game.isPlayer1Turn());
        assertEquals(0, player1.getScore());
        assertEquals(0, player2.getScore());

        //player 1 moving his fleet (should not be able to undo this movement next turn)
        FleetControl controller = new FleetControl(player1);
        controller.moveFleet(Orientation.right);
        controller.moveFleet(Orientation.down);

        //sinking player 2's minesweeper by hitting captains quarters once
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        player1.attack(player2.getBoard(),fireInput1);

        //called in Form controller, must call manually here in test since we have no form controllers
        game.updateScores();

        //change FXML scene and correct score should carry over
        game.passTurn();

        //sunk minesweeper, so player 1 should have 2 points, one for each minesweeper tile
        assertEquals(2, player1.getScore());
        assertEquals(0, player2.getScore());
        assertFalse(game.isPlayer1Turn());
    }
}
