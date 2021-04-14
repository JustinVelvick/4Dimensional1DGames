package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.PopCountAfterAttackBehavior;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.MineCollection;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.PowerUpsCollection;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.TierOneUpgrade;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.MediumWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.*;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    //account specific info
    private String ign;
    private int victories;
    private int score;
    private int missedShots;
    private int totalShots;

    private final Game game;
    private final Board board;
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Ship> shipsToPlace = new ArrayList<>();
    private final Fleet fleet;
    private TierOneUpgrade upgradeStatus; //unlocks 2 sonar pulses and replaces single shot with space laser at int = 0
    private MineCollection mines;
    private FleetControl fleetController;
    private PowerUpsCollection PowerUps;

    private boolean devMode = true;

    //constructor
    public Player (Game game, Board board, Board enemyBoardGui) {
        this.game = game;
        this.board = board;
        this.fleet = new Fleet();
        this.upgradeStatus = TierOneUpgrade.LOCKED;
        this.mines = MineCollection.UNAVAILABLE;
        this.score = 0;
        this.missedShots = 0;
        this.totalShots = 0;
        this.fleetController = new FleetControl(this);
        generateShips();
        generateWeapons();
    }

    private void generateShips(){
        ShipYard defaultShipYard = new DefaultShipYard();
        ShipYard submergableShipYard = new SubmergableShipYard();

        shipsToPlace.add(defaultShipYard.createShip("Minesweeper"));
        shipsToPlace.add(submergableShipYard.createShip("Submarine"));
        if(!devMode){
            shipsToPlace.add(defaultShipYard.createShip("Destroyer"));
            shipsToPlace.add(defaultShipYard.createShip("Battleship"));
        }
    }

    private void generateWeapons(){
        weapons.add(new SmallWeapon(new Attack(), Game.SINGLE_SHOT));
    }

    /**
     * Mount an attack on the given enemy board at attackCoords with weaponChoice
     *NOTE: PLAYER INPUT SHOULD BE VALIDATED AND FILTERED BY THIS POINT
     *
     * @return   returns list of resulting AttackResult objects in the form of {AttackResultType enum, Ship(if applicable)}
     */
    public List<AttackResult> attack(Board opponentBoard, PlayerFireInput input) {

        Weapon weapon = stringToWeapon(input.getWeaponChoice());
        double x = Double.parseDouble(input.getxCord());
        double y = Double.parseDouble(input.getyCord());

        Point2D attackCoords = new Point2D(x, y);

        //if sonar being used, remove a sonar object from player's list of weapons to decrement uses
        if(weapon.doRemove()){
            this.weapons.remove(weapon);
        }

        List<AttackResult> results = weapon.useAt(opponentBoard, attackCoords);
        opponentBoard.updateObservers();

        for(AttackResult attackResult : results){
            Ship attackedShip = attackResult.getShip();

            if (attackedShip == null) {
                //when missed shot
                missedShots++;
            }
            else if (attackedShip.destroyed()) {
                //when ship has been destroyed
                if(this.upgradeStatus == TierOneUpgrade.LOCKED){
                    this.upgradeStatus = TierOneUpgrade.UNLOCKED;
                }
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
            //add mines
            checkMines();
            checkPowerUps();
        }
        else{
            return false;
        }
        return true;
    }

    public void checkMines(){
        if(shipsToPlace.isEmpty()){
            mines = MineCollection.AVAILABLE;
        }
        if(mines == MineCollection.AVAILABLE){
            mines = MineCollection.PLACED;
            placeMines();
        }
    }

    public void placeMines(){
        Random random = new Random();
        int minesToPlace = 5;

        while(minesToPlace > 0){
            int i = random.nextInt(10) + 1;
            int j = random.nextInt(10) + 1;
            Tile oldTile = board.tiles[i][j][0];
            if (oldTile instanceof SeaTile){
                Tile mineTile = new MineTile(i,j,0);//START HERE
                board.tiles[i][j][0] = mineTile;
                minesToPlace--;
            }
        }
        board.updateLocalObservers();
    }

    //for testing purposes only to give a deterministic spawning of mines
    public void placeTestMines(int i, int j) {
        if (board.tiles[i][j][0] instanceof SeaTile) {
            Tile mineTile = new MineTile(i, j, 0);//START HERE
            board.tiles[i][j][0] = mineTile;
        }
    }



    //will only move entire fleet if every ship is able to move specified direction
    public boolean moveFleet(Orientation direction){
        // check for border collision
        switch (direction){
            case up:
                for (Ship ship : fleet.getShips()){
                    for (ShipTile tile : ship.getShipTiles()){
                        if (tile.getRow() - 1 < 1){
                            return false;
                        }
                    }
                }
                break;
            case down:
                for (Ship ship : fleet.getShips()){
                    for (ShipTile tile : ship.getShipTiles()){
                        if (tile.getRow() + 1 > 10){
                            return false;
                        }
                    }
                }
                break;
            case right:
                for (Ship ship : fleet.getShips()){
                    for (ShipTile tile : ship.getShipTiles()){
                        if (tile.getColumn() + 1 > 10){
                            return false;
                        }
                    }
                }
                break;
            case left:
                for (Ship ship : fleet.getShips()){
                    for (ShipTile tile : ship.getShipTiles()){
                        if (tile.getColumn() - 1 < 1){
                            return false;
                        }
                    }
                }
                break;
            default:
                break;
        }

        // actually move the fleet now that we confirmed it can be moved
        for (Ship ship : fleet.getShips()){
            board.moveShip(ship, direction);
        }
        board.updateLocalObservers();
        return true;
    }



    public void checkPowerUps(){
        if(shipsToPlace.isEmpty()){
            PowerUps = PowerUpsCollection.AVAILABLE;
        }
        if(PowerUps == PowerUpsCollection.AVAILABLE){
            PowerUps = PowerUpsCollection.PLACED;
            placePowerUps();
        }
    }



    public void placePowerUps(){
        Random random = new Random();
        int powerUpsToPlace = 2;

        while(powerUpsToPlace > 0){
            int i = random.nextInt(10) + 1;
            int j = random.nextInt(10) + 1;
            Tile oldTile = board.tiles[i][j][0];
            if (oldTile instanceof SeaTile){
                Tile powerUpTile = new PowerUpTile(i,j,0);//START HERE
                board.tiles[i][j][0] = powerUpTile;
                powerUpsToPlace--;
            }
        }
        board.updateLocalObservers();
    }

    public void removeWeapon(String weapon) {
        this.weapons.remove(stringToWeapon(weapon));
    }

    public Board getBoard() {
        return board;
    }

    public List<Ship> getShipsToPlace() {
        return shipsToPlace;
    }

    public List<Weapon> getWeapons(){return weapons;}

    public TierOneUpgrade getUpgradeStatus() {
        return upgradeStatus;
    }

    public Fleet getFleet(){
        return fleet;
    }

    public String getIgn() {
        return ign;
    }

    public int getVictories() {
        return victories;
    }

    public int getScore() {
        return score;
    }

    public Game getGame(){
        return game;
    }

    public FleetControl getFleetController(){
        return fleetController;
    }

    public void refreshFleetController(){
        fleetController = new FleetControl(this);
    }

    public void setUpgradeStatus(TierOneUpgrade upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public void setScore(int newScore){
        this.score = newScore;
    }

    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public void removeShipToPlace(Ship ship){
        this.shipsToPlace.remove(ship);
    }
}
