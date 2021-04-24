package edu.colorado.fourdimensionalonedgames.game.ship;

import javafx.scene.paint.Color;

//a Destroyer is 3 tiles in a line and blue in color with the CC belonging to the middle tile
public class Destroyer extends Ship {


    public Destroyer() {
        this.size = 3;
    }


    @Override
    public Color getBaseColor() {
        return Color.BLUE;
    }

    @Override
    public String getType() {
        return "Destroyer";
    }
}
