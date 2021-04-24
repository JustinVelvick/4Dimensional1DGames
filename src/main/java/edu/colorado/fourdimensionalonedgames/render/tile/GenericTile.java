package edu.colorado.fourdimensionalonedgames.render.tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//GENERIC TILE AND LETTER TILE ARE FOR THE ENEMY DISPLAY ONLY
public class GenericTile{

    Color fillColor;
    private int column;
    private int row;

    public GenericTile(int column, int row) {
        fillColor = Color.CORNFLOWERBLUE;
        this.column = column;
        this.row = row;
    }

    public Color getColor() {
        return this.fillColor;
    }

    public void setColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void draw(GraphicsContext gc){

        int xOrigin = this.getColumn()*40;
        int yOrigin = this.getRow()*40;

        gc.setStroke(Color.BLACK);
        gc.setFill(getColor());
        gc.fillRect(xOrigin, yOrigin, 40, 40);
        gc.strokeLine(xOrigin,yOrigin,xOrigin+40,yOrigin);
        gc.strokeLine(xOrigin+40,yOrigin,xOrigin+40,yOrigin+40);
        gc.strokeLine(xOrigin+40,yOrigin+40,xOrigin,yOrigin+40);
        gc.strokeLine(xOrigin,yOrigin+40,xOrigin,yOrigin);
    }
}
