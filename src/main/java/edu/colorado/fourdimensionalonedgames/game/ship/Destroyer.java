package edu.colorado.fourdimensionalonedgames.game.ship;

import javafx.scene.paint.Color;

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
