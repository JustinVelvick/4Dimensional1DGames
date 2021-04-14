package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class PowerUpTile extends Tile{

    public PowerUpTile(int column, int row, int depth) { super(column, row, depth); }


    @Override
    public Color getColor() {
        return Color.GOLD;
    }

    @Override
    public void draw(GraphicsContext gc){
        Image image = new Image("powerUp.png");
        gc.drawImage(image,getColumn()*40,getRow()*40,40,40);
    }
}