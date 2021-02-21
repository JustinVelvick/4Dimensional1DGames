package edu.colorado.fourdimensionalonedgames;

import javafx.scene.paint.Color;

public class Minesweeper extends Ship{


    public Minesweeper(){
        this.size = 2;
    }

    @Override
    public void render() {

    }

    @Override
    public Color getBaseColor() {
        return Color.GREEN;
    }
}