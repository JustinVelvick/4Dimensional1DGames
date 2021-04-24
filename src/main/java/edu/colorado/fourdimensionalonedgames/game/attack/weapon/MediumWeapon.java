package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAfterAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

//attacks in the shape of a cross with 5 total tiles
public class MediumWeapon extends Weapon{

    public MediumWeapon(IAttackBehavior behavior, String weaponName, IAfterAttackBehavior afterBehavior) {
        super(behavior, weaponName, afterBehavior);
    }

    @Override
    public List<AttackResult> useAt(Board board, Point2D position) {
        List<Point3D> positions = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 || y == 0) {
                    positions.add(new Point3D(position.getX() + x, position.getY() + y, 0));
                }
            }
        }
        Point3D pos3D = new Point3D(position.getX(), position.getY(), 0);
        return behavior.attackAt(board, positions, pos3D);
    }
}
