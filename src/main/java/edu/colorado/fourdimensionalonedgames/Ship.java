package edu.colorado.fourdimensionalonedgames;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship implements IRenderable{

    int size;
    protected List<ShipTile> shipTiles;

    //default constructor
    public Ship(){
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
    public int damage(){
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
        for (ShipTile tile : shipTiles){
            if (!tile.shot) return false;
        }
        return true;
    }

    protected abstract Color getBaseColor();

    public Color getColor() {
        if (this.destroyed()) {
            return Color.RED;
        } else {
            return this.getBaseColor();
        }
    }
}
