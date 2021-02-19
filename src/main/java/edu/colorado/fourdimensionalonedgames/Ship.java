package edu.colorado.fourdimensionalonedgames;

import javafx.geometry.Point2D;

import java.util.List;

public abstract class Ship implements IRenderable{

    int size;
    protected List<ShipTile> shipTiles;

    //default constructor
    public Ship(){

    }

    public void addTile(ShipTile tile) {
        shipTiles.add(tile);
    }
}
