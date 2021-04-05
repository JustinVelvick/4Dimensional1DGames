package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.MineCollection;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.TierOneUpgrade;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.MineTile;
import edu.colorado.fourdimensionalonedgames.render.tile.SeaTile;
import edu.colorado.fourdimensionalonedgames.render.tile.ShipTile;
import edu.colorado.fourdimensionalonedgames.render.tile.Tile;
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
    private final Board enemyBoardGui;
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Ship> shipsToPlace = new ArrayList<>();
    private final List<Ship> phantomShipsToPlace = new ArrayList<>(); //for enemy boards (GUI RELATED)
    private final Fleet fleet;
    private TierOneUpgrade upgradeStatus; //unlocks 2 sonar pulses and replaces single shot with space laser at int = 0
    private MineCollection mines;
    private FleetControl fleetController;

    //constructor
    public Player (Game game, Board board, Board enemyBoardGui) {
        this.game = game;
        this.board = board;
        this.enemyBoardGui = enemyBoardGui;
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
        shipsToPlace.add(defaultShipYard.createShip("Destroyer"));
        shipsToPlace.add(defaultShipYard.createShip("Battleship"));
        shipsToPlace.add(submergableShipYard.createShip("Submarine"));

        //Ship objects to be placed on enemy boards (GUI RELATED)
        //Need two separate objects thanks to tiles being individual canvases (GUI RELATED)
        phantomShipsToPlace.add(defaultShipYard.createShip("Minesweeper"));
        phantomShipsToPlace.add(defaultShipYard.createShip("Destroyer"));
        phantomShipsToPlace.add(defaultShipYard.createShip("Battleship"));
        phantomShipsToPlace.add(submergableShipYard.createShip("Submarine"));
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
        if(weapon.getType().equals("Sonar Pulse")){
            removeWeapon("Sonar Pulse");
        }

        List<AttackResult> results = weapon.useAt(opponentBoard, attackCoords);
        weapon.useAt(enemyBoardGui, attackCoords);
        opponentBoard.updateObservers();
        enemyBoardGui.updateObservers();

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
        }
        else{
            return false;
        }
        return true;
    }

    //for opposite player to call after they successfully placed a ship on their own real board (GUI RELATED)
    public void placeEnemyShip(PlayerShipInput input){
        Orientation direction = Orientation.down;
        double x =  Double.parseDouble(input.getxCord());
        double y =  Double.parseDouble(input.getyCord());
        Point3D origin = new Point3D(x, y, 0);
        Ship newShip = new Destroyer();

        String shipChoice = input.getShipChoice();
        String orientationChoice = input.getDirection();

        for(Ship ship : this.getPhantomShipsToPlace()){
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

        if(getEnemyBoardGui().placeShip(direction, origin, newShip)){
            //add mines
            //checkMines();
        }
    }




    public void checkMines(){
        if(shipsToPlace.isEmpty()){
            mines = MineCollection.AVAILABLE;
        }
        if(mines == MineCollection.AVAILABLE){
            placeMines();
        }
    }

    public void placeMines(){
        Random random = new Random();
        for(int x = 0; x < 4; x++) {
            int i = random.nextInt(9) + 1;
            int j = random.nextInt(9) + 1;
            Tile oldTile = board.tiles[i][j][0];
            if (oldTile instanceof SeaTile){
                Tile mineTile = new MineTile(i,j,0);//START HERE
                oldTile = mineTile;
                //re-register that spot with the renderer
                //renderer.unregister(oldTile);
                //renderer.register(mineTile);
            }
            else{
                //ship was placed at the spot
            }
        }
    }


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
        board.updateObservers();
        return true;
    }

    public Board getBoard() {
        return board;
    }

    public Board getEnemyBoardGui() {
        return enemyBoardGui;
    }

    public List<Ship> getShipsToPlace() {
        return shipsToPlace;
    }

    public List<Ship> getPhantomShipsToPlace() {
        return phantomShipsToPlace;
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

    public void removeWeapon(String weaponToRemove){
        Weapon weaponToDelete = new SmallWeapon(new Attack(),Game.SINGLE_SHOT);
        for(Weapon weapon : this.weapons){
            if(weapon.getType().equals(weaponToRemove)){
                weaponToDelete = weapon;
            }
        }
        this.getWeapons().remove(weaponToDelete);
    }

    public void removeShipToPlace(Ship ship){
        this.shipsToPlace.remove(ship);
    }
}
