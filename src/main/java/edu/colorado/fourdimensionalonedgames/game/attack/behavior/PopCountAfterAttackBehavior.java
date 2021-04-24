package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

//concrete class for weapon strategy pattern IAfterAttackBehavior
public class PopCountAfterAttackBehavior implements IAfterAttackBehavior {
    private int count;

    public PopCountAfterAttackBehavior(int count) {
        this.count = count;
    }

    @Override
    public boolean doRemove() {
        count--;
        return count <= 0;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void addCount(int n) {
        count += n;
    }
}
