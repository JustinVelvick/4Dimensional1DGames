package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.game.Player;

import java.util.Stack;

//every turn, we will spawn a new FleetControl object which will have a "fresh" empty stack
public class FleetControl {
    private Stack<MoveFleetCommand> fleetCommandStack;
    private Player player;

    public FleetControl(Player player){
        this.player = player;
        this.fleetCommandStack = new Stack<>();
    }

    public void moveFleet(Orientation direction){
        MoveFleetCommand moveCommand = new MoveFleetCommand(player, direction);
        if (moveCommand.execute())
            fleetCommandStack.push(moveCommand);
    }

    public void undoMoveFleet(){
        MoveFleetCommand commandToUndo = fleetCommandStack.pop();
        commandToUndo.undo();
    }
}
