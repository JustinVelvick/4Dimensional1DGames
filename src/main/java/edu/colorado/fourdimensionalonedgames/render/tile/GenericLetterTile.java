package edu.colorado.fourdimensionalonedgames.render.tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GenericLetterTile extends GenericTile{

    private String letter;

    public GenericLetterTile(int column, int row, String c) {
        super(column, row);
        this.letter = c;
    }

    @Override
    public void draw(GraphicsContext gc){

        int xOrigin = this.getColumn()*40;
        int yOrigin = this.getRow()*40;

        gc.setStroke(Color.BLACK);
        gc.strokeText(letter, xOrigin+20,yOrigin+20);
    }
}
