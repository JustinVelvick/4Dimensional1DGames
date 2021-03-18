package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.TierOneUpgrade;
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
    private TierOneUpgrade upgradeStatus; //unlocks 2 sonar pulses and replaces single shot with space laser at int = 0

    //constructor
    public Player (Board board, Board enemyBoard) {
        this.board = board;
        this.enemyBoard = enemyBoard;
        this.fleet = new Fleet();
        this.upgradeStatus = TierOneUpgrade.LOCKED;
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
        weapons.add(new SmallWeapon(new Attack(), Game.SINGLE_SHOT));
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

        for(AttackResult attackResult : results){
            Ship attackedShip = attackResult.ship;

            if (attackedShip == null) {
                //when missed shot
            }
            else if (attackedShip.destroyed()) {
                //when ship has been destroyed
                fleet.destroyShip(attackedShip);
                if(this.upgradeStatus == TierOneUpgrade.LOCKED){
                    this.upgradeStatus = TierOneUpgrade.UNLOCKED;
                }

            } else {
                //when ship has been hit
            }
        }
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

        //Set z axis
        if(input.getSubmergeChoice().equals("Yes")){
            newShip.setShipTileDepth(1);
            origin = new Point3D(origin.getX(), origin.getY(), 1);
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

    public TierOneUpgrade getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(TierOneUpgrade upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public void removeWeapon(String weaponToRemove){
        Weapon weaponToDelete = new SmallWeapon(new Attack(),Game.SINGLE_SHOT);
        for(Weapon weapon : this.weapons){
            if(weapon.getType().equals(weaponToRemove)){
                weaponToDelete = weapon;
            }
        }
        this.getWeapons().remove(weaponToDelete);
    }


    public Fleet getFleet(){
        return fleet;
    }
}
