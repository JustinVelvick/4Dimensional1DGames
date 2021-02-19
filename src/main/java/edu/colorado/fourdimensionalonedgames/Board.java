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

    private int rows;
    private int columns;

    public GridPane grid;
    public Tile[][] tiles;

    private List<Ship> ships = new ArrayList<>();

    public Board(int columns, int rows, Render renderer) {

        grid = new GridPane();
        tiles = new Tile[columns + 1][rows + 1];

        grid.setPadding(new Insets(10, 10, 10, 10));
        //grid.setVgap(8);
        //grid.setHgap(10);


        Tile tile;

        for (int j = 0; j <= columns; j++) {
            tile = new LetterTile(0, j, String.valueOf(j));
            renderer.register(tile);
            tiles[0][j] = tile;
            grid.add(tile, 0, j);
        }


        for (int i = 1; i <= rows; i++) {
            tile = new LetterTile(i, 0, Character.toString((char) i + 64));
            renderer.register(tile);
            tiles[i][0] = tile;
            grid.add(tile, i, 0);
        }


        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {

                tile = new Tile(i, j);
                renderer.register(tile);
                tiles[i][j] = tile;
                tiles[i][j].shot = false;
                grid.add(tile, i, j);
            }
        }


    }

    public boolean placeShip(Orientation direction, Point2D origin, Ship newShip){
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
            if (coordinate.getX() < 0) return false;
            if (coordinate.getX() >= columns) return false;
            if (coordinate.getY() < 0) return false;
            if (coordinate.getY() >= rows) return false;
        }

        for (Point2D coordinate : newCoordinates) {
            int x = (int) coordinate.getX();
            int y = (int) coordinate.getY();
            tiles[x][y] = new ShipTile(x,y);
            newShip.addTile((ShipTile) tiles[x][y]);
        }

        ships.add(newShip);

        return true;
    }
}