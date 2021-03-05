package edu.colorado.fourdimensionalonedgames.game;


import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.Fleet;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.tile.*;
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

        // if verified that placement is valid, generate tiles with proper x and y values
        List<ShipTile> tilesToAdd = new ArrayList<>();

        for (Point2D coordinate : newCoordinates) {
            int x = (int) coordinate.getX();
            int y = (int) coordinate.getY();
            tilesToAdd.add(new ShipTile(newShip, x, y));
        }

        //replace one tile with an appropriate CaptainsQuartersTile
        generateCaptainsQuarters(tilesToAdd);

        //now actually add this correct list of tilesToAdd to the Ship object, the Board tiles array, and the gpanes
        Tile oldTile;
        for(ShipTile tile : tilesToAdd){
            int x = tile.getColumn();
            int y = tile.getRow();

            //get the old tile object from the board tile array
            oldTile = tiles[x][y];

            //re-register that spot with the renderer
            renderer.unregister(oldTile);
            renderer.register(tile);

            currentBoard.getChildren().remove(oldTile);
            currentBoard.add(tile, x, y);

            tiles[x][y] = tile;
            newShip.addTile(tile);
        }

        //add this completed, built ship to the fleet
        fleet.addShip(newShip);

        return true;
    }

    private void generateCaptainsQuarters(List<ShipTile> tiles){

        Ship parentShip = tiles.get(0).getShip();
        ShipTile newTile;
        Tile tileToReplace;
        switch (parentShip.getType()) {
            case "Minesweeper":
                tileToReplace = tiles.get(0);
                newTile = new CaptainsQuartersTile(parentShip, tileToReplace.getColumn(), tileToReplace.getRow(), 1);
                tiles.set(0, newTile);
                break;
            case "Destroyer":
                tileToReplace = tiles.get(1);
                newTile = new CaptainsQuartersTile(parentShip, tileToReplace.getColumn(), tileToReplace.getRow(), 2);
                tiles.set(1, newTile);
                break;
            case "Battleship":
                tileToReplace = tiles.get(2);
                newTile = new CaptainsQuartersTile(parentShip, tileToReplace.getColumn(), tileToReplace.getRow(), 2);
                tiles.set(2, newTile);
                break;
        }
    }

    /**
     * Mount an attack on the given coordinates
     *
     * @param
     * @return              returns the ship that was hit, null if the attack misses
     */
/*    public Ship attack(Point2D attackCoords) {
        int x = (int) attackCoords.getX();
        int y = (int) attackCoords.getY();

        // check that provided coords are on board, throw exception if not
        if (x < 1 || x > columns || y < 1 || y > rows)
            throw new InvalidAttackException("Attack coordinates off of board");

        // get tile to be attacked
        Tile attackedTile = tiles[x][y];
        // if already attacked, throw exception
        if (attackedTile.shot && !(attackedTile instanceof ShipTile)) throw new InvalidAttackException("Tile has already been attacked");

        //if we hit a captains quarters, we must subtract hp first, then see if CC was destroyed,
            //if yes, destroy entire ship
            //if no, miss and create a miss tile
        if(attackedTile instanceof CaptainsQuartersTile){

            ((CaptainsQuartersTile) attackedTile).damage(); //subtracts 1 from captain's quarter's hp

            if(((CaptainsQuartersTile) attackedTile).getHp() == 0){
                attackedTile.shot = true;
                attackedTile.revealed = true;
                attackedTile.getShip().destroy();
            }
            else{
                //we return null in case of a miss, and we treat armored captains quarters hits as a miss
                //additionally, we do not set the .shot flag for this "miss"
                return null;
            }
        }

        // otherwise set shot flag and return ship that contains tile
        attackedTile.shot = true;
        attackedTile.revealed = true;

        return attackedTile.getShip();
    }*/

    //replace a tile on the board with an input tile (newTile) and do proper re registering and gridpane updating
    private void swapTile(Tile newTile, GridPane gpane){
        Tile oldTile;

        int x = newTile.getColumn();
        int y = newTile.getRow();

        //get the old tile object from the board tile array
        oldTile = tiles[x][y];

        //re-register that spot with the renderer
        renderer.unregister(oldTile);
        renderer.register(newTile);

        gpane.getChildren().remove(oldTile);
        gpane.add(newTile, x, y);

        tiles[x][y] = newTile;
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