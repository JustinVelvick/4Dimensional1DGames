package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.Destroyer;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.Render;
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
    void setUp() {
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

//    @Test
//    void testDamageShip() {
//        assertEquals(0, testShip.damage());
//        tile1.shot = true;
//        assertEquals(1, testShip.damage());
//        tile2.shot = true;
//        tile3.shot = true;
//        assertEquals(3, testShip.damage());
//    }
//
//    @Test
//    void testShipDeath() {
//        // test undamaged, partially damaged, and destroyed ship
//        assertFalse(testShip.destroyed());
//        tile1.shot = true;
//        assertFalse(testShip.destroyed());
//        tile2.shot = true;
//        tile3.shot = true;
//        assertTrue(testShip.destroyed());
//    }

    @Test
    void testCaptainQuarters(){
        Point2D point = new Point2D(1,1);
        List<AttackResult> results = player1.attack(player2.getBoard(), point, "Single Shot");
        AttackResult result = results.get(0);
        assertTrue(result.ship.destroyed());

        point = new Point2D(4,5);
        player1.attack(player2.getBoard(), point, "Single Shot");
        results = player1.attack(player2.getBoard(), point, "Single Shot");
        result = results.get(0);
        assertTrue(result.ship.destroyed());


        point = new Point2D(5,7);
        player1.attack(player2.getBoard(), point, "Single Shot");
        results = player1.attack(player2.getBoard(), point, "Single Shot");
        result = results.get(0);
        assertTrue(result.ship.destroyed());
    }

}