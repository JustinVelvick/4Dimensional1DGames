package edu.colorado.fourdimensionalonedgames.game.attack.behavior;

import edu.colorado.fourdimensionalonedgames.game.ship.Fleet;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;

import java.util.Stack;

public class MoveFleetCommand implements Command{

    Fleet currentFleet;
    Orientation moveDirection;

    public MoveFleetCommand(Fleet currentFleet, Orientation direction){
        this.moveDirection = direction;
        this.currentFleet = currentFleet;
    }
    @Override
    public void execute() {

        //Call code that actually moves the fleet
    }

    @Override
    public void undo() {
        //Call code that actually moves the fleet, but using the ships and tile positions in restoredState
    }
}
