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
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
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

public class BasicAttacksTest {

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
    //player1 attacking player2
    void attackMiss() {
        //player 1 attacking player 2's minesweeper
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        AttackResult result = (player1.attack(player2.getBoard(), fireInput1)).get(0);
        assertNotEquals(result, simpleMiss);

        //player 1 missing
        fireInput1 = new PlayerFireInput("Single Shot", "6", "6");
        result = (player1.attack(player2.getBoard(), fireInput1)).get(0);
        assertEquals(result, simpleMiss);
    }

    @Test
    //player1 attacking player2
    void attackHit() {
        fireInput1 = new PlayerFireInput("Single Shot", "1", "2");
        AttackResult result = player1.attack(player2.getBoard(), fireInput1).get(0);
        AttackResult expected = new AttackResult(AttackResultType.HIT, player2Minesweeper);
        // assertions after one hit
        assertEquals(expected, result);
        assertEquals(1, result.ship.damage());

        // sink minesweeper ship
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        result = player1.attack(player2.getBoard(), fireInput1).get(0);

        expected = new AttackResult(AttackResultType.SUNK, player2Minesweeper);
        // assertions after ship is sunk
        assertEquals(expected, result);
        assertTrue(player2Minesweeper.destroyed());
    }

    //player1 attacking player 2
    @Test
    void offBoardAttack() {
        // deal with attacks that fall off of the game board
        fireInput1 = new PlayerFireInput("Single Shot", "0", "0");
        assertThrows(InvalidAttackException.class, () -> player1.attack(player2.getBoard(), fireInput1));

        fireInput1 = new PlayerFireInput("Single Shot", "11", "11");
        assertThrows(InvalidAttackException.class, () -> player1.attack(player2.getBoard(), fireInput1));
    }

    //player1 attacking player 2
    @Test
    void edgeBoardAttack() {
        // deal with attacks that barely don't fall off of the game board
        fireInput1 = new PlayerFireInput("Single Shot", "1", "10");
        assertDoesNotThrow(() -> {
            player1.attack(player2.getBoard(), fireInput1);
        });

        fireInput1 = new PlayerFireInput("Single Shot", "10", "1");
        assertDoesNotThrow(() -> {
            player1.attack(player2.getBoard(), fireInput1);
        });
    }

    @Test
    void captainsQuartersDamaged(){
        //destroyer is located at (4,4) down
        //CaptainsQuarters will be at (4,5) for a destroyer placed here

        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");
        player1.attack(player2.getBoard(), fireInput1);

        CaptainsQuartersTile tile = (CaptainsQuartersTile)player2Destroyer.getShipTiles().get(1);
        assertEquals(tile.getHp(), 1);
        assertFalse(player2Destroyer.destroyed());
    }

    @Test
    void captainsQuartersSunk(){
        //destroyer ship is located at (4,4) down
        //CaptainsQuarters should be in the middle for destroyers, so (4,5)
        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");
        fireInput2 = new PlayerFireInput("Single Shot", "4", "5");

        //removes armour the first shot, kill whole boat the second shot
        player1.attack(player2.getBoard(), fireInput1);
        player1.attack(player2.getBoard(), fireInput2);

        CaptainsQuartersTile tile = (CaptainsQuartersTile)player2Destroyer.getShipTiles().get(1);
        assertEquals(tile.getHp(), 0);
        assertTrue(player2Destroyer.destroyed());
    }

    @Test
    void captainsQuartersSunkMinesweeper(){
        //CaptainsQuarters on minesweepers do not have armour, they get one shot
        //testMinesweeper ship is located at (2,2) right
        //CaptainsQuarters should on the origin for minesweepers, so (4,4)

        //kills entire minesweeper in one shot (hitting captains quarters)
        fireInput1 = new PlayerFireInput("Single Shot", "4", "4");
        player1.attack(player2.getBoard(), fireInput1);


        CaptainsQuartersTile tile = (CaptainsQuartersTile)player2Minesweeper.getShipTiles().get(0);
        assertEquals(tile.getHp(), 0);
        assertTrue(player2Minesweeper.destroyed());
    }

    @Test
    void duplicateAttack() {
        // attack a tile that has already been attacked (Battleship at 5,5 down)
        fireInput1 = new PlayerFireInput("Single Shot", "5", "5");
        player1.attack(player2.getBoard(), fireInput1);

        //repeat attack on same tile and throw error
        assertThrows(InvalidAttackException.class, () -> player1.attack(player2.getBoard(), fireInput1));
    }

    @Test
    void gameOver() {
        assertFalse(game.gameOver());

        // sink all ships on board
        //1 shot minesweeper by hitting 1,1 CQ
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        player1.attack(player2.getBoard(), fireInput1);

        //Destroyer at 4,4 down (CQ at 4,5)
        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");

        player1.attack(player2.getBoard(), fireInput1);
        player1.attack(player2.getBoard(), fireInput1);

        //Battleship at 5,5 down (CQ at 5,7)
        fireInput1 = new PlayerFireInput("Single Shot", "5", "7");
        fireInput1 = new PlayerFireInput("Single Shot", "5", "7");

        assertTrue(game.gameOver());
    }

}
