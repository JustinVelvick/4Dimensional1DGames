package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.Arrays;
import java.util.List;

public class SmallWeapon extends Weapon{

    public SmallWeapon(IAttackBehavior behavior, String weaponName) {
        super(behavior, weaponName);
    }

    @Override
    public List<AttackResult> useAt(Board board, Point2D position) {
        Point3D pos3D = new Point3D(position.getX(), position.getY(), 0);
        return behavior.attackAt(board, Arrays.asList(pos3D), pos3D);
    }
}
