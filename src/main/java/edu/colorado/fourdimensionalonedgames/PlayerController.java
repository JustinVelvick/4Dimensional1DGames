package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.Destroyer;
import edu.colorado.fourdimensionalonedgames.game.ship.Orientation;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.gui.AlertBox;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PlayerController implements Initializable {


    private Game game;
    private Player player;
    private Player enemyPlayer;

    @FXML
    private GridPane playergpane;

    @FXML
    private GridPane enemygpane;

    @FXML
    private Button placeShipButton;

    @FXML
    private Button passTurnButton;

    @FXML
    private Button fireWeaponButton;


    //We call this at the creation of any new Player1Scene
    //The controller needs the game class in order to inform it that the pass turn button has been pressed
    //PARAMS: game class, player that owns this controller, enemy player of this controller
    public void initialize(Game game, Player thisPlayer, Player enemyPlayer) {
        this.game = game;
        this.player = thisPlayer;
        this.enemyPlayer = enemyPlayer;
    }

    @FXML
    public void handleFireWeaponButton(ActionEvent event) throws IOException {
        //not sure if this line of code is correct, or if there's an existing controller object to grab
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fireForm.fxml"));
        Pane root = loader.load();

        FireFormController formController = loader.getController();
        formController.initialize(this.game);

        //populate weapons list
        formController.populateFireForm(this.player.getWeapons());
        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        PlayerFireInput userInput = formController.userInput();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Fire Weapon Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        //fire weapon at enemy's actual board, but also "attack" your own enemy board to mirror
        fireWeapon(userInput, enemyPlayer.getBoard(), player.getEnemyBoard());
    }

    public void fireWeapon(PlayerFireInput input, Board board, Board enemyBoard) {
        Point2D coordinate = new Point2D(input.getxCord(), input.getyCord());

        int x = (int) input.getyCord();
        int y = (int) input.getyCord();

        try {
            //your enemy's real board and your own view of their board
            Ship attackedShip = enemyPlayer.getBoard().attack(coordinate);
            player.getEnemyBoard().attack(coordinate);

            if (attackedShip == null) {
                AlertBox.display("Miss", "Shot missed");

            }
            else{
                this.game.updateEnemyGpane(enemyPlayer.getBoard(), getEnemygpane());
                if (attackedShip.destroyed()) {
                    AlertBox.display("Ship Sunk", "The enemy's " + attackedShip.getType() + " has been sunk!");
                }
                else {
                    AlertBox.display("Ship Hit", "Ship has been hit");
                }

                if(board.gameOver()) {
                    AlertBox.display("Enemy Surrender!", "Congratulations, you sunk all of your enemies ships!");
                }
            }


        }
        catch (InvalidAttackException e) {
            AlertBox.display("Invalid Coordinates", e.getErrorMsg());
        }
    }

    //spawn a ShipChoiceForm, populate it's fields, and retrieve user input from ShipChoiceForm when it closes
    public void handlePlaceShipButton(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("shipChoiceForm.fxml"));
        Pane root = loader.load();

        ShipChoiceFormController formController = loader.getController();

        //initialize form fields with updated list of ships in player's shipsToPlace
        formController.populateShipForm(this.player.getShipsToPlace());

        //open a new shipChoiceForm and get results from the form stored as a PlayerShipInput object
        PlayerShipInput userInput = formController.display();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Ship Placement Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        placeShip(userInput);
    }

    public void placeShip(PlayerShipInput input) {
        Orientation direction = Orientation.down;
        double x =  input.getxCord();
        double y =  input.getyCord();
        Point2D origin = new Point2D(x, y);

        Ship newShip = new Destroyer();

        String shipChoice = input.getShipChoice();
        String orientationChoice = input.getDirection();


        for(Ship ship : this.player.getShipsToPlace()){
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

        //place a ship down on player1's actual board
        if(!this.player.placeShip(this.getPlayergpane(), direction, origin, newShip)){
            //ship placement did not succeed
        }

        //place the same ship down on player2's enemy board if placement succeeded
        enemyPlayer.getEnemyBoard().placeShip(this.getPlayergpane(), direction, origin, newShip);

    }

    public void handlePassTurnButton(ActionEvent e){
        //Turn is over when button is pressed
        this.game.passTurn();
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {


    }


    //helper method to validate form before populating a PlayerShipInput object
    public boolean validateForm() {
        Boolean result = true;



        return result;
    }

    public GridPane getPlayergpane() {
        return playergpane;
    }

    public GridPane getEnemygpane() {
        return enemygpane;
    }
}
