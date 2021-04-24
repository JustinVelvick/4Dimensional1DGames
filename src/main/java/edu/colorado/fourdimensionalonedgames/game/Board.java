package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.OneShotAttack;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board implements Subject {

    private final int rows;
    private final int columns;
    private final int depth;

    public Tile[][][] tiles;
    public Render renderer;

    private final List<Observer> observers;

    public Board(int columns, int rows, int depth, Render renderer) {

        this.rows = rows;
        this.columns = columns;
        this.depth = depth;
        this.renderer = renderer;
        tiles = new Tile[columns + 1][rows + 1][depth];
        this.observers = new ArrayList<>();
    }

    //Method that only generates a board of sea tiles with accompanying letters and numbers in a grid shape
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
     * placeShip() in the board places a newShip down at the given origin pointed in the given direction
     *
     * @param direction     direction the ship points to relative to the origin
     * @param origin        the origin tile to place at
     * @param newShip       the ship to be placed
     * @return              boolean indicating ship placement success, false when off the board or if colliding with
     *                      another ship
     */
    public boolean placeShip(Orientation direction, Point3D origin, Ship newShip) {

        List<Point3D> generatedCoordinates = newShip.generateCoordinates(origin, direction);

        // placeable returns a list of coordinates when placement is valid, null when not valid
        if(placeable(generatedCoordinates)){

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

                //set the new position in tiles array to be the new tile object
                tiles[x][y][z] = currentTile;
            }
            updateObservers();
            return true;
        }
        //ship placement was not valid
        else{
            return false;
        }
    }

    //given a list of coordinates, makes sure all coordinates given are not off the board or taken up by another ship
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
        // check that provided coords are on board
        return !(x < 1 || x > columns || y < 1 || y > rows || z < 0 || z >= this.getDepth());
    }

    private Orientation findOrientation(Ship ship){
        List<ShipTile> tiles = ship.getShipTiles();
        ShipTile origin = tiles.get(0);
        ShipTile next = tiles.get(1);
        Orientation ret = null;
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

    /**
     * moveShip() in the board moves a single ship one tile in the given direction, attempts to handle replacing tiles
     * behind it as it moves (bugs related to this still exist as of 4/24)
     *
     * @param ship: Ship to move
     * @param direction: Direction to move the ship
     * @return: List of weapons that may have been picked up by ships via PowerUp tiles. Can be null.
     */
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
        //how far on the x and y plane the ship is moving
        int xChange = 0;
        int yChange = 0;

        //switch to determine x and y change based on direction given
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

        //origin AFTER moving for this ship
        newOrigin = new Point3D(x+xChange,y+yChange,z);
        //generate all other ship tile's coordinates based on that new origin and where the ship was originally facing
        coords = ship.generateCoordinates(newOrigin,findOrientation(ship));

        //for loop generates appropriate wake behind the tile that is moving (ie. if tile behind was
            //a SeaTile, then simply place a SeaTile in the spot you are leaving)
        for(ShipTile tile : ship.getShipTiles()){
            previousCoord = new Point3D(tile.getColumn()-xChange, tile.getRow()-yChange, tile.getDepth());
            //if shipTile was not up against the edge of the board
            if(isWithinBounds(previousCoord)){
                previous = tiles[tile.getColumn()-xChange][tile.getRow()-yChange][tile.getDepth()];

                if(previous instanceof SeaTile || previous instanceof MineTile || previous instanceof PowerUpTile){
                    tiles[tile.getColumn()][tile.getRow()][tile.getDepth()] = new SeaTile(tile.getColumn(), tile.getRow(), tile.getDepth());
                }
            }
            //when spot ship is leaving is on edge of board, this guarantees it needs to be a sea tile in the ships wake
            else{
                tiles[tile.getColumn()][tile.getRow()][tile.getDepth()] = new SeaTile(tile.getColumn(), tile.getRow(), tile.getDepth());
            }
        }

        //now ship needs to be moved to it's new location, List<Weapon> return is for if ship collects a powerUp
        List<Weapon> weaponsToAdd = new ArrayList<>();
        for(int i = 0; i < ship.getSize(); i++){
            currentTile = shipTiles.get(i);
            newCordinate = coords.get(i);
            currentTile.setColumn((int)newCordinate.getX());
            currentTile.setRow((int)newCordinate.getY());
            currentTile.setDepth((int)newCordinate.getZ());

            boolean shotFlag = false;
            boolean powerUpFlag = false;
            if (tiles[(int)newCordinate.getX()][(int)newCordinate.getY()][(int)newCordinate.getZ()] instanceof MineTile && !currentTile.shot) {
                shotFlag = true;
            }
            if (tiles[(int)newCordinate.getX()][(int)newCordinate.getY()][(int)newCordinate.getZ()] instanceof PowerUpTile) {
                powerUpFlag = true;
            }

            tiles[(int)newCordinate.getX()][(int)newCordinate.getY()][(int)newCordinate.getZ()] = currentTile;

            //if we hit a mine on move, simulate the tile being damaged
            if (shotFlag) {
                SmallWeapon mineWeapon = new SmallWeapon(new Attack(), "Mine");
                mineWeapon.useAt(this, new Point2D(newCordinate.getX(), newCordinate.getY()));
            }
            //if we hit a powerUp on move
            if (powerUpFlag) {
                ship.incrementPowerups();
                if (Math.random() < 0.5)
                    weaponsToAdd.add(new MediumWeapon(new Attack(), Game.CLUSTER_BOMB, new PopCountAfterAttackBehavior(1)));
                else{
                    weaponsToAdd.add(new XLargeWeapon(new OneShotAttack(), Game.NUKE, new PopCountAfterAttackBehavior(1)));
                }
            }
        }
        return weaponsToAdd;
    }

    //method to actually place mines down on the board
    //will NOT place a mine down on an already occupied tile
    public void placeMines(){
        Random random = new Random();
        int minesToPlace = 5;

        while(minesToPlace > 0){
            int i = random.nextInt(10) + 1;
            int j = random.nextInt(10) + 1;
            Tile oldTile = tiles[i][j][0];
            if (oldTile instanceof SeaTile){
                Tile mineTile = new MineTile(i,j,0);
                tiles[i][j][0] = mineTile;
                minesToPlace--;
            }
        }
        updateLocalObservers();
    }

    //method to actually place powerUps down on the board
    //will NOT place a powerUp on an already occupied tile
    public void placePowerUps(){
        Random random = new Random();
        int powerUpsToPlace = 2;

        while(powerUpsToPlace > 0){
            int i = random.nextInt(10) + 1;
            int j = random.nextInt(10) + 1;
            Tile oldTile = tiles[i][j][0];
            if (oldTile instanceof SeaTile){
                Tile powerUpTile = new PowerUpTile(i,j,0);
                tiles[i][j][0] = powerUpTile;
                powerUpsToPlace--;
            }
        }
        updateLocalObservers();
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

    //update all displays subscribed to this board (example usage would be a miss, both players need to see the miss
    @Override
    public void updateObservers() {
        for(Observer observer : observers){
            observer.update(tiles);
        }
        renderer.tick();
    }

    //only update your own display subscribed to this board
    //An example use of this would be for when the user moves their ship, you don't want the enemy to now be able
        //to suddenly see where your ship moved to, so just update "local" displays, ie, your own
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