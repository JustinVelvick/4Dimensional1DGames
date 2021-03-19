package edu.colorado.fourdimensionalonedgames.game.ship;

public interface Command {
    boolean execute();
    void undo();
}
