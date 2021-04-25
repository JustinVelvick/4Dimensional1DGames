package edu.colorado.fourdimensionalonedgames;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaceShipsTest {

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

    String singleShot;
    AttackResult simpleMiss;
    Ship player2Minesweeper;
    Ship player2Destroyer;
    Ship player2Battleship;

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
    }

    @Test
    void placeValidShip() {
        //placing a minsweeper at 1,1 down
        testInput1 = new PlayerShipInput("Down", "Minesweeper", "1", "1");
        assertTrue(player2.placeShip(testInput1));
        assertTrue(player2.getBoard().tiles[1][1][0] instanceof CaptainsQuartersTile);
        assertTrue(player2.getBoard().tiles[1][2][0] instanceof ShipTile);


        //placing a destroyer at 4,4 right
        testInput2 = new PlayerShipInput("Right", "Destroyer", "4", "4");
        assertTrue(player2.placeShip(testInput2));
        assertTrue(player2.getBoard().tiles[5][4][0] instanceof CaptainsQuartersTile);
        assertTrue(player2.getBoard().tiles[4][4][0] instanceof ShipTile);

        //placing a battleship at 2,8 up
        testInput3 = new PlayerShipInput("Up", "Battleship", "2", "8");
        assertTrue(player2.placeShip(testInput3));
        assertTrue(player2.getBoard().tiles[2][6][0] instanceof CaptainsQuartersTile);
        assertTrue(player2.getBoard().tiles[2][8][0] instanceof ShipTile);

        //placing a submarine at 8,2,0 down
        testInput3 = new PlayerShipInput("Down", "Submarine", "No", "8", "2");
        assertTrue(player2.placeShip(testInput3));
        assertTrue(player2.getBoard().tiles[8][5][0] instanceof CaptainsQuartersTile);
        assertTrue(player2.getBoard().tiles[8][2][0] instanceof ShipTile);
        //weird protruding sub tile
        assertTrue(player2.getBoard().tiles[9][4][0] instanceof ShipTile);
    }

    @Test
    void submergedPlaceShipTest() {
        //placing a submarine at 8,2,1 down
        testInput3 = new PlayerShipInput("Down", "Submarine", "Yes", "8", "2");
        assertTrue(player2.placeShip(testInput3));
        assertTrue(player2.getBoard().tiles[8][5][1] instanceof CaptainsQuartersTile);
        assertTrue(player2.getBoard().tiles[8][2][1] instanceof ShipTile);
        assertTrue(player2.getBoard().tiles[9][4][1] instanceof ShipTile);
    }

    @Test
    void placeInvalidShip() {
        // goes off top of board
        testInput1 = new PlayerShipInput("Up", "Battleship", "1", "1");
        assertFalse(player2.placeShip(testInput1));

        // goes off left side of board
        testInput2 = new PlayerShipInput("Left", "Destroyer", "1", "4");
        assertFalse(player2.placeShip(testInput2));

        // goes off bottom of board
        testInput3 = new PlayerShipInput("Down", "Battleship", "7", "9");
        assertFalse(player2.placeShip(testInput3));

        // goes off right side of board
        testInput1 = new PlayerShipInput("Right", "Minesweeper", "10", "1");
        assertFalse(player2.placeShip(testInput1));

        // invalid origin
        testInput2 = new PlayerShipInput("Down", "Minesweeper", "130", "1");
        assertFalse(player2.placeShip(testInput2));
    }

    @Test
    void placeOverlappingShips() {
        // place first ship, placement succeeds
        testInput1 = new PlayerShipInput("Down", "Battleship", "3", "3");
        assertTrue(player2.placeShip(testInput1));

        // place second, overlapping ship and placement should fail
        testInput2 = new PlayerShipInput("Left", "Destroyer", "4", "4");
        assertFalse(player2.placeShip(testInput2));
    }
}