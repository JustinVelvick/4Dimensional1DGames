package edu.colorado.fourdimensionalonedgames;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile extends Canvas implements IRenderable{

    public boolean shot;
    //private Canvas canvas;
    private int row;
    public int column;

    public Tile(int column, int row){
        super(40,40);
        this.row = row;
        this.column = column;
        this.shot = false;
    }

    public Ship getShip() { return null; }

    @Override
    public void render() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeLine(0,0,40,0);
        gc.strokeLine(40,0,40,40);
        gc.strokeLine(40,40,0,40);
        gc.strokeLine(0,40,0,0);
    }
}
