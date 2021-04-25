package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.gui.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Tile implements IDrawable {

    public boolean shot;
    public boolean revealed;
    private int row;
    private int column;
    private int depth;

    //default constructor for surface tiles
    public Tile(int column, int row) {
        this.row = row;
        this.column = column;
        this.depth = 0;
        this.shot = false;
    }

    //overloaded constructor for submerged tiles
    public Tile(int column, int row, int depth) {
        this.row = row;
        this.column = column;
        this.depth = depth;
        this.shot = false;
    }

    public Ship getShip() {
        return null;
    }

    public void draw(GraphicsContext gc) {
        int xOrigin = this.getColumn() * 40;
        int yOrigin = this.getRow() * 40;

        gc.setStroke(Color.BLACK);
        gc.setFill(getColor());
        gc.fillRect(xOrigin, yOrigin, 40, 40);
        gc.strokeLine(xOrigin, yOrigin, xOrigin + 40, yOrigin);
        gc.strokeLine(xOrigin + 40, yOrigin, xOrigin + 40, yOrigin + 40);
        gc.strokeLine(xOrigin + 40, yOrigin + 40, xOrigin, yOrigin + 40);
        gc.strokeLine(xOrigin, yOrigin + 40, xOrigin, yOrigin);
    }

    public abstract Color getColor();

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getDepth() {
        return depth;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
