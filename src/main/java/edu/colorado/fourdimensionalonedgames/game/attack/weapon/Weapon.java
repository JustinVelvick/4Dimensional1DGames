package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAfterAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.NoAfterAttackBehavior;
import javafx.geometry.Point2D;

import java.util.List;

public abstract class Weapon {
    protected final IAttackBehavior behavior;
    protected final IAfterAttackBehavior afterBehavior;
    protected final String weaponName;

    public Weapon(IAttackBehavior behavior, String weaponName) {
        this(behavior, weaponName, new NoAfterAttackBehavior());
    }

    public Weapon(IAttackBehavior behavior, String weaponName, IAfterAttackBehavior afterBehavior) {
        this.behavior = behavior;
        this.weaponName = weaponName;
        this.afterBehavior = afterBehavior;
    }

    public abstract List<AttackResult> useAt(Board board, Point2D position);

    public String getType() {
        return weaponName;
    }

    public boolean doRemove() {
        return afterBehavior.doRemove();
    }

    public int getCount() {
        return afterBehavior.getCount();
    }

    public void addCount(int n) {
        afterBehavior.addCount(n);
    }
}
