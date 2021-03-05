package edu.colorado.fourdimensionalonedgames;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SingleShot;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class SingleShotWeaponTest {

    Board testBoard;
    Board testPlayerBoard;
    Player testPlayer;
    Ship testDestroyer, testMinesweeper, testBattleship;
    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    Render renderer;
    GridPane testgpane;
    AttackResult simpleMiss;
    Weapon singleShot;

    @BeforeEach
    void setUp() {


        testgpane = new GridPane();
        renderer = new Render();
        testBoard = new Board(columns, rows, renderer);
        testBoard.initializeBoard(testgpane);
        testPlayerBoard = new Board(columns, rows, renderer);
        testPlayerBoard.initializeBoard(testgpane);
        testPlayer = new Player(testPlayerBoard, testBoard);

        simpleMiss = new AttackResult(AttackResultType.MISS, null);

        testDestroyer = new Destroyer();
        testMinesweeper = new Minesweeper();
        testBattleship = new Battleship();

        Orientation direction = Orientation.down;
        Point2D origin = new Point2D(2,2);
        testBoard.placeShip(testgpane, direction, origin, testDestroyer);

        direction = Orientation.right;
        origin = new Point2D(4,4);
        testBoard.placeShip(testgpane, direction, origin, testMinesweeper);

        singleShot = new SingleShot();
    }

    @Test
    void attackMiss() {
        Point2D attackCoords = new Point2D(1,1);
        assertEquals(testPlayer.attack(testBoard, attackCoords, singleShot), simpleMiss);

        attackCoords = new Point2D(2,2);
        assertNotEquals(testPlayer.attack(testBoard, attackCoords, singleShot), simpleMiss);
    }

    @Test
    void attackHit() {
        Point2D attackCoords = new Point2D(2,2);
        AttackResult result = testPlayer.attack(testBoard, attackCoords, singleShot);

        // assertions after one hit
        assertEquals(new AttackResult(AttackResultType.HIT, testDestroyer), result);
        assertEquals(1, result.ship.damage());

        // sink ship
        attackCoords = new Point2D(2,3);
        result = testPlayer.attack(testBoard, attackCoords, singleShot);
        attackCoords = new Point2D(2,4);
        result = testPlayer.attack(testBoard, attackCoords, singleShot);

        // assertions after ship is sunk
        assertEquals(new AttackResult(AttackResultType.SUNK, testDestroyer), result);
        assertEquals(3, result.ship.damage());
    }

    @Test
    void offBoardAttack() {
        // deal with attacks that fall off of the game board
        assertThrows(InvalidAttackException.class, () -> {
            Point2D attackCoords = new Point2D(0,0);
            testPlayer.attack(testBoard, attackCoords, singleShot);
        });

        assertThrows(InvalidAttackException.class, () -> {
            Point2D attackCoords = new Point2D(11,11);
            testPlayer.attack(testBoard, attackCoords, singleShot);
        });
    }

    @Test
    void edgeBoardAttack() {
        // deal with attacks that barely don't fall off of the game board
        assertDoesNotThrow(() -> {
            Point2D attackCoords = new Point2D(1,1);
            testBoard.attack(attackCoords);
        });

        assertDoesNotThrow(() -> {
            Point2D attackCoords = new Point2D(10,10);
            testBoard.attack(attackCoords);
        });
    }

    @Test
    void captainsQuartersDamaged(){
        //public boolean placeShip(GridPane currentBoard, Orientation direction, Point2D origin, Ship newShip)
        //destroyer ship is located at (2,2) down
        //CaptainsQuarters should be in the middle for destroyers, so (2,3)
        Point2D cord1 = new Point2D(2,3);
        testBoard.attack(cord1);

        CaptainsQuartersTile tile = (CaptainsQuartersTile)testDestroyer.getShipTiles().get(1);
        assertTrue(tile.getHp() == 1);
        assertTrue(testDestroyer.destroyed() == false);
    }

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
        AttackResult result = testPlayer.attack(testBoard, attackCoords, singleShot);

        assertEquals(result.type, AttackResultType.SURRENDER);
    }

}
