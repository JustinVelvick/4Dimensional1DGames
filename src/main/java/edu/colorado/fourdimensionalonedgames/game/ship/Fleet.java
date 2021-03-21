package edu.colorado.fourdimensionalonedgames.game.ship;

import java.util.ArrayList;
import java.util.List;

public class Fleet {
    private List<Ship> fleet = new ArrayList<>();

    //empty constructor
    public Fleet(){

    }

    //ship list manipulation
    public void addShip(Ship newShip){
        this.fleet.add(newShip);
    }

    public List<Ship> getShips(){
        return this.fleet;
    }
    //check if any ships exist
    public boolean hasShip() {
        for (Ship ship : fleet) {
            if (!ship.destroyed()) {
                return true;
            }
        }
        return false;
    }

    public List<Ship> getDestroyedShips(){
        List<Ship> destroyedShips = new ArrayList<>();
        for(Ship ship : fleet){
            if(ship.destroyed()){
                destroyedShips.add(ship);
            }
        }
        return destroyedShips;
    }
}
