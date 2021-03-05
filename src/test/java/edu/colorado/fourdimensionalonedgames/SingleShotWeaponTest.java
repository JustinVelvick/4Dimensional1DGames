package edu.colorado.fourdimensionalonedgames;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SingleShotWeaponTest {

    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    static int numberOfPlayers = 2;

    Board player1Board;
    Board player1EnemyBoard;
    Board player2Board;
    Board player2EnemyBoard;

    GridPane player1gpane;
    GridPane player1enemygpane;
    GridPane player2gpane;
    GridPane player2enemygpane;

    Render renderer;

    Player player1;
    Player player2;

    String singleShot;
    AttackResult simpleMiss;
    Ship player2Minesweeper;
    Ship player2Destroyer;
    Ship player2Battleship;


    @BeforeEach
    void setUp(){
        renderer = new Render();

        player1Board = new Board(columns, rows, renderer);
        player1EnemyBoard = new Board(columns, rows, renderer);
        player2Board = new Board(columns, rows, renderer);
        player2EnemyBoard = new Board(columns, rows, renderer);

        player1gpane = new GridPane();
        player1enemygpane = new GridPane();
        player2gpane = new GridPane();
        player2enemygpane = new GridPane();

        player1Board.initializeBoard(player1gpane);
        player1EnemyBoard.initializeBoard(player1enemygpane);
        player2Board.initializeBoard(player2gpane);
        player2EnemyBoard.initializeBoard(player2enemygpane);

        player1 = new Player(player1Board, player1EnemyBoard);
        player2 = new Player(player2Board, player2EnemyBoard);

        player2Minesweeper = player2.getShipsToPlace().get(0);
        player2Destroyer = player2.getShipsToPlace().get(1);
        player2Battleship = player2.getShipsToPlace().get(2);

        singleShot = "Single Shot";
        simpleMiss = new AttackResult(AttackResultType.MISS, null);

        //player 2 placing a minesweeper down
        Orientation direction = Orientation.down;
        Point2D origin = new Point2D(1,1);

        player2.placeShip(player2gpane, direction, origin, player2Minesweeper);

        //player 2 placing a destroyer down
        direction = Orientation.down;
        origin = new Point2D(4,4);

        player2.placeShip(player2gpane, direction, origin, player2Destroyer);

        //player 2 placing a battleship down
        direction = Orientation.down;
        origin = new Point2D(5,5);

        player2.placeShip(player2gpane, direction, origin, player2Battleship);
    }

    @Test
    //player1 attacking player2
    void attackMiss() {
        //player 2 placing a ship down
        Orientation direction = Orientation.down;
        Point2D origin = new Point2D(1,1);

        player2.placeShip(player2gpane, direction, origin, player2Minesweeper);

        //player 1 attacking player 2's minesweeper
        Point2D attackCoords = new Point2D(1,1);
        AttackResult result = (player1.attack(player2.getBoard(), attackCoords, singleShot)).get(0);
        System.out.println(result.ship);
        assertNotEquals(result, simpleMiss);

        //player 1 missing
        attackCoords = new Point2D(6,6);
        result = (player1.attack(player2.getBoard(), attackCoords, singleShot)).get(0);
        assertEquals(result, simpleMiss);
    }

    @Test
    //player1 attacking player2
    void attackHit() {
        Point2D attackCoords = new Point2D(1,2);
        AttackResult result = player1.attack(player2.getBoard(), attackCoords, singleShot).get(0);
        AttackResult expected = new AttackResult(AttackResultType.HIT, player2Minesweeper);
        // assertions after one hit
        assertEquals(expected, result);
        assertEquals(1, result.ship.damage());

        // sink minesweeper ship
        attackCoords = new Point2D(1,1);
        result = player1.attack(player2.getBoard(), attackCoords, singleShot).get(0);

        expected = new AttackResult(AttackResultType.SUNK, player2Minesweeper);
        // assertions after ship is sunk
        assertEquals(expected, result);
        assertEquals(2, result.ship.damage());
    }

    //player1 attacking player 2
    @Test
    void offBoardAttack() {
        // deal with attacks that fall off of the game board
        assertThrows(InvalidAttackException.class, () -> {
            Point2D attackCoords = new Point2D(0,0);
            player1.attack(player2.getBoard(), attackCoords, singleShot);
        });

        assertThrows(InvalidAttackException.class, () -> {
            Point2D attackCoords = new Point2D(11,11);
            player1.attack(player2.getBoard(), attackCoords, singleShot);
        });
    }

    //player1 attacking player 2
    @Test
    void edgeBoardAttack() {
        // deal with attacks that barely don't fall off of the game board
        assertDoesNotThrow(() -> {
            Point2D attackCoords = new Point2D(1,1);
            player1.attack(player2.getBoard(), attackCoords, singleShot);
        });

        assertDoesNotThrow(() -> {
            Point2D attackCoords = new Point2D(10,10);
            player1.attack(player2.getBoard(), attackCoords, singleShot);
        });
    }

    @Test
    void captainsQuartersDamaged(){
        //destroyer is located at (4,4) down
        //CaptainsQuarters will be at (4,5) for a destroyer placed here

        Point2D cord1 = new Point2D(4,5);
        player1.attack(player2.getBoard(), cord1, singleShot);

        CaptainsQuartersTile tile = (CaptainsQuartersTile)player2Destroyer.getShipTiles().get(1);
        System.out.println(tile.getHp());
        assertTrue(tile.getHp() == 1);
        assertFalse(player2Destroyer.destroyed());
    }
/*
    @Test
    void captainsQuartersSunk(){
        //destroyer ship is located at (2,2) down
        //CaptainsQuarters should be in the middle for destroyers, so (2,3)
        Point2D cord1 = new Point2D(2,3);

        //removes armour the first shot, kills the second shot
        testBoard.attack(cord1);
        testBoard.attack(cord1);

        CaptainsQuartersTile tile = (CaptainsQuartersTile)testDestroyer.getShipTiles().get(1);
        assertTrue(tile.getHp() == 0);
        assertTrue(testDestroyer.destroyed() == true);
    }

    @Test
    void captainsQuartersSunkMinesweeper(){
        //CaptainsQuarters on minesweepers do not have armour, they get one shot
        //testMinesweeper ship is located at (4,4) right
        //CaptainsQuarters should on the origin for minesweepers, so (4,4)
        Point2D cord1 = new Point2D(4,4);

        //kills entire minesweeper in one shot (hitting captains quarters)
        testBoard.attack(cord1);


        CaptainsQuartersTile tile = (CaptainsQuartersTile)testMinesweeper.getShipTiles().get(0);
        assertTrue(tile.getHp() == 0);
        assertTrue(testMinesweeper.destroyed() == true);
    }

    @Test
    void duplicateAttack() {
        // attack a tile that has already been attacked
        Point2D attackCoords = new Point2D(1, 1);
        testPlayer.attack(testBoard, attackCoords, singleShot);

        assertThrows(InvalidAttackException.class, () -> {
            testPlayer.attack(testBoard, attackCoords, singleShot);
        });
    }

    @Test
    void gameOver() {
        assertFalse(testBoard.gameOver());

        // sink all ships on board
        Point2D attackCoords = new Point2D(2,2);
        testPlayer.attack(testBoard, attackCoords, singleShot);
        attackCoords = new Point2D(2,3);
        testPlayer.attack(testBoard, attackCoords, singleShot);
        attackCoords = new Point2D(2,4);
        testPlayer.attack(testBoard, attackCoords, singleShot);
        attackCoords = new Point2D(4,4);
        testPlayer.attack(testBoard, attackCoords, singleShot);
        attackCoords = new Point2D(5,4);
        AttackResult result = testPlayer.attack(testBoard, attackCoords, singleShot).get(0);

        assertEquals(result.type, AttackResultType.SURRENDER);
    }*/

}
