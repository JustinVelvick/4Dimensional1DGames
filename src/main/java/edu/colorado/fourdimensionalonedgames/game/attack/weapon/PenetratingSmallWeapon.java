package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.IAttackBehavior;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PenetratingSmallWeapon extends Weapon{

    public PenetratingSmallWeapon(IAttackBehavior behavior, String weaponName) {
        super(behavior, weaponName);
    }

    @Override
    public List<AttackResult> useAt(Board board, Point2D position) {
        Point3D pos3D = new Point3D(position.getX(), position.getY(), 0);
        List<Point3D> positions = new ArrayList<>();
        for (int z = 0; z < board.getDepth(); z++) {
            positions.add(new Point3D(position.getX(), position.getY(), z));
        }
        return behavior.attackAt(board, positions, pos3D);
    }
}
