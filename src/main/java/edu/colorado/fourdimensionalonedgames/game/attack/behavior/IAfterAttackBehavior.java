package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

//strategy pattern for weapons
public interface IAfterAttackBehavior {
    boolean doRemove();

    int getCount(); // returns -1 if infinity

    void addCount(int n);
}
