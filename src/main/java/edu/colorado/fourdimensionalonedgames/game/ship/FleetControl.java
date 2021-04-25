package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.game.Player;

import java.util.Stack;

//FleetControl is what the client interacts with when wanting to move their fleet
//The stack holds the history of all moves the player has made this turn
//Every turn, we will spawn a new FleetControl object which will have a "fresh" empty stack
public class FleetControl {
    private Stack<MoveFleetCommand> fleetCommandStack;
    private Player player;

    public FleetControl(Player player) {
        this.player = player;
        this.fleetCommandStack = new Stack<>();
    }

    public void moveFleet(Orientation direction) {
        MoveFleetCommand moveCommand = new MoveFleetCommand(player, direction);
        //only add to the stack if the ships were actually able to move
        if (moveCommand.execute())
            fleetCommandStack.push(moveCommand);
    }

    public void undoMoveFleet() {
        MoveFleetCommand commandToUndo = fleetCommandStack.pop();
        commandToUndo.undo();
    }
}
