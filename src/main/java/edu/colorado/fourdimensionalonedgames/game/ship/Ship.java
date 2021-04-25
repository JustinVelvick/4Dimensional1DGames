package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * @size -A ship has a size (total number of tiles)
 * @shipTiles - List of tiles belonging to the ship
 */
public abstract class Ship {

    protected int size;
    protected List<ShipTile> shipTiles;
    private boolean destroyed;
    protected int powerUps = 0;

    //default constructor
    public Ship() {
        shipTiles = new ArrayList<>();
    }

    /**
     * returns "what-if we placed this ship here" coordinates FOR LINEAR SHIPS ONLY, OTHER SHAPES NEED TO OVERRIDE
     *
     * @param origin
     * @param direction
     */
    public List<Point3D> generateCoordinates(Point3D origin, Orientation direction) {
        double xCoordinate = origin.getX();
        double yCoordinate = origin.getY();
        double zCoordinate = origin.getZ();

        List<Point3D> newCoordinates = new ArrayList<>();
        // get coordinate set of tiles ship would occupy if placed in given orientation
        switch (direction) {
            case up:
                for (double y = yCoordinate; y > (yCoordinate - size); y--) {
                    newCoordinates.add(new Point3D(xCoordinate, y, zCoordinate));
                }
                break;

            case down:
                for (double y = yCoordinate; y < (yCoordinate + size); y++) {
                    newCoordinates.add(new Point3D(xCoordinate, y, zCoordinate));
                }
                break;

            case left:
                for (double x = xCoordinate; x > (xCoordinate - size); x--) {
                    newCoordinates.add(new Point3D(x, yCoordinate, zCoordinate));
                }
                break;
            case right:
                for (double x = xCoordinate; x < (xCoordinate + size); x++) {
                    newCoordinates.add(new Point3D(x, yCoordinate, zCoordinate));
                }
                break;
        }

        return newCoordinates;
    }

    public Point2D findAveragePostion(){
        Point2D average;

        double xAverage = 0;
        double yAverage = 0;

        for(ShipTile tile: this.getShipTiles()){
            xAverage+= tile.getColumn();
            yAverage+= tile.getRow();
        }

        xAverage = xAverage/this.size;
        yAverage = yAverage/this.size;

        average = new Point2D(xAverage, yAverage);
        return average;
    }



    //calculates total number of this ship's tiles that have been shot
    public int damage() {
        int damage = 0;
        for (ShipTile tile : shipTiles) {
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
        for (ShipTile tile : shipTiles) {
            if (!tile.shot) {
                allTilesShot = false;
                break;
            }
        }

        return (allTilesShot || this.destroyed);
    }

    //manually destroy this ship
    public void destroy() {
        this.destroyed = true;
        for (ShipTile tile : shipTiles) {
            tile.shot = true;
        }
    }

    //give the entire ship a new depth
    public void setShipTileDepth(int newDepth) {
        List<ShipTile> tilesToUpdate = this.getShipTiles();
        for (ShipTile tile : tilesToUpdate) {
            tile.setDepth(newDepth);
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

    public void incrementPowerups() {
        powerUps++;
    }

    public abstract String getType();

    public List<ShipTile> getShipTiles() {
        return shipTiles;
    }

    public void addTile(ShipTile tile) {
        shipTiles.add(tile);
    }

    public int getSize() {
        return size;
    }

    public int getPowerUps() {
        return powerUps;
    }
}
