package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.Render;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;

public class Player {
    public Board board;

    public Player (Board board) {

    }

    public AttackResult attack(Board opponent, Point2D attackCoords) {
        return null;
    }
}
