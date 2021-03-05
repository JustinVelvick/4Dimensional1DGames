package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Attack implements IAttackBehavior {
    @Override
    public List<AttackResult> attackAt(Board board, List<Point2D> positions, Point2D origin) {
        // attack tile on board at position
        //int x = (int) origin.getX();
        //int y = (int) origin.getY();

        // check that provided coords are on board, throw exception if not
        if (!board.isWithinBounds(origin))
            throw new InvalidAttackException("Attack coordinates off of board");

        //Tile originTile = board.tiles[x][y];
        // if already attacked, throw exception
        //if (originTile.shot) throw new InvalidAttackException("tile has already been attacked");

        List<AttackResult> ret = new ArrayList<>();

        for (Point2D position : positions) {
            int x2 = (int) position.getX();
            int y2 = (int) position.getY();

            // get tile to be attacked
            Tile attackedTile = board.tiles[x2][y2];

            // otherwise set shot flag and return ship that contains tile
            attackedTile.shot = true;

            Ship ship = attackedTile.getShip();
            if (ship == null) {
                ret.add(new AttackResult(AttackResultType.MISS, null));
            }
            else if (board.gameOver()){
                ret.add(new AttackResult(AttackResultType.SURRENDER, ship));
            }
            else if (ship.destroyed()){
                ret.add(new AttackResult(AttackResultType.SUNK, ship));
            }
            else{
                ret.add(new AttackResult(AttackResultType.HIT, ship));
            }

        }

        return ret;
    }
}
