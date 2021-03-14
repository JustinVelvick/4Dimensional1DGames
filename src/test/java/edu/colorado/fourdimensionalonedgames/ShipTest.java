package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.Destroyer;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

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
    Ship player2Submarine;


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
        player2Submarine = player2.getShipsToPlace().get(3);

        singleShot = "Single Shot";
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

        //placing a submarine at 2,2 down
        testInput1 = new PlayerShipInput("Down", "Submarine", "2", "2");
        player2.placeShip(testInput1);
    }

    @Test
    void shipCreation(){

        //Minesweeper
        List<ShipTile> tiles = player2Minesweeper.getShipTiles();

        for(ShipTile tile : tiles){
            assertFalse(tile.shot);
            assertFalse(tile.revealed);
        }
        assertEquals(0, player2Minesweeper.damage());
        assertFalse(player2Minesweeper.destroyed());

        //Destroyer
        tiles = player2Destroyer.getShipTiles();

        for(ShipTile tile : tiles){
            assertFalse(tile.shot);
            assertFalse(tile.revealed);
        }
        assertEquals(0, player2Destroyer.damage());
        assertFalse(player2Destroyer.destroyed());


        //Battleship
        tiles = player2Battleship.getShipTiles();

        for(ShipTile tile : tiles){
            assertFalse(tile.shot);
            assertFalse(tile.revealed);
        }
        assertEquals(0, player2Battleship.damage());
        assertFalse(player2Battleship.destroyed());
    }

    @Test
    void submergableShipCreation(){

        //Submarine
        List<ShipTile> tiles = player2Submarine.getShipTiles();

        for(ShipTile tile : tiles){
            assertFalse(tile.shot);
            assertFalse(tile.revealed);
        }
        assertEquals(0, player2Submarine.damage());
        assertFalse(player2Submarine.destroyed());
    }

    @Test
    void testDamageShip() {


    }

    @Test
    void testShipDeath() {

    }

    @Test
    void testCaptainQuarters(){
        //damage Minesweeper's CQ (one shots it)
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        List<AttackResult> results = player1.attack(player2.getBoard(), fireInput1);
        AttackResult result = results.get(0);
        assertTrue(result.ship.destroyed());
        CaptainsQuartersTile captainsQ = (CaptainsQuartersTile)result.ship.getShipTiles().get(1);
        assertEquals(captainsQ.getHp(), 0);
        assertSame(result.type, AttackResultType.SUNK);


        //damage Destroyer's CQ
        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        assertFalse(result.ship.destroyed());
        captainsQ = (CaptainsQuartersTile)result.ship.getShipTiles().get(1);
        assertEquals(captainsQ.getHp(), 1);
        assertSame(result.type, AttackResultType.HIT);

        //destroy entire Destroyer after hitting CQ one more time
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        assertTrue(result.ship.destroyed());
        assertEquals(captainsQ.getHp(), 0);
        assertSame(result.type, AttackResultType.SUNK);


        //damage Battleship's CQ
        fireInput1 = new PlayerFireInput("Single Shot", "5", "7");
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        captainsQ = (CaptainsQuartersTile)result.ship.getShipTiles().get(2);
        assertEquals(captainsQ.getHp(), 1);
        assertFalse(result.ship.destroyed());
        assertSame(result.type, AttackResultType.HIT);

        //destroy entire Battleship after hitting CQ one more time
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        assertTrue(result.ship.destroyed());
        assertEquals(captainsQ.getHp(), 0);
        assertSame(result.type, AttackResultType.SUNK);
    }

}