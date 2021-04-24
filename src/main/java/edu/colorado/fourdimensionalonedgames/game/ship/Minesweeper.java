package edu.colorado.fourdimensionalonedgames.game.ship;

import javafx.scene.paint.Color;

//a Minesweeper is just 2 tiles in a line and Green in color with the CC belonging to the origin tile
public class Minesweeper extends Ship {


    public Minesweeper() {
        this.size = 2;
    }


    @Override
    public Color getBaseColor() {
        return Color.GREEN;
    }

    @Override
    public String getType() {
        return "Minesweeper";
    }
}