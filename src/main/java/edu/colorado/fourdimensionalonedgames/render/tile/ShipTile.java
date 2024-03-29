package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ShipTile extends Tile {

    protected Ship parentShip;
    //GUI needs this bool to only record a hit tile once, and never move that red tile again on EnemyDisplays
    boolean beenRecorded = false;

    public ShipTile(Ship parentShip, int column, int row) {
        super(column, row);
        this.parentShip = parentShip;
    }

    //overloaded tile constructor for depth
    public ShipTile(Ship parentShip, int column, int row, int depth) {
        super(column, row, depth);
        this.parentShip = parentShip;
    }

    @Override
    public Ship getShip() {
        return parentShip;
    }


    public Color getColor() {
        if (shot && !parentShip.destroyed()) {
            return Color.HOTPINK;
        } else {
            return parentShip.getColor();
        }
    }

    public boolean hasBeenRecorded() {
        return beenRecorded;
    }

    public void setBeenRecorded(boolean beenRecorded) {
        this.beenRecorded = beenRecorded;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor());
        Rectangle rect = new Rectangle(40, 40);
        gc.fillRect(0, 0, rect.getWidth(), rect.getHeight());
    }
}
