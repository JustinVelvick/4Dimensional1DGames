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

    public void removeShip(Ship ship){
        fleet.remove(ship);
    }

    public List<Ship> getShips(){
        return this.fleet;
    }
    //check if any ships exist
    public boolean hasShip(){
        if(fleet.isEmpty()){
            return false;
        }
        return true;
    }
}
