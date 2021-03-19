package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.ship.FleetControl;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FleetMovementTest {

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
    void testSingleMove() {

        List<Integer> originalTiles = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            for (ShipTile tile : ship.getShipTiles()){
                originalTiles.add(tile.getRow());
            }
        }

        FleetControl controller = new FleetControl(player2.getFleet(), player2);
        controller.moveFleet(Orientation.down);

        List<ShipTile> movedTiles = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            movedTiles.addAll(ship.getShipTiles());
        }

        for (int i = 0; i < originalTiles.size(); i++) {
            assertEquals(originalTiles.get(i) + 1, movedTiles.get(i).getRow());
        }
    }

    @Test
    void testBoundaryNoMove() {

        List<Integer> originalTiles = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            for (ShipTile tile : ship.getShipTiles()){
                originalTiles.add(tile.getRow());
            }
        }

        FleetControl controller = new FleetControl(player2.getFleet(), player2);
        controller.moveFleet(Orientation.up);

        List<ShipTile> movedTiles = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            movedTiles.addAll(ship.getShipTiles());
        }

        //for non minesweeper ships
        for (int i = 0; i < originalTiles.size(); i++) {
            assertEquals(originalTiles.get(i), movedTiles.get(i).getRow());
        }
    }

    @Test
    void testFleetMovementUndo() {
        List<Integer> originalTileRows = new ArrayList<>();
        List<Integer> originalTileColumns = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            for (ShipTile tile : ship.getShipTiles()){
                originalTileRows.add(tile.getRow());
                originalTileColumns.add(tile.getColumn());
            }
        }

        FleetControl controller = new FleetControl(player2.getFleet(), player2);
        controller.moveFleet(Orientation.down);

        List<ShipTile> movedTiles = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            movedTiles.addAll(ship.getShipTiles());
        }

        //assert that each tile is at the same position after moving and then undoing once
        for (int i = 0; i < originalTileRows.size(); i++) {
            assertEquals(originalTileRows.get(i)+1, movedTiles.get(i).getRow());
            assertEquals(originalTileColumns.get(i), movedTiles.get(i).getColumn());
        }

        controller.undoMoveFleet();

        movedTiles.clear();
        for (Ship ship : player2.getFleet().getShips()) {
            movedTiles.addAll(ship.getShipTiles());
        }

        //assert that each tile is at the same position after moving and then undoing once
        for (int i = 0; i < originalTileRows.size(); i++) {
            assertEquals(originalTileRows.get(i), movedTiles.get(i).getRow());
            assertEquals(originalTileColumns.get(i), movedTiles.get(i).getColumn());
        }
    }

    @Test
    //down, right, undo, down, right, left, left
    void complexFleetMovement(){
        List<Integer> originalTileRows = new ArrayList<>();
        List<Integer> originalTileColumns = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            for (ShipTile tile : ship.getShipTiles()){
                originalTileRows.add(tile.getRow());
                originalTileColumns.add(tile.getColumn());
            }
        }

        FleetControl controller = new FleetControl(player2.getFleet(), player2);

        controller.moveFleet(Orientation.down);
        controller.moveFleet(Orientation.right);
        controller.undoMoveFleet();
        controller.moveFleet(Orientation.up);
        controller.moveFleet(Orientation.right);
        controller.moveFleet(Orientation.left);
        controller.moveFleet(Orientation.down);
        controller.moveFleet(Orientation.down);
        controller.moveFleet(Orientation.up);
        controller.moveFleet(Orientation.right);


        List<ShipTile> movedTiles = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            movedTiles.addAll(ship.getShipTiles());
        }

        //assert that each tile is at the same position after moving and then undoing once
        for (int i = 0; i < originalTileRows.size(); i++) {
            assertEquals(originalTileRows.get(i)+1, movedTiles.get(i).getRow());
            assertEquals(originalTileColumns.get(i)+1, movedTiles.get(i).getColumn());
        }
    }

    @Test
        //down, right, undo, down, right, left, left
    void complexUndoFleetMovement(){
        List<Integer> originalTileRows = new ArrayList<>();
        List<Integer> originalTileColumns = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            for (ShipTile tile : ship.getShipTiles()){
                originalTileRows.add(tile.getRow());
                originalTileColumns.add(tile.getColumn());
            }
        }

        FleetControl controller = new FleetControl(player2.getFleet(), player2);

        controller.moveFleet(Orientation.down);
        controller.moveFleet(Orientation.down);
        controller.undoMoveFleet();
        controller.moveFleet(Orientation.right);
        controller.moveFleet(Orientation.right);
        controller.moveFleet(Orientation.down);
        controller.moveFleet(Orientation.left);
        controller.undoMoveFleet();
        controller.undoMoveFleet();
        controller.undoMoveFleet();
        controller.undoMoveFleet();
        controller.undoMoveFleet();


        List<ShipTile> movedTiles = new ArrayList<>();
        for (Ship ship : player2.getFleet().getShips()) {
            movedTiles.addAll(ship.getShipTiles());
        }

        //assert that each tile is at the same position after since we called undo the same # of times as moving
        for (int i = 0; i < originalTileRows.size(); i++) {
            assertEquals(originalTileRows.get(i), movedTiles.get(i).getRow());
            assertEquals(originalTileColumns.get(i), movedTiles.get(i).getColumn());
        }
    }
}