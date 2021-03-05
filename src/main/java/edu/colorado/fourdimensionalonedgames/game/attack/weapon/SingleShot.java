package edu.colorado.fourdimensionalonedgames.game.attack.weapon;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import javafx.geometry.Point2D;

public class SingleShot extends Weapon{
    @Override
    public AttackResult useAt(Board board, Point2D position) {
        return null;
    }

    @Override
    public String getType() {
        return "Single Shot";
    }
}