package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile extends Canvas implements IRenderable {

    public boolean shot;
    //private Canvas canvas;
    private int row;
    public int column;

    public Tile(int column, int row) {
        super(40,40);
        this.row = row;
        this.column = column;
        this.shot = false;
    }

    public Ship getShip() {
        return null;
    }

    public Color getColor() {
        return Color.CORNFLOWERBLUE;
    }

    @Override
    public void render() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(0, 0, 40, 40);
        gc.strokeLine(0,0,40,0);
        gc.strokeLine(40,0,40,40);
        gc.strokeLine(40,40,0,40);
        gc.strokeLine(0,40,0,0);
    }
}
