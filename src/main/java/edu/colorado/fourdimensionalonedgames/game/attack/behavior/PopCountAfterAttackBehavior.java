package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

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

    public void addCount(int n) {
        count += n;
    }
}
