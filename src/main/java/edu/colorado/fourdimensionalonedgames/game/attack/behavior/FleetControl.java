package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

import edu.colorado.fourdimensionalonedgames.game.ship.Fleet;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;

import java.util.Stack;

public class FleetControl {
    Stack<MoveFleetCommand> fleetCommandStack;
    Fleet currentFleet;

    public FleetControl(Fleet fleet){
        this.currentFleet = fleet;
    }

    public void moveFleet(Orientation direction){
        MoveFleetCommand moveCommand = new MoveFleetCommand(currentFleet, direction);
        fleetCommandStack.push(moveCommand);

        moveCommand.execute();

    }

    public void undoMoveFleet(){
        MoveFleetCommand commandToUndo = fleetCommandStack.pop();
        commandToUndo.undo();
    }
}
