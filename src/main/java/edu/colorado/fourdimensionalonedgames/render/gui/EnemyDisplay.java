package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.LetterTile;
import edu.colorado.fourdimensionalonedgames.render.tile.SeaTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.scene.layout.GridPane;

public class EnemyDisplay extends Display implements Observer{

    //Send initial board to display
    public EnemyDisplay(GridPane gpane, Tile[][][] board, Render renderer){
        super(gpane, board, renderer);
    }

    @Override
    public void update(Tile[][][] newBoardState) {
        //unregister and reregister tiles to renderer
        //remove and add children to gpane

        for(Tile[][] tileColumn : newBoardState){
            for(Tile[] tileRow : tileColumn){
                Tile newTile = tileRow[0];
                if(newTile instanceof ShipTile){
                    if(newTile.revealed || newTile.shot){
                        swapTile(newTile);
                    }
                    else{
                        //swapTile(new SeaTile(newTile.getColumn(), newTile.getRow()));
                    }
                }
                else if(newTile instanceof SeaTile || newTile instanceof LetterTile){
                    swapTile(newTile);
                }
            }
        }
    }
}
