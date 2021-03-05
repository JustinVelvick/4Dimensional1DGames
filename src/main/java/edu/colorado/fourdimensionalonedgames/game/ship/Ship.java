package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
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
        Boolean allTilesShot = true;
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
