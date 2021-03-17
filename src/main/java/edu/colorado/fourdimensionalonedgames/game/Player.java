package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Reveal;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.LargeWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.PenetratingSmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Player {
    //account specific info
    private String ign;
    private int victories;

    private Board board;
    private Board enemyBoard;
    private List<Weapon> weapons = new ArrayList<>();
    private List<Ship> shipsToPlace = new ArrayList<>();
    private Fleet fleet;

    //constructor
    public Player (Board board, Board enemyBoard) {
        this.board = board;
        this.enemyBoard = enemyBoard;
        this.fleet = new Fleet();
        generateShips();
        generateWeapons();
    }

    private void generateShips(){
        ShipYard defaultShipYard = new DefaultShipYard();
        ShipYard submergableShipYard = new SubmergableShipYard();

        shipsToPlace.add(defaultShipYard.createShip("Minesweeper"));
        shipsToPlace.add(defaultShipYard.createShip("Destroyer"));
        shipsToPlace.add(defaultShipYard.createShip("Battleship"));
        shipsToPlace.add(submergableShipYard.createShip("Submarine"));
    }

    private void generateWeapons(){
        weapons.add(new SmallWeapon(new Attack(), "Single Shot"));
        weapons.add(new LargeWeapon(new Reveal(), "Sonar Pulse"));
        weapons.add(new PenetratingSmallWeapon(new Attack(), "Space Laser"));
    }

    public void updateVisuals(){

    }

    /**
     * Mount an attack on the given enemy board at attackCoords with weaponChoice
     *NOTE: PLAYER INPUT SHOULD BE VALIDATED AND FILTERED BY THIS POINT
     *
     * @return   returns AttackResult object in the form of {AttackResultType enum, Ship(if applicable)}
     */
    public List<AttackResult> attack(Board opponentBoard, PlayerFireInput input) {

        Weapon weapon = stringToWeapon(input.getWeaponChoice());
        double x = Double.parseDouble(input.getxCord());
        double y = Double.parseDouble(input.getyCord());

        Point2D attackCoords = new Point2D(x, y);
        List<AttackResult> results = weapon.useAt(opponentBoard, attackCoords);

        return results;
    }

    //matches weapon that user picked in GUI with those stored in player's list of weapons avaliable
    private Weapon stringToWeapon(String weaponChoice){
        Weapon returnWeapon = null;

        for( Weapon weapon : weapons){
            if(weapon.getType().equals(weaponChoice)){
                returnWeapon = weapon;
            }
        }

        return returnWeapon;
    }

    public Boolean placeShip(PlayerShipInput input){
        Orientation direction = Orientation.down;
        double x =  Double.parseDouble(input.getxCord());
        double y =  Double.parseDouble(input.getyCord());
        Point3D origin = new Point3D(x, y, 0);

        Ship newShip = new Destroyer();

        String shipChoice = input.getShipChoice();
        String orientationChoice = input.getDirection();

        for(Ship ship : this.getShipsToPlace()){
            if(shipChoice.equals(ship.getType())){
                newShip = ship;
            }
        }
        switch (orientationChoice) {
            case "Up":
                direction = Orientation.up;
                break;

            case "Down":
                direction = Orientation.down;
                break;

            case "Left":
                direction = Orientation.left;
                break;

            case "Right":
                direction = Orientation.right;
                break;
        }

        if(getBoard().placeShip(direction, origin, newShip)){
            //add this completed, built ship to the fleet
            fleet.addShip(newShip);
            //Remove shipToPlace from player list of shipsToPlace since placement succeeded
            removeShipToPlace(newShip);
        }
        else{
            return false;
        }
        return true;
    }

    public void moveFleet(Orientation direction, Fleet newFleet){
        for(Ship ship : fleet.getShips()){
            for(ShipTile tile : ship.getShipTiles()){

            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public Board getEnemyBoard() {
        return enemyBoard;
    }

    public List<Ship> getShipsToPlace() {
        return shipsToPlace;
    }

    public void removeShipToPlace(Ship ship){
        this.shipsToPlace.remove(ship);
    }

    public List<Weapon> getWeapons(){return weapons;}

    public Fleet getFleet(){
        return fleet;
    }
}
