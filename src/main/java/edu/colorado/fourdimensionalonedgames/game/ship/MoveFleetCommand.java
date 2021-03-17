package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.game.Player;

public class MoveFleetCommand implements Command{

    Fleet currentFleet;
    Orientation moveDirection;
    Player player;

    public MoveFleetCommand(Player player, Fleet currentFleet, Orientation direction){
        this.moveDirection = direction;
        this.currentFleet = currentFleet;
    }
    @Override
    public void execute() {
        //Call code that actually moves the fleet
        player.moveFleet(moveDirection, currentFleet);
    }

    @Override
    public void undo() {
        //Call code that actually moves the fleet, but using the ships and tile positions in restoredState
        switch (moveDirection){
            case up:
                player.moveFleet(Orientation.down, currentFleet);
                break;
            case down:
                player.moveFleet(Orientation.up, currentFleet);
                break;
            case right:
                player.moveFleet(Orientation.left, currentFleet);
                break;
            case left:
                player.moveFleet(Orientation.right, currentFleet);
        }
    }
}
