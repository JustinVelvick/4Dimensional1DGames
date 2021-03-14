package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ColorTest {

    static int tileSize = 40;
    static int rows = 10;
    static int columns = 10;
    static int depth = 2;
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

        player1Board = new Board(columns, rows, depth, renderer);
        player1EnemyBoard = new Board(columns, rows, depth, renderer);
        player2Board = new Board(columns, rows, depth, renderer);
        player2EnemyBoard = new Board(columns, rows, depth, renderer);

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
    void testShipColors() {
        assertEquals(Color.BLUE, player2Destroyer.getColor());
        assertEquals(Color.INDIGO, player2Battleship.getColor());
        assertEquals(Color.GREEN, player2Minesweeper.getColor());
    }

    @Test
    void testDestroyedShipColors() {
        Point2D coord;
        //damage all of destroyer
        for (int y = 4; y <= 6; y++) {
            coord = new Point2D(4, y);
            player1.attack(player2.getBoard(), coord, singleShot);
        }
        //one more for CC
        coord = new Point2D(4, 5);
        player1.attack(player2.getBoard(), coord, singleShot);

        //hit minesweeper's CC which should shot the whole boat
        coord = new Point2D(1, 1);
        player1.attack(player2.getBoard(), coord, singleShot);

        //damage all of battleship
        for (int y = 5; y <= 8; y++) {
            coord = new Point2D(5, y);
            player1.attack(player2.getBoard(), coord, singleShot);
        }
        //one more for battleship CC
        coord = new Point2D(5, 7);
        player1.attack(player2.getBoard(), coord, singleShot);

        assertEquals(Color.RED, player2Minesweeper.getColor());
        assertEquals(Color.RED, player2Destroyer.getColor());
        assertEquals(Color.RED, player2Battleship.getColor());
    }

    @Test
    void testDamagedTileColors() {
        //damage minesweeper
        Point2D coord;
        coord = new Point2D(1, 2); //non CC tile for minesweeper
        player1.attack(player2.getBoard(), coord, singleShot);
        //damage destroyer
        coord = new Point2D(4, 4);
        player1.attack(player2.getBoard(), coord, singleShot);

        //damage battleship
        coord = new Point2D(5, 5);
        player1.attack(player2.getBoard(), coord, singleShot);


        assertEquals(Color.HOTPINK, player2.getBoard().tiles[1][2][0].getColor());
        assertEquals(Color.HOTPINK, player2.getBoard().tiles[4][4][0].getColor());
        assertEquals(Color.HOTPINK, player2.getBoard().tiles[5][5][0].getColor());
        assertEquals(Color.GREEN, player2.getBoard().tiles[1][1][0].getColor());
        assertEquals(Color.BLUE, player2.getBoard().tiles[4][6][0].getColor());
        assertEquals(Color.INDIGO, player2.getBoard().tiles[5][6][0].getColor());
    }

    @Test
    void testNonShipTileColors() {
        assertEquals(Color.CORNFLOWERBLUE, player2.getBoard().tiles[1][3][0].getColor());
        assertEquals(Color.TRANSPARENT, player2.getBoard().tiles[0][0][0].getColor());
    }
}
