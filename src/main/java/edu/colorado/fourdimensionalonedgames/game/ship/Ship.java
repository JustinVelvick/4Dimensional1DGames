package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import javafx.geometry.Point3D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship implements IRenderable {

    public int size;
    protected List<ShipTile> shipTiles;
    private boolean destroyed;

    //default constructor
    public Ship() {
        shipTiles = new ArrayList<>();
    }

    public void addTile(ShipTile tile) {
        shipTiles.add(tile);
    }

    /**
     * Calculate the damage done to the ship
     *
     * @return the number of damaged ship tiles in the ship
     */
    //generating "what-if we placed this ship here" coordinates FOR LINEAR SHIPS ONLY, OTHER SHAPES NEED TO OVERRIDE
    public List<Point3D> generateCoordinates(Point3D origin, Orientation direction){
        double xCoordinate = origin.getX();
        double yCoordinate = origin.getY();
        double zCoordinate = origin.getZ();

        List<Point3D> newCoordinates = new ArrayList<>();
        // get coordinate set of tiles ship would occupy if placed in given orientation
        switch (direction) {
            case up:
                for(double y = yCoordinate; y > (yCoordinate - size); y--) {
                    newCoordinates.add(new Point3D(xCoordinate, y, zCoordinate));
                }
                break;

            case down:
                for(double y = yCoordinate; y < (yCoordinate + size); y++){
                    newCoordinates.add(new Point3D(xCoordinate, y, zCoordinate));
                }
                break;

            case left:
                for(double x = xCoordinate; x > (xCoordinate - size); x--){
                    newCoordinates.add(new Point3D(x, yCoordinate, zCoordinate));
                }
                break;
            case right:
                for(double x = xCoordinate; x < (xCoordinate + size); x++){
                    newCoordinates.add(new Point3D(x, yCoordinate, zCoordinate));
                }
                break;
        }

        return newCoordinates;
    }


    public int damage() {
        int damage = 0;
        for (ShipTile tile : shipTiles){
            if (tile.shot) damage++;
        }
        return damage;
    }

    /**
     * Determine if the ship has been destroyed (have all tiles been shot)
     *
     * @return boolean indicating if ship is destroyed
     */
    public boolean destroyed() {
        boolean allTilesShot = true;
        for (ShipTile tile : shipTiles){
            if (!tile.shot){
                allTilesShot = false;
                break;
            }
        }

        return (allTilesShot || this.destroyed);
    }

    public void destroy(){
        this.destroyed = true;
        for (ShipTile tile : shipTiles){
            tile.shot = true;
        }
    }

    protected abstract Color getBaseColor();

    public Color getColor() {
        if (this.destroyed()) {
            return Color.RED;
        } else {
            return this.getBaseColor();
        }
    }

    public abstract String getType();

    public List<ShipTile> getShipTiles() {
        return shipTiles;
    }
}
