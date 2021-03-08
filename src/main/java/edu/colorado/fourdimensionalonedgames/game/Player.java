package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Reveal;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.LargeWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
import javafx.geometry.Point2D;
import javafx.scene.layout.GridPane;

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

    //constructor
    public Player (Board board, Board enemyBoard) {
        this.board = board;
        this.enemyBoard = enemyBoard;
        generateShips();
        generateWeapons();
    }

    private void generateShips(){
        ShipYard shipYard = new DefaultShipYard();

        shipsToPlace.add(shipYard.createShip("Minesweeper"));
        shipsToPlace.add(shipYard.createShip("Destroyer"));
        shipsToPlace.add(shipYard.createShip("Battleship"));
    }

    private void generateWeapons(){
        weapons.add(new SmallWeapon(new Attack(), "Single Shot"));
        weapons.add(new LargeWeapon(new Reveal(), "Sonar"));
    }



    /**
     * Mount an attack on the given enemy tile
     *
     *
     * @return   returns AttackResult object in the form of {AttackResultType enum, Ship(if applicable)}
     */
    public List<AttackResult> attack(Board opponent, Point2D attackCoords, String weaponChoice) {
        Weapon weapon = stringToWeapon(weaponChoice);

        return weapon.useAt(opponent, attackCoords);
    }

    private Weapon stringToWeapon(String weaponChoice){
        Weapon weapon = new SmallWeapon(new Attack(), "error");
        switch (weaponChoice){
            case "Single Shot":
                weapon = new SmallWeapon(new Attack(), weaponChoice);
                break;
            case "Sonar Pulse":
                weapon = new LargeWeapon(new Reveal(), weaponChoice);
                break;
            default:
                break;
        }

        return weapon;
    }

    //DELETE ME EVENTUALLY AND KEEP METHODS BELOW IT
    public Boolean placeShip(GridPane gpane, Orientation direction, Point2D origin, Ship shipToPlace){

        //if ship placement on board didn't succeed, return false
        if(!getBoard().placeShip(gpane, direction, origin, shipToPlace)){
            return false;
        }
        //Remove shipToPlace from player list of shipsToPlace since placement succeeded
        removeShipToPlace(shipToPlace);
        return true;
    }

    public Boolean placeShipNew(GridPane gpane, PlayerShipInput input){
        Orientation direction = Orientation.down;
        double x =  input.getxCord();
        double y =  input.getyCord();
        Point2D origin = new Point2D(x, y);

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

        if(getBoard().placeShip(gpane, direction, origin, newShip)){
            //Remove shipToPlace from player list of shipsToPlace since placement succeeded
            removeShipToPlace(newShip);
        }
        else{
            return false;
        }
        return true;
    }

    public Boolean placeEnemyShip(GridPane gpane, PlayerShipInput input){
        Orientation direction = Orientation.down;
        double x =  input.getxCord();
        double y =  input.getyCord();
        Point2D origin = new Point2D(x, y);

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

        if(!getEnemyBoard().placeShip(gpane, direction, origin, newShip)){
            return false;
        }

        return true;
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
}
