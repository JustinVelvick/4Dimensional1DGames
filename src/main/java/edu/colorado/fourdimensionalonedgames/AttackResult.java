package edu.colorado.fourdimensionalonedgames;

public class AttackResult {
    public final AttackResultType type;
    public final Ship ship;

    public AttackResult(AttackResultType type, Ship ship) {
        this.type = type;
        this.ship = ship;
    }
}
