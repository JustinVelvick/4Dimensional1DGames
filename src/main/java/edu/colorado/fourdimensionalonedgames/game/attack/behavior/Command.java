package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

public interface Command {
    void execute();
    void undo();
}
