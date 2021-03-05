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
        shipsToPlace.add(new Minesweeper());
        shipsToPlace.add(new Destroyer());
        shipsToPlace.add(new Battleship());
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
    public List<AttackResult> attack(Board opponent, Point2D attackCoords, Weapon weapon) {
        return weapon.useAt(board, attackCoords);
    }


    public Boolean placeShip(GridPane gpane, Orientation direction, Point2D origin, Ship shipToPlace){

        //if ship placement on board didn't succeed, return false
        if(!getBoard().placeShip(gpane, direction, origin, shipToPlace)){
            return false;
        }


        //Remove shipToPlace from player list of shipsToPlace since placement succeeded
        removeShipToPlace(shipToPlace);
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
