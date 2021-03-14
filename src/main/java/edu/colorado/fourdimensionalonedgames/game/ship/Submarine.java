package edu.colorado.fourdimensionalonedgames.game.ship;

import javafx.scene.paint.Color;

public class Submarine extends Ship {


    public Submarine() {
        this.size = 5;
    }

    @Override
    public void render() {

    }

    @Override
    public Color getBaseColor() {
        return Color.YELLOW;
    }

    @Override
    public String getType() {
        return "Submarine";
    }
}