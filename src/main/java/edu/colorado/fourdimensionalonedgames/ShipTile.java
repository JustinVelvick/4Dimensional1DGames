package edu.colorado.fourdimensionalonedgames;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShipTile extends Tile implements IRenderable{
    public ShipTile(int column, int row) {

        super(column, row);
    }

    @Override
    public void render(){
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        Rectangle rect = new Rectangle(40,40);
        gc.fillRect(0, 0, rect.getWidth(), rect.getHeight());
    }
}
