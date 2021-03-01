package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ShipTile extends Tile implements IRenderable {

    private Ship parentShip;

    public ShipTile(Ship parentShip, int column, int row) {
        super(column, row);
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

    @Override
    public void render() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(getColor());
        Rectangle rect = new Rectangle(40,40);
        gc.fillRect(0, 0, rect.getWidth(), rect.getHeight());
    }
}
