package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import javafx.geometry.Point2D;

import java.util.List;

public abstract class Weapon {
    protected final IAttackBehavior behavior;
    protected final String weaponName;

    public Weapon(IAttackBehavior behavior, String weaponName) {
        this.behavior = behavior;
        this.weaponName = weaponName;
    }

    public abstract List<AttackResult> useAt(Board board, Point2D position);

    public String getType() {
        return weaponName;
    }
}
