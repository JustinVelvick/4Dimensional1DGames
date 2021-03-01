package edu.colorado.fourdimensionalonedgames.game.attack;

import edu.colorado.fourdimensionalonedgames.game.ship.Ship;

public class AttackResult {
    public final AttackResultType type;
    public final Ship ship;

    public AttackResult(AttackResultType type, Ship ship) {
        this.type = type;
        this.ship = ship;
    }
}
