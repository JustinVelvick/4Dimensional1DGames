package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShipFormControllerTest {

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

    PlayerShipInput input;
    PlayerShipInput badInput;

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

        input = new PlayerShipInput("Down","Destroyer","B", "3");
        badInput = new PlayerShipInput("", "Destroyer", "Applesauce", "032485*");
    }

    @Test
    void testValidateForm(){
        ShipChoiceFormController controller = new ShipChoiceFormController();
        Boolean result = controller.validateForm(input);
    }


}
