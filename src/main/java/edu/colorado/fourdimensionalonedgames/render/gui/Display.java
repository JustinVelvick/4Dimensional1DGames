package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.scene.layout.GridPane;

public class Display implements Observer{
    private GridPane gpane;
    private Tile[][] boardState;
    private Render renderer;

    //Send initial board to display
    public Display(GridPane gpane, Tile[][] board, Render renderer){
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
                swapTile(newTile);
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
        boardState[x][y] = newTile;
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
