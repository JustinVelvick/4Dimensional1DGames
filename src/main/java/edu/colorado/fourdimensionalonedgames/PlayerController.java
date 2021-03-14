package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.AttackResult;
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
import java.util.ArrayList;
import java.util.List;
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
        //open a new fireForm and get results from the form stored as a PlayerFireInput object
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fireForm.fxml"));
        Pane root = loader.load();

        FireFormController formController = loader.getController();
        //populate weapons list
        formController.initialize(this.game);
        formController.populateFireForm(this.player.getWeapons());

        //object that will store the user's input
        PlayerFireInput userInput = formController.userInput();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Fire Weapon Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();

        //fire weapon at enemy's actual board
        player.attack(enemyPlayer.getBoard(), userInput);
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

        //place a ship down on player1's actual board
        if(!this.player.placeShip(input)){
            //ship placement did not succeed
        }

        else{
            player.updateVisuals();
        }
    }

    public void handlePassTurnButton(ActionEvent e){
        //Turn is over when button is pressed
        this.game.passTurn();
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {


    }

    public GridPane getPlayergpane() {
        return playergpane;
    }

    public GridPane getEnemygpane() {
        return enemygpane;
    }
}
