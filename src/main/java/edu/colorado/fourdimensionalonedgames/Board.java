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

    public GridPane grid;
    public Tile[][] tiles;
    public Render renderer;
    private List<Ship> ships = new ArrayList<>();

    public Board(int columns, int rows, Render renderer) {

        this.rows = rows;
        this.columns = columns;

        this.renderer = renderer;

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

    /**
     * Place a ship on the board in a valid orientation given an origin and direction
     *
     * @param direction the direction the ship points in from the placement origin
     * @param origin    origin of the ship's placement orientation
     * @param newShip   the ship to be placed
     * @return          boolean status indicating if the ship was successfully placed in this orientation
     */
    public boolean placeShip(Orientation direction, Point2D origin, Ship newShip){
        List<Point2D> newCoordinates = new ArrayList<>();

        double xCoordinate = origin.getX();
        double yCoordinate = origin.getY();

        // get the set of coordinates the ship would occupy assuming the given orientation is valid
        switch (direction){
            case up:
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

        // ensure no point in set falls off board or overlaps with existing ship tiles
        for (Point2D coordinate : newCoordinates) {
            if (coordinate.getX() < 1) return false;
            if (coordinate.getX() >= columns) return false;
            if (coordinate.getY() < 1) return false;
            if (coordinate.getY() >= rows) return false;

            Tile oldTile = tiles[(int) coordinate.getX()][(int) coordinate.getY()];
            if (oldTile instanceof ShipTile) return false;
        }

        // once orientation is determined valid, create and place new ShipTiles for the ship on the board
        ShipTile newTile;
        for (Point2D coordinate : newCoordinates) {
            int x = (int) coordinate.getX();
            int y = (int) coordinate.getY();
            newTile = new ShipTile(x, y);

            Tile oldTile = tiles[x][y];
            renderer.unregister(oldTile);

            renderer.register(newTile);

            grid.getChildren().remove(oldTile);
            grid.add(newTile, x, y);

            tiles[x][y] = newTile;
            newShip.addTile(newTile);
        }

        ships.add(newShip);

        return true;
    }

    /**
     * Mount an attack on the given coordinates
     *
     * @param attackCoords  the coordinates of tile on the board to be attacked
     * @return              returns the ship that was hit, null if the attack misses
     */
    public Ship attack(Point2D attackCoords){
        return null;
    }

    /**
     * Determines if the game is over based on the board state
     *
     * @return  boolean indicating if game is over
     */
    public boolean gameOver(){
        return false;
    }

}