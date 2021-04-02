package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.LetterTile;
import edu.colorado.fourdimensionalonedgames.render.tile.SeaTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.scene.layout.GridPane;

public class EnemyDisplay implements Observer{
    private GridPane gpane;
    private Tile[][] boardState;
    private Render renderer;
    //Send initial board to display
    public EnemyDisplay(GridPane gpane, Tile[][] board, Render renderer){
        this.boardState = board;
        this.renderer = renderer;
        this.gpane = gpane;
    }

    @Override
    public void update(Tile[][] newBoardState) {
        //unregister and reregister tiles to renderer
        //remove and add children to gpane
        for(Tile[] tileRow : newBoardState){
            for(Tile newTile : tileRow){
                if(newTile instanceof ShipTile){
                    if(newTile.revealed || newTile.shot){
                        swapTile(newTile);
                    }
                    else{
                        swapTile(new SeaTile(newTile.getColumn(), newTile.getRow()));
                    }
                }
                else if(newTile instanceof SeaTile || newTile instanceof LetterTile){
                    swapTile(newTile);
                }
            }
        }
    }

    //replace a tile on the board with an input tile (newTile) and do proper re registering and gridpane updating
    private void swapTile(Tile newTile){
        Tile oldTile;

        int x = newTile.getColumn();
        int y = newTile.getRow();

        oldTile = boardState[x][y];

        //re-register that spot with the renderer
        renderer.unregister(oldTile);
        renderer.register(newTile);

        //remove old tile, add new tile to our GridPane
        gpane.getChildren().remove(oldTile);
        gpane.add(newTile, x, y);

        //Update display object with the new board state
        //boardState[x][y] = newTile;
        //boardState[x][y] = newTile;
    }


    public GridPane getGpane() {
        return gpane;
    }

    public void setGpane(GridPane gpane) {
        this.gpane = gpane;
    }

    public Render getRenderer() {
        return renderer;
    }

    public void setRenderer(Render renderer) {
        this.renderer = renderer;
    }
}
