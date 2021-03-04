package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResultType;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
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
    }

    private void generateShips(){
        shipsToPlace.add(new Minesweeper());
        shipsToPlace.add(new Destroyer());
        shipsToPlace.add(new Battleship());
    }



    /**
     * Mount an attack on the given enemy tile
     *
     *
     * @return   returns AttackResult object in the form of {AttackResultType enum, Ship(if applicable)}
     */
    public AttackResult attack(Board opponent, Point2D attackCoords) {
        int x = (int) attackCoords.getX();
        int y = (int) attackCoords.getY();

        // check that provided coords are on board, throw exception if not
        if (!opponent.isWithinBounds(attackCoords))
            throw new InvalidAttackException("attack coordinates off of board");

        // get tile to be attacked
        Tile attackedTile = opponent.tiles[x][y];

        // if already attacked, throw exception
        if (attackedTile.shot) throw new InvalidAttackException("tile has already been attacked");

        // otherwise set shot flag and return ship that contains tile
        attackedTile.shot = true;

        Ship ship = attackedTile.getShip();

        if (ship == null) return new AttackResult(AttackResultType.MISS, null);

        if (opponent.gameOver()) return new AttackResult(AttackResultType.SURRENDER, ship);

        if (ship.destroyed()) return new AttackResult(AttackResultType.SUNK, ship);

        return new AttackResult(AttackResultType.HIT, ship);
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
}
