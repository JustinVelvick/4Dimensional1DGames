package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MineTile extends Tile implements IRenderable {

    public MineTile(int column, int row, int depth) { super(column, row, depth); }

    @Override
    public Color getColor() {
        return Color.DARKSLATEGREY;
    }

    @Override
    public void render(){
        GraphicsContext gc = this.getGraphicsContext2D();
        Image image = new Image("mine.png");
        gc.drawImage(image,0,0,40,40);
    }
}
