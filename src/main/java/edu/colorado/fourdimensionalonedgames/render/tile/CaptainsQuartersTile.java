package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CaptainsQuartersTile extends ShipTile{

    private int hp;
    public CaptainsQuartersTile(Ship parentShip, int column, int row, int hp) {
        super(parentShip, column, row);
        this.hp = hp;
    }

    @Override
    public void render() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(getColor());
        Rectangle rect = new Rectangle(40,40);
        gc.fillRect(0, 0, rect.getWidth(), rect.getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeLine(0,0,rect.getWidth(), rect.getHeight());
        gc.strokeLine(rect.getWidth(),0,0, rect.getHeight());
    }

    public int getHp() {
        return hp;
    }
}
