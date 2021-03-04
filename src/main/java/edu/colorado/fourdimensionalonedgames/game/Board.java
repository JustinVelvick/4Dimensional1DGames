package edu.colorado.fourdimensionalonedgames.game;


import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.Fleet;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.tile.LetterTile;
import edu.colorado.fourdimensionalonedgames.render.tile.SeaTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int rows;
    private final int columns;

    public Tile[][] tiles;
    public Render renderer;
    private Fleet fleet;

    public Board(int columns, int rows, Render renderer) {

        this.rows = rows;
        this.columns = columns;
        this.renderer = renderer;
        this.fleet = new Fleet();
        tiles = new Tile[columns + 1][rows + 1];


    }

    //Only called once upon game creation to make a sea of blank tile objects
    public void initializeBoard(GridPane gpane) {

        Tile tile;

        for (int j = 0; j <= columns; j++) {
            tile = new LetterTile(0, j, String.valueOf(j));
            this.renderer.register(tile);
            this.tiles[0][j] = tile;
            gpane.add(tile, 0, j);
        }


        for (int i = 1; i <= rows; i++) {
            tile = new LetterTile(i, 0, Character.toString((char) i + 64));
            this.renderer.register(tile);
            this.tiles[i][0] = tile;
            gpane.add(tile, i, 0);
        }


        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                tile = new SeaTile(i, j);
                this.renderer.register(tile);
                this.tiles[i][j] = tile;
                this.tiles[i][j].shot = false;
                gpane.add(tile, i, j);
            }
        }
    }

    public void updateEnemyGpane(GridPane gpane){
        Tile oldTile;
        Tile newTile;
        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                oldTile = this.tiles[i][j];
                //if any tile was shot, we need to display it on the other player's enemy grid
                if(oldTile.shot){
                    newTile = new SeaTile(i, j);
                    this.renderer.unregister(oldTile);
                    this.renderer.register(newTile);
                    this.tiles[i][j].shot = false;
                    gpane.getChildren().remove(oldTile);
                    gpane.add(newTile, i, j);
                }

            }
        }
    }

    /**
     * Place a new ship on the board given a placement orientation
     *
     * @param currentBoard  JavaFX Gpane object to place tile canvases onto
     * @param direction     direction the ship points in from the origin
     * @param origin        the origin of the placement
     * @param newShip       the ship to be placed
     * @return              boolean indicating ship placement success
     */
    public boolean placeShip(GridPane currentBoard, Orientation direction, Point2D origin, Ship newShip) {
        List<Point2D> newCoordinates = new ArrayList<>();

        double xCoordinate = origin.getX();
        double yCoordinate = origin.getY();

        // get coordinate set of tiles ship would occupy if placed in given orientation
        switch (direction) {
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

        // check each coordinate to make sure not off board or occupied by other ship
        for (Point2D coordinate : newCoordinates) {
            if (coordinate.getX() < 1) return false;
            if (coordinate.getX() > columns) return false;
            if (coordinate.getY() < 1) return false;
            if (coordinate.getY() > rows) return false;

            Tile oldTile = tiles[(int) coordinate.getX()][(int) coordinate.getY()];
            if (oldTile instanceof ShipTile) return false;
        }

        // if verified that placement is valid, add ship tiles to board
        ShipTile newTile;
        for (Point2D coordinate : newCoordinates) {
            int x = (int) coordinate.getX();
            int y = (int) coordinate.getY();
            newTile = new ShipTile(newShip, x, y);

            Tile oldTile = tiles[x][y];
            renderer.unregister(oldTile);

            renderer.register(newTile);

            currentBoard.getChildren().remove(oldTile);
            currentBoard.add(newTile, x, y);

            tiles[x][y] = newTile;
            newShip.addTile(newTile);
        }

        fleet.addShip(newShip);

        return true;
    }

    /**
     * Mount an attack on the given coordinates
     *
     * @param attackCoords  the coordinates of tile on the board to be attacked
     * @return              returns the ship that was hit, null if the attack misses
     */
    public Ship attack(Point2D attackCoords) {
        int x = (int) attackCoords.getX();
        int y = (int) attackCoords.getY();

        // check that provided coords are on board, throw exception if not
        if (x < 1 || x > columns || y < 1 || y > rows)
            throw new InvalidAttackException("attack coordinates off of board");

        // get tile to be attacked
        Tile attackedTile = tiles[x][y];

        // if already attacked, throw exception
        if (attackedTile.shot) throw new InvalidAttackException("tile has already been attacked");

        // otherwise set shot flag and return ship that contains tile
        attackedTile.shot = true;



        return attackedTile.getShip();
    }

    /**
     * Determines if the game is over based on the board state
     *
     * @return  boolean indicating if game is over
     */
    public boolean gameOver() {
        for (Ship ship : fleet.getShips()) {
            if (ship.destroyed() == false) return false;
        }
        return true;
    }

    public boolean isWithinBounds(Point2D coords) {
        int x = (int) coords.getX();
        int y = (int) coords.getY();

        // check that provided coords are on board, throw exception if not
        return !(x < 1 || x > columns || y < 1 || y > rows);
    }
}