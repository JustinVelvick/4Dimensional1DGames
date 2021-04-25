package edu.colorado.fourdimensionalonedgames.game.ship;

/**
 *  ShipYard is an abstract factory for ship creation, some concrete implementation examples include DefaultShipYard
 *  and SubmergableShipYard
 */
public abstract class ShipYard {
    protected abstract Ship buildShip(String choice);

    public Ship createShip(String choice) {
        Ship ship = buildShip(choice);
        return ship;
    }
}
