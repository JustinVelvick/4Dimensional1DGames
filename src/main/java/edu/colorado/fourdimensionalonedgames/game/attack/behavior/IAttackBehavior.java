package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import javafx.geometry.Point2D;

public interface IAttackBehavior {
    public abstract AttackResult attackAt(Board board, Point2D position);
}
