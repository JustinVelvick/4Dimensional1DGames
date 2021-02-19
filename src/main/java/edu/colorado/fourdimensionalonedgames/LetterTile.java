package edu.colorado.fourdimensionalonedgames;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LetterTile extends Tile implements IRenderable{

    private String letter;

    public LetterTile(int column, int row, String c) {
        super(column, row);
        this.letter = c;
    }

    @Override
    public void render(){
        GraphicsContext gc = this.getGraphicsContext2D();

        gc.setStroke(Color.BLACK);
        gc.strokeText(letter, 20,20);
    }
}
