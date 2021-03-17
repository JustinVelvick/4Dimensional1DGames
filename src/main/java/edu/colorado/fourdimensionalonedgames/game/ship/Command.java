package edu.colorado.fourdimensionalonedgames.game.ship;

public interface Command {
    void execute();
    void undo();
}
