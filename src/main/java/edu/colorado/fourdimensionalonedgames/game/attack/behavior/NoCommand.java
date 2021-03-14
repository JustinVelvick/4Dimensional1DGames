package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

public class NoCommand implements Command {
    @Override
    public void execute() {
        //a no command is only executed when there is no ship movement to undo
        //maybe spawn an AlertBox and tell user theres nothing to undo?
    }

    @Override
    public void undo() {

    }
}
