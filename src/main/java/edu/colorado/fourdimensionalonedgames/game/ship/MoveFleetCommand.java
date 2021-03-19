package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.game.Player;

public class MoveFleetCommand implements Command{

    Fleet currentFleet;
    Orientation moveDirection;
    Player player;

    public MoveFleetCommand(Player player, Fleet currentFleet, Orientation direction){
        this.player = player;
        this.moveDirection = direction;
        this.currentFleet = currentFleet;
    }
    @Override
    public boolean execute() {
        //Call code that actually moves the fleet
        return player.moveFleet(moveDirection);
    }

    @Override
    public void undo() {
        //Call code that actually moves the fleet, but using the ships and tile positions in restoredState
        switch (moveDirection){
            case up:
                player.moveFleet(Orientation.down);
                break;
            case down:
                player.moveFleet(Orientation.up);
                break;
            case right:
                player.moveFleet(Orientation.left);
                break;
            case left:
                player.moveFleet(Orientation.right);
        }
    }
}
