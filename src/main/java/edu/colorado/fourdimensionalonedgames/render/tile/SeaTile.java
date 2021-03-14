package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SeaTile extends Tile implements IRenderable {

    //sea tiles should never need a depth constructor (should always be on the surface)
    public SeaTile(int column, int row) {
        super(column, row);
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
