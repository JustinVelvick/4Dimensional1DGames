package edu.colorado.fourdimensionalonedgames.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Grid extends Canvas {

    //fields
    private int columns;
    private int rows;
    private double tileSize;

    private double totalWidth;
    private double totalHeight;

    //pixel adjustments for strokeLine()
    private static final double SEPARATOR_SIZE   = 0.25D;
    private static final double SEPARATOR_OFFSET = (SEPARATOR_SIZE / 2.0D);



    private Color lineColor;
    //2D array of tile obejcts
    private Tile[][] tiles;


    //constructors
    public Grid(int width, int height, double tileSize, Color lineColor) {
        super(width,height); //call Canvas's constructor to make the window
        this.columns = (int) (width/tileSize);
        this.rows = (int) (height/tileSize);
        this.tileSize = tileSize;
        this.lineColor = lineColor;
        this.totalHeight = height;
        this.totalWidth = width;
    }

    //methods
    public void initialize(){
        this.drawLines(this.getGraphicsContext2D());

    }

    private void drawLines(GraphicsContext gc){
        gc.setStroke(this.lineColor);

        //draw vertical boundaries
        for(int i = 0; i < totalWidth+1; i+= this.getTileSize()){
            gc.strokeLine(i+SEPARATOR_OFFSET, 0, i+SEPARATOR_OFFSET, totalHeight);
        }

        //draw horizontal boundaries
        for(int j = 0; j < totalHeight+1; j+= this.getTileSize()){
            gc.strokeLine(0, j+SEPARATOR_OFFSET, totalWidth, j+SEPARATOR_OFFSET);
        }


    }


    /////////////----getters----////////////////

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public double getTotalWidth() {
        return totalWidth;
    }

    public double getTotalHeight() {
        return totalHeight;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public double getTileSize() {
        return tileSize;
    }

}
