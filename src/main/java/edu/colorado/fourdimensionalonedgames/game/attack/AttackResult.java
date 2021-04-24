package edu.colorado.fourdimensionalonedgames.game.attack;

import edu.colorado.fourdimensionalonedgames.game.ship.*;

//for each tile effected by an attack, one of these objects will be generated to represent what happened and to what
public class AttackResult {
    private final AttackResultType type;
    private final Ship ship;

    public AttackResult(AttackResultType type, Ship ship) {
        this.type = type;
        this.ship = ship;
    }

    public Ship getShip(){ return ship; }
    public AttackResultType getType(){ return type; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AttackResult)) return false;
        AttackResult result = (AttackResult) o;
        return result.type == this.type && result.ship == this.ship;
    }
}
