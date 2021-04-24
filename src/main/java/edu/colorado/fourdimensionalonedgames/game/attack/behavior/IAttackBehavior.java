package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.List;

//strategy pattern for weapons
public interface IAttackBehavior {
    List<AttackResult> attackAt(Board board, List<Point3D> positions, Point3D origin);
}
