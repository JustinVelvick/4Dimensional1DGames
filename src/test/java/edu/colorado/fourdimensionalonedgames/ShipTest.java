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
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import javafx.geometry.Point3D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

    String singleShotString;
    AttackResult simpleMiss;
    Ship player2Minesweeper;
    Ship player2Destroyer;
    Ship player2Battleship;
    Ship player2Submarine;

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
        player2Submarine = player2.getShipsToPlace().get(3);

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

        //placing a submarine at 2,2 down
        testInput1 = new PlayerShipInput("Down", "Submarine", "2", "2");
        player2.placeShip(testInput1);

        //placing a mine at (1,3,0)
        //mine = new MineTile(1,3,0); //MINETILE

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

    //sub specific shape only
    @Test
    void testSubmarineGenerateCoordinates(){

        //////////////See if sub generates correct coordinates for itself on SURFACE (9,9 up)///////////////
        Point3D origin = new Point3D(9,9,0);
        List<Point3D> results = player2Submarine.generateCoordinates(origin, Orientation.up);
        List<Point3D> expected = new ArrayList<>();
        //for the 4 submarine tiles lined up in a row
        for(int y = 9; y > 5; y--){
            expected.add(new Point3D(9,y,0));
        }
        //for the weird submarine protrusion tile
        expected.add(new Point3D(8,7,0));

        for(int i = 0; i < player2Submarine.getSize(); i++){
            assertEquals(expected.get(i), results.get(i));
        }

        /////////See if sub generates correct coordinates for itself below sea level (z=1)////////////////
        origin = new Point3D(9,9,1);
        results = player2Submarine.generateCoordinates(origin, Orientation.up);
        expected = new ArrayList<>();
        //for the 4 submarine tiles lined up in a row
        for(int y = 9; y > 5; y--){
            expected.add(new Point3D(9,y,1));
        }
        //for the weird submarine protrusion tile
        expected.add(new Point3D(8,7,1));

        for(int i = 0; i < player2Submarine.getSize(); i++){
            assertEquals(expected.get(i), results.get(i));
        }

        /////////Since sub protrustion tile is wacky, test again but with down and left to be sure///////////////
        //NOTE: 10 will be the y of the protruding tile, which is a good edge case to test anyways (still on board)
        origin = new Point3D(9,9,0);
        results = player2Submarine.generateCoordinates(origin, Orientation.left);
        expected = new ArrayList<>();
        //for the 4 submarine tiles lined up in a row
        for(int x = 9; x > 5; x--){
            expected.add(new Point3D(x,9,0));
        }
        //for the weird submarine protrusion tile
        expected.add(new Point3D(7,10,0));

        for(int i = 0; i < player2Submarine.getSize(); i++){
            assertEquals(expected.get(i), results.get(i));
        }

        ////////Sub placement down//////////////////////////////////////////////////////////////////////////////////
        origin = new Point3D(8,1,0);
        results = player2Submarine.generateCoordinates(origin, Orientation.down);
        expected = new ArrayList<>();
        //for the 4 submarine tiles lined up in a row
        for(int y = 1; y < 5; y++){
            expected.add(new Point3D(8,y,0));
        }
        //for the weird submarine protrusion tile
        expected.add(new Point3D(9,3,0));

        for(int i = 0; i < player2Submarine.getSize(); i++){
            assertEquals(expected.get(i), results.get(i));
        }

    }

    //ships with simple line shapes
    @Test
    void testLinearShipsGenerateCoordinates() {

        //////////////See if battleship generates correct coordinates for itself (9,1 down)///////////////
        Point3D origin = new Point3D(9, 1, 0);
        List<Point3D> results = player2Battleship.generateCoordinates(origin, Orientation.down);
        List<Point3D> expected = new ArrayList<>();
        //for the 4 battleship tiles lined up in a row
        for(int y = 1; y < 5; y++) {
            expected.add(new Point3D(9, y, 0));
        }

        for (int i = 0; i < player2Battleship.getSize(); i++) {
            assertEquals(expected.get(i), results.get(i));
        }
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
        assertTrue(result.getShip().destroyed());
        CaptainsQuartersTile captainsQ = (CaptainsQuartersTile)result.getShip().getShipTiles().get(0);
        assertEquals(captainsQ.getHp(), 0);
        assertSame(result.getType(), AttackResultType.SUNK);


        //damage Destroyer's CQ
        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        assertFalse(result.getShip().destroyed());
        captainsQ = (CaptainsQuartersTile)result.getShip().getShipTiles().get(1);
        assertEquals(captainsQ.getHp(), 1);
        assertSame(result.getType(), AttackResultType.MISS);

        //destroy entire Destroyer after hitting CQ one more time
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        assertTrue(result.getShip().destroyed());
        assertEquals(captainsQ.getHp(), 0);
        assertSame(result.getType(), AttackResultType.SUNK);


        //damage Battleship's CQ
        fireInput1 = new PlayerFireInput("Single Shot", "5", "7");
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        captainsQ = (CaptainsQuartersTile)result.getShip().getShipTiles().get(2);
        assertEquals(captainsQ.getHp(), 1);
        assertFalse(result.getShip().destroyed());
        assertSame(result.getType(), AttackResultType.MISS);

        //destroy entire Battleship after hitting CQ one more time
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        assertTrue(result.getShip().destroyed());
        assertEquals(captainsQ.getHp(), 0);
        assertSame(result.getType(), AttackResultType.SUNK);

        //damage Submarine's CQ (sub at 2,2 down) (CQ at 2,5)
        player1.addWeapon(spaceLaser);
        fireInput1 = new PlayerFireInput("Space Laser", "2", "5");
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        captainsQ = (CaptainsQuartersTile)result.getShip().getShipTiles().get(3);
        assertEquals(captainsQ.getHp(), 1);
        assertFalse(result.getShip().destroyed());
        assertSame(result.getType(), AttackResultType.MISS);

        //destroy entire Submarine after hitting CQ one more time
        results = player1.attack(player2.getBoard(), fireInput1);
        result = results.get(0);
        assertTrue(result.getShip().destroyed());
        assertEquals(captainsQ.getHp(), 0);
        assertSame(result.getType(), AttackResultType.SUNK);
    }

}