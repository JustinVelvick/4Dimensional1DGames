package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.render.tile.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

//Contains all that is needed for a player to see the ENEMY'S BOARD (what they have seen so far)
//This class differs from Display in how tiles are selected before they are actually drawn on the Canvas
public class EnemyDisplay implements Observer, IRenderable {

    private GenericTile[][] displayTiles;
    private Tile[][][] oldState;
    private Tile[][][] newState;
    private Canvas grid;
    //Send initial board to display
    public EnemyDisplay(Canvas grid, Tile[][][] board){
        this.displayTiles = new GenericTile[board.length][board.length];
        this.grid = grid;
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

    //UPDATE METHOD in EnemyDisplay differs greatly from Display as it must decide if each tile should be seen or not
    //cycles through all GenericTiles and updates their colors based on
    //what the new board state looks like compared to the old state
    public void update(Tile[][][] board) {

        oldState = this.newState;
        this.newState = board;

        boolean isPrimativeTileType;
        for(int x = 1; x < newState.length; x++){
            for(int y = 1; y < newState.length; y++){
                Tile oldTile = oldState[x][y][0];
                Tile newTile = newState[x][y][0];
                isPrimativeTileType = oldTile instanceof PowerUpTile || oldTile instanceof MineTile;

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
                        if(isPrimativeTileType){
                            continue;
                        }
                    }
                    displayTiles[x][y].setColor(newTile.getColor());
                }
                //************UNCOMMENT BELOW IF YOU WANT ENEMY TO SEE A HIT, BUT NOT DESTROYED CC TILE
                //************CSCI 4448 REQUIREMENTS SAID FOR HIT ON CC TO ACT AS A MISS
//                else if(newTile instanceof CaptainsQuartersTile){
//                    if(((CaptainsQuartersTile) newTile).getHp() < ((CaptainsQuartersTile) newTile).getStartingHp()){
//                        displayTiles[x][y].setColor(newTile.getColor());
//                    }
//                }
            }
        }
    }

    @Override
    public void render() {
        GraphicsContext gc = this.grid.getGraphicsContext2D();

        for(GenericTile[] tileColumn : displayTiles){
            for(GenericTile tile : tileColumn){
                tile.draw(gc);
            }
        }
    }
}
