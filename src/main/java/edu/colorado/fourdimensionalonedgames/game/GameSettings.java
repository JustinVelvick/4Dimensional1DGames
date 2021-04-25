package edu.colorado.fourdimensionalonedgames.game;

public class GameSettings {
    private int numberOfPlayers;
    private int columns;
    private int rows;
    private int depth;
    private int tileSize;

    public GameSettings(int numberOfPlayers, int columns, int rows, int depth, int tileSize) {
        this.numberOfPlayers = numberOfPlayers;
        this.columns = columns;
        this.rows = rows;
        this.depth = depth;
        this.tileSize = tileSize;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getDepth() {
        return depth;
    }

    public int getTileSize() {
        return tileSize;
    }
}
