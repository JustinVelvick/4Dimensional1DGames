package edu.colorado.fourdimensionalonedgames.render.tile;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import javafx.scene.paint.Color;

public class PowerUpTile extends Tile implements IRenderable {

    public PowerUpTile(int column, int row, int depth) { super(column, row, depth); }


    @Override
    public Color getColor() {
        return Color.GOLD;
    }

}