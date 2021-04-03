package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.scene.layout.GridPane;

public class Display implements Observer{
    protected GridPane gpane;
    protected Tile[][][] boardState;
    protected Render renderer;

    //Send initial board to display
    public Display(GridPane gpane, Tile[][][] board, Render renderer){
        this.boardState = board;
        this.renderer = renderer;
        this.gpane = gpane;
    }

    @Override
    public void update(Tile[][][] newBoardState) {
        //unregister and reregister tiles to renderer
        //remove and add children to gpane

        for(Tile[][] tileColumn : newBoardState){
            for(Tile[] tileRow : tileColumn){
                swapTile(tileRow[0]);
            }
        }

        boardState = newBoardState;
    }

    //replace a tile on the board with an input tile (newTile) and do proper re registering and gridpane updating
    protected void swapTile(Tile newTile){
        Tile oldTile;

        int x = newTile.getColumn();
        int y = newTile.getRow();
        int z = newTile.getDepth();

        oldTile = boardState[x][y][z];

        //re-register that spot with the renderer
        renderer.unregister(oldTile);
        renderer.register(newTile);

        //remove old tile, add new tile to our GridPane
        gpane.getChildren().remove(oldTile);
        gpane.add(newTile, x, y);

        //Update display object with the new board state
        //boardState[x][y][z] = newTile;
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
