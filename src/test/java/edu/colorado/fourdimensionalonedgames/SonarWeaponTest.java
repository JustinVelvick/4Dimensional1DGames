package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Reveal;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.LargeWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SonarWeaponTest {
    Board testBoard;
    Board testPlayerBoard;
    Player testPlayer;
    Ship testDestroyer, testMinesweeper, testBattleship;
    static int rows = 10;
    static int columns = 10;
    Render renderer;
    GridPane testgpane;
    AttackResult simpleMiss;
    String sonar;

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

        sonar = "Sonar Pulse";
    }

    @Test
    void testSonar() {
        Point2D attackPos = new Point2D(5, 5);
        testPlayer.attack(testBoard, attackPos, sonar);
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                // math to check if it's in the diamond
                System.out.println();
                if (!((Math.abs(x) == 1 && Math.abs(y) == 2) || (Math.abs(x) == 2 && Math.abs(y) == 1)) && (Math.abs(x) != 2 || Math.abs(y) != 2)) {
                    assertTrue(testBoard.tiles[5+x][5+y].revealed);
                } else {
                    assertFalse(testBoard.tiles[5+x][5+y].revealed);
                }
            }
        }
    }
}
