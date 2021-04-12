package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

public interface IAfterAttackBehavior {
    public boolean doRemove();

    public int getCount(); // returns -1 if infinity
}
