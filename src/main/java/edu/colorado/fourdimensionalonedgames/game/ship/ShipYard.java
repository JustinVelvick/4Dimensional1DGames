package edu.colorado.fourdimensionalonedgames.game.ship;

//abstract factory for ship creation
public abstract class ShipYard {
    protected abstract Ship buildShip(String choice);

    public Ship createShip(String choice){
        Ship ship = buildShip(choice);
        return ship;
    }
}
