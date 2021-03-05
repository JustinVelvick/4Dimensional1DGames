package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Tile extends Canvas implements IRenderable {

    public boolean shot;
    public boolean revealed;
    private int row;
    private int column;

    public Tile(int column, int row) {
        super(40,40);
        this.row = row;
        this.column = column;
        this.shot = false;
    }

    public Ship getShip() {
        return null;
    }

    public abstract Color getColor();

    @Override
    public void render() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFill(getColor());
        gc.fillRect(0, 0, 40, 40);
        gc.strokeLine(0,0,40,0);
        gc.strokeLine(40,0,40,40);
        gc.strokeLine(40,40,0,40);
        gc.strokeLine(0,40,0,0);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
