package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

//concrete class for weapon strategy pattern IAttackBehavior
//this behavior kills everything, even objects with HP and other defences (CaptiansQuarter's Tiles)
public class OneShotAttack implements IAttackBehavior {
    @Override
    public List<AttackResult> attackAt(Board board, List<Point3D> positions, Point3D origin) {

        // check that provided coords are on board, throw exception if not
        if (!board.isWithinBounds(origin))
            throw new InvalidAttackException("Attack coordinates off of board");


        List<AttackResult> ret = new ArrayList<>();

        for (Point3D position : positions) {
            int x = (int) position.getX();
            int y = (int) position.getY();
            int depth = (int) position.getZ();

            // get tile to be attacked
            Tile attackedTile = board.tiles[x][y][depth];

            //if we hit a captains quarters, we must subtract hp first, then see if CC was destroyed,
            //if yes, destroy entire ship
            if (attackedTile instanceof CaptainsQuartersTile) {
                //drain HP to zero
                while (((CaptainsQuartersTile) attackedTile).getHp() > 0) {
                    ((CaptainsQuartersTile) attackedTile).damage();
                }

                if (((CaptainsQuartersTile) attackedTile).getHp() == 0) {
                    attackedTile.shot = true;
                    attackedTile.revealed = true;
                    attackedTile.getShip().destroy();
                }
            }
            // otherwise set shot flag and return ship that contains tile
            attackedTile.shot = true;

            Ship ship = attackedTile.getShip();
            if (ship == null) {
                ret.add(new AttackResult(AttackResultType.MISS, null));
            } else if (ship.destroyed()) {
                ret.add(new AttackResult(AttackResultType.SUNK, ship));
            } else {
                ret.add(new AttackResult(AttackResultType.HIT, ship));
            }
        }
        return ret;
    }
}
