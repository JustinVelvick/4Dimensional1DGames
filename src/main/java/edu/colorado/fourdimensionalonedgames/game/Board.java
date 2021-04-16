package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.PopCountAfterAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.MediumWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.XLargeWeapon;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.gui.Display;
import edu.colorado.fourdimensionalonedgames.render.gui.Observer;
import edu.colorado.fourdimensionalonedgames.render.gui.Subject;
import edu.colorado.fourdimensionalonedgames.render.tile.*;
import javafx.geometry.Point2D;
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
            for(int i = 0; i < newShip.getSize(); i++){
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
                //renderer.unregister(oldTile);
                //renderer.register(currentTile);
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


    private Orientation findOrientation(Ship ship){
        List<ShipTile> tiles = ship.getShipTiles();
        ShipTile origin = tiles.get(0);
        ShipTile next = tiles.get(1);
        Orientation ret = Orientation.up;
        if(origin.getRow() > next.getRow()){
            ret = Orientation.up;
        }
        if(origin.getRow() < next.getRow()){
            ret = Orientation.down;
        }
        if(origin.getColumn() < next.getColumn()){
            ret = Orientation.right;
        }
        if(origin.getColumn() > next.getColumn()){
            ret = Orientation.left;
        }
        return ret;
    }

    public List<Weapon> moveShip(Ship ship, Orientation direction){
        List<ShipTile> shipTiles = ship.getShipTiles();
        int x = shipTiles.get(0).getColumn();
        int y = shipTiles.get(0).getRow();
        int z = shipTiles.get(0).getDepth();
        Point3D newOrigin = new Point3D(x,y,z);
        List<Point3D> coords = new ArrayList<>();
        ShipTile currentTile;
        Point3D newCordinate;
        Point3D previousCoord;
        Tile previous;


        int xChange = 0;
        int yChange = 0;

        switch (direction){
            case up:
                xChange = 0;
                yChange = -1;
                break;

            case down:
                xChange = 0;
                yChange = 1;
                break;

            case left:
                xChange = -1;
                yChange = 0;
                break;

            case right:
                xChange = 1;
                yChange = 0;
                break;
        }

        newOrigin = new Point3D(x+xChange,y+yChange,z);
        coords = ship.generateCoordinates(newOrigin,findOrientation(ship));
        for(ShipTile tile : ship.getShipTiles()){
            previousCoord = new Point3D(tile.getColumn()-xChange, tile.getRow()-yChange, tile.getDepth());
            if(isWithinBounds(previousCoord)){
                previous = tiles[tile.getColumn()-xChange][tile.getRow()-yChange][tile.getDepth()];
                if(previous instanceof SeaTile){
                    tiles[tile.getColumn()][tile.getRow()][tile.getDepth()] = new SeaTile(tile.getColumn(), tile.getRow(), tile.getDepth());
                }
            }
            else{
                tiles[tile.getColumn()][tile.getRow()][tile.getDepth()] = new SeaTile(tile.getColumn(), tile.getRow(), tile.getDepth());
            }
        }

        List<Weapon> weaponsToAdd = new ArrayList<>();
        for(int i = 0; i < ship.getSize(); i++){
            currentTile = shipTiles.get(i);
            newCordinate = coords.get(i);
            currentTile.setColumn((int)newCordinate.getX());
            currentTile.setRow((int)newCordinate.getY());
            currentTile.setDepth((int)newCordinate.getZ());

            boolean shotFlag = false;
            boolean powerUpFlag = false;
            if (tiles[(int)newCordinate.getX()][(int)newCordinate.getY()][(int)newCordinate.getZ()] instanceof MineTile) {
                shotFlag = true;
            }
            if (tiles[(int)newCordinate.getX()][(int)newCordinate.getY()][(int)newCordinate.getZ()] instanceof PowerUpTile) {
                powerUpFlag = true;
            }

            tiles[(int)newCordinate.getX()][(int)newCordinate.getY()][(int)newCordinate.getZ()] = currentTile;

            //simulate a tile being damaged by the mine
            if (shotFlag) {
                SmallWeapon mineWeapon = new SmallWeapon(new Attack(), "Mine");
                mineWeapon.useAt(this, new Point2D(newCordinate.getX(), newCordinate.getY()));
            }
            if (powerUpFlag) {
                ship.incrementPowerups();
                if (Math.random() < 0.5)
                    weaponsToAdd.add(new MediumWeapon(new Attack(), Game.CLUSTER_BOMB, new PopCountAfterAttackBehavior(1)));
                else
                    weaponsToAdd.add(new XLargeWeapon(new Attack(), Game.NUKE, new PopCountAfterAttackBehavior(1)));
            }
        }
        return weaponsToAdd;
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

    //all displays subscribed to this board
    @Override
    public void updateObservers() {
        for(Observer observer : observers){
            observer.update(tiles);
        }
        renderer.tick();
    }

    //only your own display subscribed to this board
    @Override
    public void updateLocalObservers() {
        for(Observer observer : observers){
            if(observer instanceof Display){
                observer.update(tiles);
            }
        }
        renderer.tick();
    }
}