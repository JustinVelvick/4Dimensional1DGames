package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.game.Player;

import java.util.Stack;

//every turn, we will spawn a new FleetControl object which will have a "fresh" empty stack
public class FleetControl {
    Stack<MoveFleetCommand> fleetCommandStack;
    Fleet currentFleet;
    Player player;

    public FleetControl(Fleet fleet, Player player){
        this.currentFleet = fleet;
        this.player = player;
    }

    public void moveFleet(Orientation direction){
        MoveFleetCommand moveCommand = new MoveFleetCommand(player, currentFleet, direction);
        fleetCommandStack.push(moveCommand);

        moveCommand.execute();
    }

    public void undoMoveFleet(){
        MoveFleetCommand commandToUndo = fleetCommandStack.pop();
        commandToUndo.undo();
    }
}