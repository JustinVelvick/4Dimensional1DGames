package edu.colorado.fourdimensionalonedgames.render.tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MineTile extends Tile {

    public MineTile(int column, int row, int depth) {
        super(column, row, depth);
    }

    @Override
    public Color getColor() {
        return Color.DARKSLATEGREY;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Image image = new Image("mine.png");
        gc.drawImage(image, getColumn() * 40, getRow() * 40, 40, 40);
    }
}
