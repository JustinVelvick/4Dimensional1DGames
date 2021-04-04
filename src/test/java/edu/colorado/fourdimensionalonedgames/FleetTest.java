package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.MineTile;
import edu.colorado.fourdimensionalonedgames.render.tile.SeaTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FleetTest {

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

    Tile mine;


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


        singleShot = "Single Shot";
        simpleMiss = new AttackResult(AttackResultType.MISS, null);
        /////////////////////////////////////////////////////////////////////////////////////
        //Player 2 placing a minesweeper at 1,1 down
        testInput1 = new PlayerShipInput("Down", "Minesweeper", "1", "1");
        //player2.placeShip(testInput1);


        //Player 2 placing a destroyer at 4,4 down
        testInput2 = new PlayerShipInput("Down", "Destroyer", "4", "4");
        //player2.placeShip(testInput2);


        //Player 2 placing a battleship at 5,5 down
        testInput3 = new PlayerShipInput("Down", "Battleship", "5", "5");
        //player2.placeShip(testInput3);


        //placing a mine at (1,3,0), directly below the minesweeper
        mine = new MineTile(1,3,0);
    }

    @Test
    void testFleetAdd() {
        assertFalse(player2.getFleet().hasShip());
        player2.placeShip(testInput1);
        assertTrue(player2.getFleet().hasShip());
    }


    @Test
    void testFleetRemove() {
        player2.placeShip(testInput2);
        assertTrue(player2.getFleet().hasShip());
        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");
        player1.attack(player2.getBoard(), fireInput1);
        player1.attack(player2.getBoard(), fireInput1);
        assertFalse(player2.getFleet().hasShip());
    }


    @Test
    void testFleetMove() {
        //need to be able to place mines in specific spots
        player2.placeShip(testInput1);
        assertTrue(player2.getFleet().hasShip());
        player2.moveFleet(Orientation.left); //hits two mines
        //CHANGE TO ASSERT FALSE
        assertTrue(player2.getFleet().hasShip());
    }


//    @Test
//    void testFleetDestroy() {
//        player2.placeShip(testInput3);
//        player2.getFleet().removeShip();
//        assertTrue(game.gameOver());
//
//    }
}