package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

//concrete class for weapon strategy pattern IAfterAttackBehavior
//this behavior does not decrement usages after use (Space Laser, Single Shot, etc.)
public class NoAfterAttackBehavior implements IAfterAttackBehavior {
    @Override
    public boolean doRemove() {
        return false;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void addCount(int n) {

    }
}
