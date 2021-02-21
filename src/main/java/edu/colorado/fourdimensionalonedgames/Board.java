package edu.colorado.fourdimensionalonedgames;


import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int rows;
    private final int columns;

    public Tile[][] tiles;
    public Render renderer;
    private List<Ship> ships = new ArrayList<>();

    public Board(int columns, int rows, Render renderer) {

        this.rows = rows;
        this.columns = columns;
        this.renderer = renderer;

        tiles = new Tile[columns + 1][rows + 1];


    }

    public void initializeBoard(GridPane existingBoard){

        Tile tile;

        for (int j = 0; j <= columns; j++) {
            tile = new LetterTile(0, j, String.valueOf(j));
            this.renderer.register(tile);
            this.tiles[0][j] = tile;
            existingBoard.add(tile, 0, j);
        }


        for (int i = 1; i <= rows; i++) {
            tile = new LetterTile(i, 0, Character.toString((char) i + 64));
            this.renderer.register(tile);
            this.tiles[i][0] = tile;
            existingBoard.add(tile, i, 0);
        }


        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {

                tile = new Tile(i, j);
                this.renderer.register(tile);
                this.tiles[i][j] = tile;
                this.tiles[i][j].shot = false;
                existingBoard.add(tile, i, j);
            }
        }
    }

    public boolean placeShip(GridPane currentBoard, Orientation direction, Point2D origin, Ship newShip){
        List<Point2D> newCoordinates = new ArrayList<>();

        double xCoordinate = origin.getX();
        double yCoordinate = origin.getY();

        switch (direction){
            case up: //come back and check for overlaps
                for(double y = yCoordinate; y > (yCoordinate - newShip.size); y--) {
                    newCoordinates.add(new Point2D(xCoordinate, y));
                }
                break;

            case down:
                for(double y = yCoordinate; y < (yCoordinate + newShip.size); y++){
                    newCoordinates.add(new Point2D(xCoordinate, y));
                }
                break;

            case left:
                for(double x = xCoordinate; x > (xCoordinate - newShip.size); x--){
                    newCoordinates.add(new Point2D(x, yCoordinate));
                }
                break;
            case right:
                for(double x = xCoordinate; x < (xCoordinate + newShip.size); x++){
                    newCoordinates.add(new Point2D(x, yCoordinate));
                }
                break;
        }

        for (Point2D coordinate : newCoordinates) {
            if (coordinate.getX() < 1) return false;
            if (coordinate.getX() >= columns) return false;
            if (coordinate.getY() < 1) return false;
            if (coordinate.getY() >= rows) return false;
        }


        ShipTile newTile;
        for (Point2D coordinate : newCoordinates) {
            int x = (int) coordinate.getX();
            int y = (int) coordinate.getY();
            newTile = new ShipTile(x, y);

            Tile oldTile = tiles[x][y];
            renderer.unregister(oldTile);

            renderer.register(newTile);

            currentBoard.getChildren().remove(oldTile);
            currentBoard.add(newTile, x, y);

            tiles[x][y] = newTile;
            newShip.addTile(newTile);
        }

        ships.add(newShip);

        return true;
    }
}