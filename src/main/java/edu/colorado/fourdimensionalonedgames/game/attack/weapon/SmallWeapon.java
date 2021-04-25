package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAfterAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.Arrays;
import java.util.List;

//most basic weapon, just hits one single tile on the surface (depth = 0)
public class SmallWeapon extends Weapon {

    public SmallWeapon(IAttackBehavior behavior, String weaponName) {
        super(behavior, weaponName);
    }

    public SmallWeapon(IAttackBehavior behavior, String weaponName, IAfterAttackBehavior afterBehavior) {
        super(behavior, weaponName, afterBehavior);
    }

    @Override
    public List<AttackResult> useAt(Board board, Point2D position) {
        Point3D pos3D = new Point3D(position.getX(), position.getY(), 0);
        return behavior.attackAt(board, Arrays.asList(pos3D), pos3D);
    }
}
