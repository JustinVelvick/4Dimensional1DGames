package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAfterAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

//for weapons that hit the entire board
public class XLargeWeapon extends Weapon{

    public XLargeWeapon(IAttackBehavior behavior, String weaponName) {
        super(behavior, weaponName);
    }

    public XLargeWeapon(IAttackBehavior behavior, String weaponName, IAfterAttackBehavior afterBehavior) {
        super(behavior, weaponName, afterBehavior);
    }

    @Override
    public List<AttackResult> useAt(Board board, Point2D position) {
        List<Point3D> positions = new ArrayList<>();
        for (int x = 1; x <= 10; x++) {
            for (int y = 1; y <= 10; y++) {
                for (int z = 0; z < board.getDepth(); z++) {
                    positions.add(new Point3D(x, y, z));
                }
            }
        }
        Point3D pos3D = new Point3D(position.getX(), position.getY(), 0);
        return behavior.attackAt(board, positions, pos3D);
    }
}
