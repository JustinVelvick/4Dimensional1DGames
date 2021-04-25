package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
import edu.colorado.fourdimensionalonedgames.game.attack.behavior.Attack;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.MineCollection;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.PowerUpsCollection;
import edu.colorado.fourdimensionalonedgames.game.attack.upgrades.TierOneUpgrade;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.SmallWeapon;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.gui.AlertBox;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import edu.colorado.fourdimensionalonedgames.render.tile.*;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Player class deals with all player related actions and data, including placing ships onto their Board, attacking
 * the enemy board, and moving the fleet. Player tracks user related things as well like ign, victories, score, etc.
 * For this reason, the Player class CAN exist without game, but more practically we would export these fields to a
 * database on game close, and reload them into a new player object once that same user comes back.
 */
public class Player {
    //account specific info
    private String ign;
    private int victories;
    private int score;
    private int missedShots;
    private int totalShots;
    //core player fields
    private final Game game;
    private final Board board;
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Ship> shipsToPlace = new ArrayList<>();
    private final Fleet fleet;
    private FleetControl fleetController;
    //enums that keep track of one time events in the game
    private TierOneUpgrade upgradeStatus; //unlocks 2 sonar pulses and replaces single shot with space laser at int = 0
    private MineCollection mines;
    private PowerUpsCollection powerUps;

    public Player(Game game, Board board) {
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

    private void generateShips() {
        ShipYard defaultShipYard = new DefaultShipYard();
        ShipYard submergableShipYard = new SubmergableShipYard();

        shipsToPlace.add(defaultShipYard.createShip("Minesweeper"));
        shipsToPlace.add(defaultShipYard.createShip("Destroyer"));
        shipsToPlace.add(defaultShipYard.createShip("Battleship"));
        shipsToPlace.add(submergableShipYard.createShip("Submarine"));
    }

    //creates starting weapons for the players, as of now that is just SingleShot
    private void generateWeapons() {
        weapons.add(new SmallWeapon(new Attack(), Game.SINGLE_SHOT));
    }

    /**
     * Mount an attack on the given enemy board at attackCoords with weaponChoice
     * NOTE: PLAYER INPUT SHOULD BE VALIDATED AND FILTERED BY THIS POINT
     *
     * @return - list of resulting AttackResult objects in the form of {AttackResultType enum, Ship(if applicable)}
     */
    public List<AttackResult> attack(Board opponentBoard, PlayerFireInput input) {

        Weapon weapon = stringToWeapon(input.getWeaponChoice());
        double x = Double.parseDouble(input.getxCord());
        double y = Double.parseDouble(input.getyCord());

        Point2D attackCoords = new Point2D(x, y);

        //if a finite weapon is being used, decrement uses. If decrement uses becomes zero, remove from player's weapons
        if (weapon.doRemove()) {
            this.weapons.remove(weapon);
        }

        List<AttackResult> results = weapon.useAt(opponentBoard, attackCoords);
        opponentBoard.updateObservers();

        for (AttackResult attackResult : results) {
            Ship attackedShip = attackResult.getShip();

            if (attackedShip == null) {
                //when missed shot
                missedShots++;
            } else if (attackedShip.destroyed()) {
                //when ship has been destroyed
                if (this.upgradeStatus == TierOneUpgrade.LOCKED) {
                    this.upgradeStatus = TierOneUpgrade.UNLOCKED;
                }
            }
        }
        return results;
    }

    //matches weapon that user picked in GUI with those stored in player's list of weapons avaliable
    private Weapon stringToWeapon(String weaponChoice) {
        Weapon returnWeapon = null;

        for (Weapon weapon : weapons) {
            if (weapon.getType().equals(weaponChoice)) {
                returnWeapon = weapon;
            }
        }

        return returnWeapon;
    }

    /**
     * placeShip() in the Player object first converts everything from strings into their appropriate objects,
     * then attempts to place the ship on the board. If this succeeds, the newly placed ship is added to the player's
     * fleet, the player's "shipsToPlace" list is updated and finally Mines and Powerups are placed if that ship was
     * the last one the user had to place down.*
     *
     * @param input: An object that contains all relevant data the user filled out in the GUI form
     * @return: returns true if placement succeeded, false otherwise
     */
    public Boolean placeShip(PlayerShipInput input) {
        Orientation direction = Orientation.down;
        double x = Double.parseDouble(input.getxCord());
        double y = Double.parseDouble(input.getyCord());
        Point3D origin = new Point3D(x, y, 0);
        Ship newShip = new Destroyer();

        String shipChoice = input.getShipChoice();
        String orientationChoice = input.getDirection();

        for (Ship ship : this.getShipsToPlace()) {
            if (shipChoice.equals(ship.getType())) {
                newShip = ship;
            }
        }

        //Set z axis
        if (input.getSubmergeChoice().equals("Yes")) {
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

        if (getBoard().placeShip(direction, origin, newShip)) {
            //add this completed, built ship to the fleet
            fleet.addShip(newShip);
            //Remove shipToPlace from player list of shipsToPlace since placement succeeded
            removeShipToPlace(newShip);
            //add mines
            checkMines();
            checkPowerUps();
        } else {
            return false;
        }
        return true;
    }

    private void checkMines() {
        if (shipsToPlace.isEmpty()) {
            mines = MineCollection.AVAILABLE;
        }
        if (mines == MineCollection.AVAILABLE) {
            mines = MineCollection.PLACED;
            if(!game.isTestMode()){
                board.placeMines();
            }
        }
    }

    //for testing purposes only to give a deterministic spawning of mines
    public void placeTestTile(int i, int j, Tile tile) {
        if (board.tiles[i][j][0] instanceof SeaTile) {
            board.tiles[i][j][0] = tile;
        }
    }

    /**
     * moveFleet() will only move entire fleet if every ship is able to move specified direction
     *
     * @param direction: direction to attempt to move every ship in the fleet towards
     * @return: returns false if no movement was possible
     */
    public boolean moveFleet(Orientation direction) {
        // check for border collision on all ships in the fleet
        if (borderCollision(direction)) {
            return false;
        } else {
            // actually move the fleet now that we confirmed it can be moved
            List<Weapon> weaponsToAdd = new ArrayList<>();
            List<Ship> correctlyOrdered = new ArrayList<>();

            for(Ship ship : fleet.getShips()){
                correctlyOrdered.add(ship);
            }

            switch (direction){
                case down:
                    //sort all average ship positions by largest y axis
                    correctlyOrdered.sort(Comparator.comparingInt((Ship s) -> (int) s.findAveragePostion().getY()).reversed());
                    break;
                case up:
                    //sort all average ship positions by smallest y axis
                    correctlyOrdered.sort(Comparator.comparingInt((Ship s) -> (int) s.findAveragePostion().getY()));
                    break;
                case left:
                    //sort all average ship positions by smallest x axis
                    correctlyOrdered.sort(Comparator.comparingInt((Ship s) -> (int) s.findAveragePostion().getX()));
                    break;
                case right:
                    //sort all average ship positions by largest x axis
                    correctlyOrdered.sort(Comparator.comparingInt((Ship s) -> (int) s.findAveragePostion().getX()).reversed());
                    break;
            }

            for (Ship ship : correctlyOrdered) {
                //dead ships should not move
                if (ship.destroyed()) {
                    continue;
                }
                weaponsToAdd.addAll(board.moveShip(ship, direction));
            }
            //update GUI to show the ships moved
            board.updateLocalObservers();
            //now add all weapons that user may have picked up and display AlertBoxes for each one picked up
            for (Weapon weapon : weaponsToAdd) {
                boolean isInAlreadyExistingWeapons = false;
                for (Weapon weapon2 : weapons) {
                    if (weapon2.getType().equals(weapon.getType())) {
                        isInAlreadyExistingWeapons = true;
                        weapon2.addCount(weapon.getCount());
                        if (!game.isTestMode()) {
                            AlertBox.display("Power Up Acquired", "You just picked up another " + weaponsToAdd.get(0).getType() + "! Use it wisely, you now have " +
                                    weaponsToAdd.get(0).getCount() + " left");
                        }
                    }
                }
                if (!isInAlreadyExistingWeapons) {
                    weapons.add(weapon);
                    if (!game.isTestMode()) {
                        AlertBox.display("Power Up Acquired", "You just picked up a " + weaponsToAdd.get(0).getType() + "! Use it wisely, you only have one!");
                    }
                }
            }
            return true;
        }
    }

    //helper method for moveFleet to see if any ship cannot move (up against an edge of the board in desired direction)
    private boolean borderCollision(Orientation direction) {
        switch (direction) {
            case up:
                for (Ship ship : fleet.getShips()) {
                    if(ship.destroyed()){
                        continue;
                    }
                    for (ShipTile tile : ship.getShipTiles()) {
                        if (tile.getRow() - 1 < 1) {
                            return true;
                        }
                    }
                }
                break;
            case down:
                for (Ship ship : fleet.getShips()) {
                    if(ship.destroyed()){
                        continue;
                    }
                    for (ShipTile tile : ship.getShipTiles()) {
                        if (tile.getRow() + 1 > 10) {
                            return true;
                        }
                    }
                }
                break;
            case right:
                for (Ship ship : fleet.getShips()) {
                    if(ship.destroyed()){
                        continue;
                    }
                    for (ShipTile tile : ship.getShipTiles()) {
                        if (tile.getColumn() + 1 > 10) {
                            return true;
                        }
                    }
                }
                break;
            case left:
                for (Ship ship : fleet.getShips()) {
                    if(ship.destroyed()){
                        continue;
                    }
                    for (ShipTile tile : ship.getShipTiles()) {
                        if (tile.getColumn() - 1 < 1) {
                            return true;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    public void checkPowerUps() {
        if (shipsToPlace.isEmpty()) {
            powerUps = PowerUpsCollection.AVAILABLE;
        }
        if (powerUps == PowerUpsCollection.AVAILABLE) {
            powerUps = PowerUpsCollection.PLACED;
            if(!game.isTestMode()){
                board.placePowerUps();
            }
        }
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

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public TierOneUpgrade getUpgradeStatus() {
        return upgradeStatus;
    }

    public Fleet getFleet() {
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

    public Game getGame() {
        return game;
    }

    public FleetControl getFleetController() {
        return fleetController;
    }

    public void refreshFleetController() {
        fleetController = new FleetControl(this);
    }

    public void setUpgradeStatus(TierOneUpgrade upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
    }

    public void removeShipToPlace(Ship ship) {
        this.shipsToPlace.remove(ship);
    }
}
