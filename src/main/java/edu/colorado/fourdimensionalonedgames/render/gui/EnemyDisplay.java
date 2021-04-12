package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.lang.reflect.GenericSignatureFormatError;
import java.util.List;

public class EnemyDisplay implements Observer, IRenderable {

    private GenericTile[][] displayTiles;
    private Tile[][][] oldState;
    private Tile[][][] newState;
    private Canvas canvas;
    //Send initial board to display
    public EnemyDisplay(Canvas canvas, Tile[][][] board){
        this.displayTiles = new GenericTile[board.length][board.length];
        this.canvas = canvas;
        oldState = board;
        newState = board;

        initializeDisplayTiles();

        update(newState);
    }

    private void initializeDisplayTiles(){
        GenericTile tile;

        for (int j = 0; j < oldState.length; j++) {
            tile = new GenericLetterTile(0, j, String.valueOf(j));
            this.displayTiles[0][j] = tile;
        }

        for (int i = 1; i < oldState.length; i++) {
            tile = new GenericLetterTile(i, 0, Character.toString((char) i + 64));
            this.displayTiles[i][0] = tile;
        }

        for(int i = 1; i < oldState.length; i++){
            for(int j = 1; j < oldState.length; j++){
                displayTiles[i][j] = new GenericTile(i, j);
            }
        }
    }

    //cycles through all GenericTiles and updates their colors based on
    //what the new board state looks like compared to the old state
    public void update(Tile[][][] board) {

        oldState = this.newState;
        this.newState = board;


        for(int x = 1; x < newState.length; x++){
            for(int y = 1; y < newState.length; y++){
                Tile oldTile = oldState[x][y][0];
                Tile newTile = newState[x][y][0];

                if(newTile.shot || newTile.revealed){
                    if(newTile instanceof ShipTile){
                        ShipTile newShipTile = (ShipTile) newTile;
                        if(!newShipTile.hasBeenRecorded()){
                            newShipTile.setBeenRecorded(true);
                        }
                        else{
                            if(!newShipTile.getShip().destroyed()){
                                continue;
                            }
                        }
                    }
                    if(oldTile.shot || oldTile.revealed){

                    }
                    displayTiles[x][y].setColor(newTile.getColor());
                }
            }
        }
    }

    private List<Color> getSubmergedColors(){
        return null;
    }

    @Override
    public void render() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();

        for(GenericTile[] tileColumn : displayTiles){
            for(GenericTile tile : tileColumn){
                tile.draw(gc);
            }
        }
    }
}
