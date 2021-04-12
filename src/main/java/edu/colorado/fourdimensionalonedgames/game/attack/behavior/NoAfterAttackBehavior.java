package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

public class NoAfterAttackBehavior implements IAfterAttackBehavior {
    @Override
    public boolean doRemove() {
        return false;
    }
}
