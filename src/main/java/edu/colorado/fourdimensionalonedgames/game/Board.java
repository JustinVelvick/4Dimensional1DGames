package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.gui.Observer;
import edu.colorado.fourdimensionalonedgames.render.gui.Subject;
import edu.colorado.fourdimensionalonedgames.render.tile.*;
import javafx.geometry.Point3D;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class Board implements Subject {

    private final int rows;
    private final int columns;
    private final int depth;

    public Tile[][][] tiles;
    public Render renderer;

    private List<Observer> observers;

    public Board(int columns, int rows, int depth, Render renderer) {

        this.rows = rows;
        this.columns = columns;
        this.depth = depth;
        this.renderer = renderer;
        tiles = new Tile[columns + 1][rows + 1][depth];
        this.observers = new ArrayList<>();
    }

    //Method that only generates a board of sea tiles with accompying letters and numbers in a grid
    public void initializeBoard(){
        Tile tile;

        for (int j = 0; j <= columns; j++) {
            tile = new LetterTile(0, j, String.valueOf(j));
            this.tiles[0][j][0] = tile;
        }

        for (int i = 1; i <= rows; i++) {
            tile = new LetterTile(i, 0, Character.toString((char) i + 64));
            this.tiles[i][0][0] = tile;
        }

        for (int z = 0; z < this.depth; z++) {
            for (int i = 1; i <= columns; i++) {
                for (int j = 1; j <= rows; j++) {
                    tile = new SeaTile(i, j, z);
                    this.tiles[i][j][z] = tile;
                    this.tiles[i][j][z].shot = false;
                }
            }
        }
        updateObservers();
    }


    /**
     * Place a new ship on the board given a placement orientation
     *
     * @param direction     direction the ship points in from the origin
     * @param origin        the origin of the placement
     * @param newShip       the ship to be placed
     * @return              boolean indicating ship placement success
     */
    public boolean placeShip(Orientation direction, Point3D origin, Ship newShip) {

        List<Point3D> generatedCoordinates = newShip.generateCoordinates(origin, direction);

        // placeable returns a list of coordinates when placement is valid, null when not valid
        if(placeable(generatedCoordinates)){

            Tile oldTile;
            ShipTile currentTile;
           //update newShip's tiles to have newly generatedCoordinates
            List<ShipTile> tilesToAdd = newShip.getShipTiles();
            for(int i = 0; i < newShip.size; i++){
                int x = (int) generatedCoordinates.get(i).getX();
                int y = (int) generatedCoordinates.get(i).getY();
                int z = (int) generatedCoordinates.get(i).getZ();
                currentTile = tilesToAdd.get(i);
                tilesToAdd.get(i).setColumn(x);
                tilesToAdd.get(i).setRow(y);
                tilesToAdd.get(i).setDepth(z);

                //get the old tile object from the board tile array
                oldTile = tiles[x][y][z];
                tiles[x][y][z] = currentTile;

                //re-register that spot with the renderer
                renderer.unregister(oldTile);
                renderer.register(currentTile);
            }
            updateObservers();
            return true;
        }
        else{
            return false;
        }
    }

    //given a ship length, origin, and direction, placeable returns true if valid placement
    private boolean placeable(List<Point3D> newCoordinates) {

        // check each coordinate to make sure not off board or occupied by other ship
        for (Point3D coordinate : newCoordinates) {
            if (!isWithinBounds(coordinate)) return false;

            Tile oldTile = tiles[(int) coordinate.getX()][(int) coordinate.getY()][(int) coordinate.getZ()];
            if (oldTile instanceof ShipTile) return false;
        }
        return true;
    }

    public boolean isWithinBounds(Point3D coords) {
        int x = (int) coords.getX();
        int y = (int) coords.getY();
        int z = (int) coords.getZ();

        // check that provided coords are on board, throw exception if not
        return !(x < 1 || x > columns || y < 1 || y > rows || z < 0 || z >= this.getDepth());
    }

    public void moveShip(Ship ship, Orientation direction){
        switch (direction){
            case up:
                for (ShipTile tile : ship.getShipTiles()){
                    int x = tile.getColumn();
                    int old_y = tile.getRow();
                    int y = old_y-1;
                    tile.setRow(y);
                    int z = tile.getDepth();

                    tiles[x][y][z] = tile;
                    tiles[x][old_y][z] = new SeaTile(x, old_y, z);
                }
                break;
            case down:
                for (ShipTile tile : ship.getShipTiles()){
                    int x = tile.getColumn();
                    int old_y = tile.getRow();
                    int y = old_y+1;
                    tile.setRow(y);
                    int z = tile.getDepth();

                    tiles[x][y][z] = tile;
                    tiles[x][old_y][z] = new SeaTile(x, old_y, z);
                }
                break;
            case left:
                for (ShipTile tile : ship.getShipTiles()){
                    int old_x = tile.getColumn();
                    int x = old_x - 1;
                    tile.setColumn(x);
                    int y = tile.getRow();
                    int z = tile.getDepth();

                    tiles[x][y][z] = tile;
                    tiles[old_x][y][z] = new SeaTile(old_x, y, z);
                }
                break;
            case right:
                for (ShipTile tile : ship.getShipTiles()){
                    int old_x = tile.getColumn();
                    int x = old_x + 1;
                    tile.setColumn(x);
                    int y = tile.getRow();
                    int z = tile.getDepth();

                    tiles[x][y][z] = tile;
                    tiles[old_x][y][z] = new SeaTile(old_x, y, z);
                }
                break;
        }
    }

    public int getDepth() {
        return depth;
    }


    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void updateObservers() {
        for(Observer observer : observers){
            observer.update(tiles);
        }
    }
}