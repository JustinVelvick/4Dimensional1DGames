package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerControllerTest {

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
    }


}
