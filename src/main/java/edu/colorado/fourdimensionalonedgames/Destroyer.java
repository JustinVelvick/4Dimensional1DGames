package edu.colorado.fourdimensionalonedgames;

import javafx.scene.paint.Color;

public class Destroyer extends Ship{


    public Destroyer(){
        this.size = 3;
    }

    @Override
    public void render() {

    }

    @Override
    public Color getBaseColor() {
        return Color.BLUE;
    }
}
