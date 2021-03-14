package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
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
            fireInput1 = new PlayerFireInput("Single Shot", "4", Integer.toString(y));
            player1.attack(player2.getBoard(), fireInput1);
        }
        //one more for CC
        fireInput1 = new PlayerFireInput("Single Shot", "4", "5");
        player1.attack(player2.getBoard(), fireInput1);

        //hit minesweeper's CC which should one shot the whole boat
        fireInput1 = new PlayerFireInput("Single Shot", "1", "1");
        player1.attack(player2.getBoard(), fireInput1);

        //damage all of battleship
        for (int y = 5; y <= 8; y++) {
            fireInput1 = new PlayerFireInput("Single Shot", "5", Integer.toString(y));
            player1.attack(player2.getBoard(), fireInput1);
        }
        //one more for battleship CC
        fireInput1 = new PlayerFireInput("Single Shot", "5", "7");
        player1.attack(player2.getBoard(), fireInput1);

        assertEquals(Color.RED, player2Minesweeper.getColor());
        assertEquals(Color.RED, player2Destroyer.getColor());
        assertEquals(Color.RED, player2Battleship.getColor());
    }

    @Test
    void testDamagedTileColors() {
        //damage minesweeper
        fireInput1 = new PlayerFireInput("Single Shot", "1", "2");
        player1.attack(player2.getBoard(), fireInput1);
        //damage destroyer
        fireInput1 = new PlayerFireInput("Single Shot", "4", "4");
        player1.attack(player2.getBoard(), fireInput1);

        //damage battleship
        fireInput1 = new PlayerFireInput("Single Shot", "5", "5");
        player1.attack(player2.getBoard(), fireInput1);


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
