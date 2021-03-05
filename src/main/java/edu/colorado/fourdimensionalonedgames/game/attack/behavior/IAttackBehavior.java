package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import javafx.geometry.Point2D;

import java.util.List;

public interface IAttackBehavior {
    public abstract List<AttackResult> attackAt(Board board, List<Point2D> positions, Point2D origin);
}
