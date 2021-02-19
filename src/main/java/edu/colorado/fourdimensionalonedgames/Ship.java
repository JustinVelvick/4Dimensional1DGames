package edu.colorado.fourdimensionalonedgames;

import javafx.geometry.Point2D;

import java.util.List;

public abstract class Ship implements IRenderable{

    int size;
    public List<ShipTile> shipTiles;

    //default constructor
    public Ship(){

    }

    //class to show testing framework works, can delete whenever
    public int add(int a, int b){
        return a+b;
    }
}
