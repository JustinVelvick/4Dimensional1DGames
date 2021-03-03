package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import javafx.geometry.Point2D;

public class Reveal implements IAttackBehavior {
    @Override
    public AttackResult attackAt(Board board, Point2D position) {
        // reveal tile on board at position
        return null;
    }
}
