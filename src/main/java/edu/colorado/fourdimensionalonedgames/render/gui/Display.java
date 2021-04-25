package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.IRenderable;
import edu.colorado.fourdimensionalonedgames.render.Render;
import edu.colorado.fourdimensionalonedgames.render.tile.CaptainsQuartersTile;
import edu.colorado.fourdimensionalonedgames.render.tile.GenericTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floor;

//Contains all that is needed for a player to see their own board
//This class differs from EnemyDisplay in how tiles are selected before they are actually drawn on the Canvas
public class Display implements Observer, IRenderable {
    protected Canvas grid;
    protected Tile[][][] boardState;

    //Send initial board to display
    public Display(Canvas grid, Tile[][][] board) {
        this.boardState = board;
        this.grid = grid;
    }

    @Override
    public void update(Tile[][][] newBoardState) {
        boardState = newBoardState;
    }

    @Override
    public void render() {
        GraphicsContext gc = this.grid.getGraphicsContext2D();

        for (Tile[][] tileColumn : boardState) {
            for (Tile[] tileStack : tileColumn) {
                List<Color> colors = shipColors(tileStack);
                Tile surfaceTile = tileStack[0];
                //implies no ships were on this tile, so just render the surface tile (mine, seatile, or powerup)
                if (colors.size() == 0) {
                    surfaceTile.draw(gc);
                }
                //if two or more ships are on the same spot, or a ship is submerged, we need to mix their colors
                else {
                    drawOverlappingTiles(gc, colors, surfaceTile.getColumn(), surfaceTile.getRow());
                }
            }
        }
    }

    public void drawOverlappingTiles(GraphicsContext gc, List<Color> colors, int column, int row) {

        int xOrigin = column * 40;
        int yOrigin = row * 40;

        //lines for the tile
        gc.setStroke(Color.BLACK);
        gc.strokeLine(xOrigin, yOrigin, xOrigin + 40, yOrigin);
        gc.strokeLine(xOrigin + 40, yOrigin, xOrigin + 40, yOrigin + 40);
        gc.strokeLine(xOrigin + 40, yOrigin + 40, xOrigin, yOrigin + 40);
        gc.strokeLine(xOrigin, yOrigin + 40, xOrigin, yOrigin);

        int colorCount = colors.size();
        int height = (int) floor(40 / colorCount);

        for (int i = 0; i < colorCount; i++) {
            gc.setFill(colors.get(i));
            gc.fillRect(xOrigin, yOrigin + (i * height), 40, height);
        }
    }


    //takes in an array of tiles all on top of each other (same x and y, differing z's)
    //returns a list of all colors of all ships on this x and y, if any exist
    private List<Color> shipColors(Tile[] tiles) {
        List<Color> returnColors = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile instanceof ShipTile) {
                returnColors.add(tile.getColor());
            }
        }
        return returnColors;
    }

    public Canvas getGrid() {
        return grid;
    }

    public void setGrid(Canvas grid) {
        this.grid = grid;
    }
}
