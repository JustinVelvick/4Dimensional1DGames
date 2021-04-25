package edu.colorado.fourdimensionalonedgames.game.ship;

import java.util.ArrayList;
import java.util.List;

//object to track all ships a player has and encapsulate the idea of one part for fleet wide actions (moving the fleet)
public class Fleet {
    private List<Ship> fleet = new ArrayList<>();

    //empty constructor
    public Fleet() {

    }

    //ship list manipulation
    public void addShip(Ship newShip) {
        this.fleet.add(newShip);
    }

    public List<Ship> getShips() {
        return this.fleet;
    }

    //check if any non-destroyed ships exist in the fleet
    public boolean hasShip() {
        for (Ship ship : fleet) {
            if (!ship.destroyed()) {
                return true;
            }
        }
        return false;
    }

    //returns all destroyed ships in the fleet
    public List<Ship> getDestroyedShips() {
        List<Ship> destroyedShips = new ArrayList<>();
        for (Ship ship : fleet) {
            if (ship.destroyed()) {
                destroyedShips.add(ship);
            }
        }
        return destroyedShips;
    }

    //checks to see if any ships in the fleet have picked up a powerUp
    public boolean hasPowerUp() {
        for (Ship ship : fleet) {
            if (ship.getPowerUps() > 0) {
                return true;
            }
        }
        return false;
    }
}
