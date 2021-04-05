package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.FireFormController;
import edu.colorado.fourdimensionalonedgames.ShipChoiceFormController;
import edu.colorado.fourdimensionalonedgames.game.Board;
import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.InvalidAttackException;
import edu.colorado.fourdimensionalonedgames.game.ship.*;
import edu.colorado.fourdimensionalonedgames.render.gui.AlertBox;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerShipInput;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuSceneController implements Initializable {

    private List<Player> players;
    private Game game;


    @FXML
    private Button startGameButton;



    //initializes visual side of the program
    public void initialize(Game game) {
        this.game = game;

        this.players = game.getPlayers();
    }


    //event handlers

    /*
        On "Place Ship" or "Fire Weapon" button click, load the form and invoke "showAndWait" which waits for
        the form to close itself, ie. when the Confirm button is clicked, the form will call
        handleConfirmButton and close itself, returning what the user had inputed
    */
    @FXML
    public void handleStartGameButton(ActionEvent event){
        game.passSetupTurn();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

