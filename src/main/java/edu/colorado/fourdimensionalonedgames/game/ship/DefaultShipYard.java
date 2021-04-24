package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;

import java.util.List;

//concrete factory for non-submergable ships
public class DefaultShipYard extends ShipYard{

    @Override
    public Ship createShip(String choice){
        Ship newShip = buildShip(choice);
        ///
        //do other things with freshly built ship (maybe in future requirements)
        ///
        generateShipTiles(newShip);
        generateCaptainsQuarters(newShip);

        return newShip;
    }

    protected Ship buildShip(String choice) {
        switch (choice){
            case "Minesweeper":
                return new Minesweeper();
            case "Destroyer":
                return new Destroyer();
            case "Battleship":
                return new Battleship();
            case "Submarine":
                return new Submarine();
            default:
                return null;
        }
    }

    //generates default tiles to be overridden later (these are all at col 0, row 0)
    private void generateShipTiles(Ship newShip){
        for(int i = 0; i < newShip.size; i++){
            newShip.addTile(new ShipTile(newShip,0,0));
        }
    }

    private void generateCaptainsQuarters(Ship newShip){

        ShipTile newTile;
        List<ShipTile> tiles = newShip.getShipTiles();
        Tile tileToReplace;
        switch (newShip.getType()) {
            case "Minesweeper":
                tileToReplace = tiles.get(0);
                newTile = new CaptainsQuartersTile(newShip, tileToReplace.getColumn(), tileToReplace.getRow(), 1);
                tiles.set(0, newTile);
                break;
            case "Destroyer":
                tileToReplace = tiles.get(1);
                newTile = new CaptainsQuartersTile(newShip, tileToReplace.getColumn(), tileToReplace.getRow(), 2);
                tiles.set(1, newTile);
                break;
            case "Battleship":
                tileToReplace = tiles.get(2);
                newTile = new CaptainsQuartersTile(newShip, tileToReplace.getColumn(), tileToReplace.getRow(), 2);
                tiles.set(2, newTile);
                break;
            default:
                break;
        }
    }
}
