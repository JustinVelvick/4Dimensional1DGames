package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAfterAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//attacks in the shape of a filled in diamond
public class LargeWeapon extends Weapon{

    public LargeWeapon(IAttackBehavior behavior, String weaponName, IAfterAttackBehavior afterBehavior) {
        super(behavior, weaponName, afterBehavior);
    }

    @Override
    public List<AttackResult> useAt(Board board, Point2D position) {
        List<Point3D> positions = new ArrayList<>();
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                // math to check if it's in the diamond
                if (!((Math.abs(x) == 1 && Math.abs(y) == 2) || (Math.abs(x) == 2 && Math.abs(y) == 1)) && (Math.abs(x) != 2 || Math.abs(y) != 2)) {
                    for (int z = 0; z < board.getDepth(); z++) {
                        positions.add(new Point3D(position.getX() + x, position.getY() + y, z));
                    }
                }
            }
        }
        Point3D pos3D = new Point3D(position.getX(), position.getY(), 0);
        return behavior.attackAt(board, positions, pos3D);
    }
}