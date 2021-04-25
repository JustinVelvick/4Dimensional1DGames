package edu.colorado.fourdimensionalonedgames.game.ship;

import edu.colorado.fourdimensionalonedgames.game.Player;

//One of these are generated for every single move the player enacts on the fleet
//FleetControl class will have a stack of these ready to pop and un pop at the user's will
public class MoveFleetCommand implements Command {

    private final Orientation moveDirection;
    private final Player player;

    public MoveFleetCommand(Player player, Orientation direction) {
        this.player = player;
        this.moveDirection = direction;
    }

    @Override
    public boolean execute() {
        //Call code that actually moves the fleet
        return player.moveFleet(moveDirection);
    }

    @Override
    public void undo() {
        //Call code that actually moves the fleet (but in the opposite direction to give the undo effect)
        switch (moveDirection) {
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
