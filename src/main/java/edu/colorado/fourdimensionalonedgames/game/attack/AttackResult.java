package edu.colorado.fourdimensionalonedgames.game.attack;

import edu.colorado.fourdimensionalonedgames.game.ship.*;

public class AttackResult {
    public final AttackResultType type;
    public final Ship ship;

    public AttackResult(AttackResultType type, Ship ship) {
        this.type = type;
        this.ship = ship;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AttackResult)) return false;
        AttackResult result = (AttackResult) o;
        return result.type == this.type && result.ship == this.ship;
    }
}
