package edu.colorado.fourdimensionalonedgames.render.tile;

import javafx.scene.paint.Color;

public class SeaTile extends Tile{

    public SeaTile(int column, int row) {
        super(column, row);
    }

    public SeaTile(int column, int row, int depth) {
        super(column, row, depth);
    }


    public Color getColor() {

        //if shot and a sea tile, then this is a miss, return miss color
        if(this.shot){
            return Color.GREENYELLOW;
        }
        else{
            return Color.CORNFLOWERBLUE;
        }

    }
}
