package edu.colorado.fourdimensionalonedgames.game;


import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.Fleet;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.tile.*;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int rows;
    private final int columns;
    private final int depth;

    public Tile[][][] tiles;
    public Render renderer;

    public Board(int columns, int rows, int depth, Render renderer) {

        this.rows = rows;
        this.columns = columns;
        this.depth = depth;
        this.renderer = renderer;
        tiles = new Tile[columns + 1][rows + 1][depth];
    }

    //Only called once upon game creation to make a sea of blank tile objects
    public void initializeBoard(GridPane gpane) {

        Tile tile;

        for (int j = 0; j <= columns; j++) {
            tile = new LetterTile(0, j, String.valueOf(j));
            this.renderer.register(tile);
            this.tiles[0][j][0] = tile;
            gpane.add(tile, 0, j);
        }


        for (int i = 1; i <= rows; i++) {
            tile = new LetterTile(i, 0, Character.toString((char) i + 64));
            this.renderer.register(tile);
            this.tiles[i][0][0] = tile;
            gpane.add(tile, i, 0);
        }

        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                tile = new SeaTile(i, j);
                this.renderer.register(tile);
                this.tiles[i][j][0] = tile;
                this.tiles[i][j][0].shot = false;
                gpane.add(tile, i, j);
            }
        }
    }

    //Overloaded method for tests (This method does not care about grid panes, but still generates tiles)
    public void initializeBoard(){
        Tile tile;

        for (int j = 0; j <= columns; j++) {
            tile = new LetterTile(0, j, String.valueOf(j));
            this.renderer.register(tile);
            this.tiles[0][j][0] = tile;
        }


        for (int i = 1; i <= rows; i++) {
            tile = new LetterTile(i, 0, Character.toString((char) i + 64));
            this.renderer.register(tile);
            this.tiles[i][0][0] = tile;
        }

        for (int z = 0; z < this.depth; z++) {
            for (int i = 1; i <= columns; i++) {
                for (int j = 1; j <= rows; j++) {
                    tile = new SeaTile(i, j, z);
                    this.renderer.register(tile);
                    this.tiles[i][j][z] = tile;
                    this.tiles[i][j][z].shot = false;
                }
            }
        }
    }


    /**
     * Place a new ship on the board given a placement orientation
     *
     * @param direction     direction the ship points in from the origin
     * @param origin        the origin of the placement
     * @param newShip       the ship to be placed
     * @return              boolean indicating ship placement success
     */
    public boolean placeShip(Orientation direction, Point2D origin, Ship newShip) {

        // placeable returns a list of coordinates when placement is valid, null when not valid
        List<Point2D> generatedCoordinates = placeable(origin, direction, newShip.size);

        if(generatedCoordinates != null){

            Tile oldTile;
            ShipTile currentTile;
           //update newShip's tiles to have newly generatedCoordinates
            List<ShipTile> tilesToAdd = newShip.getShipTiles();
            for(int i = 0; i < newShip.size; i++){
                int x = (int) generatedCoordinates.get(i).getX();
                int y = (int) generatedCoordinates.get(i).getY();
                currentTile = tilesToAdd.get(i);
                tilesToAdd.get(i).setColumn(x);
                tilesToAdd.get(i).setRow(y);

                //get the old tile object from the board tile array
                oldTile = tiles[x][y][0];
                tiles[x][y][0] = currentTile;

                //re-register that spot with the renderer
                renderer.unregister(oldTile);
                renderer.register(currentTile);
            }
            return true;
        }
        else{
            return false;
        }
    }

    //given a ship length, origin, and direction, placeable returns true if valid placement
    //FOR SURFACE PLACEMENT ONLY
    private List<Point2D> placeable(Point2D origin, Orientation direction, int shipSize) {
        double xCoordinate = origin.getX();
        double yCoordinate = origin.getY();

        List<Point2D> newCoordinates = new ArrayList<>();
        // get coordinate set of tiles ship would occupy if placed in given orientation
        switch (direction) {
            case up:
                for(double y = yCoordinate; y > (yCoordinate - shipSize); y--) {
                    newCoordinates.add(new Point2D(xCoordinate, y));
                }
                break;

            case down:
                for(double y = yCoordinate; y < (yCoordinate + shipSize); y++){
                    newCoordinates.add(new Point2D(xCoordinate, y));
                }
                break;

            case left:
                for(double x = xCoordinate; x > (xCoordinate - shipSize); x--){
                    newCoordinates.add(new Point2D(x, yCoordinate));
                }
                break;
            case right:
                for(double x = xCoordinate; x < (xCoordinate + shipSize); x++){
                    newCoordinates.add(new Point2D(x, yCoordinate));
                }
                break;
        }

        // check each coordinate to make sure not off board or occupied by other ship
        for (Point2D coordinate : newCoordinates) {
            if (coordinate.getX() < 1) return null;
            if (coordinate.getX() > columns) return null;
            if (coordinate.getY() < 1) return null;
            if (coordinate.getY() > rows) return null;

            Tile oldTile = tiles[(int) coordinate.getX()][(int) coordinate.getY()][0];
            if (oldTile instanceof ShipTile) return null;
        }
        return newCoordinates;
    }

    //replace a tile on the board with an input tile (newTile) and do proper re registering and gridpane updating
    private void swapTile(Tile newTile){
        Tile oldTile;

        int x = newTile.getColumn();
        int y = newTile.getRow();

        //get the old tile object from the board tile array
        oldTile = tiles[x][y][0];

        //re-register that spot with the renderer
        renderer.unregister(oldTile);
        renderer.register(newTile);

        tiles[x][y][0] = newTile;
    }

    public boolean isWithinBounds(Point3D coords) {
        int x = (int) coords.getX();
        int y = (int) coords.getY();
        int z = (int) coords.getZ();

        // check that provided coords are on board, throw exception if not
        return !(x < 1 || x > columns || y < 1 || y > rows || z < 0 || z >= this.getDepth());
    }

    public int getDepth() {
        return depth;
    }

}