package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;

import java.util.List;

//concrete factory for submergable ships
public class SubmergableShipYard extends ShipYard {

    @Override
    public Ship createShip(String choice) {
        Ship newShip = buildShip(choice);
        ///
        //do other things with freshly built ship (maybe in future requirements)
        ///
        generateShipTiles(newShip);
        generateCaptainsQuarters(newShip);

        return newShip;
    }

    protected Ship buildShip(String choice) {
        switch (choice) {
            case "Submarine":
                return new Submarine();
            default:
                return null;
        }
    }

    //generates default tiles whose coordinates will be updated later (these are all at col 0, row 0)
    private void generateShipTiles(Ship newShip) {
        for (int i = 0; i < newShip.size; i++) {
            newShip.addTile(new ShipTile(newShip, 0, 0, 0));
        }
    }

    private void generateCaptainsQuarters(Ship newShip) {

        ShipTile newTile;
        List<ShipTile> tiles = newShip.getShipTiles();
        Tile tileToReplace;
        switch (newShip.getType()) {
            case "Submarine":
                tileToReplace = tiles.get(3);
                newTile = new CaptainsQuartersTile(newShip, tileToReplace.getColumn(), tileToReplace.getRow(), tileToReplace.getDepth(), 2);
                tiles.set(3, newTile);
                break;
            default:
                break;
        }
    }
}
