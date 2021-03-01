package edu.colorado.fourdimensionalonedgames.game.ship;

import javafx.scene.paint.Color;

public class Battleship extends Ship {


    public Battleship() {
        this.size = 4;
    }

    @Override
    public void render() {

    }

    @Override
    public Color getBaseColor() {
        return Color.INDIGO;
    }

    @Override
    public String getType() {
        return "Battleship";
    }
}