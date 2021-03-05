package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import javafx.geometry.Point2D;

import java.util.Arrays;
import java.util.List;

public class SmallWeapon extends Weapon{

    public SmallWeapon(IAttackBehavior behavior, String weaponName) {
        super(behavior, weaponName);
    }

    @Override
    public List<AttackResult> useAt(Board board, Point2D position) {
        return behavior.attackAt(board, Arrays.asList(position), position);
    }
}
