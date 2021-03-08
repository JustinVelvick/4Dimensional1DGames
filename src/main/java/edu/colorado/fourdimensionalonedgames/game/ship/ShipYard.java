package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;

public abstract class ShipYard {
    protected abstract Ship buildShip(String choice);

    public Ship createShip(String choice){
        Ship ship = buildShip(choice);
        return ship;
    }
}
