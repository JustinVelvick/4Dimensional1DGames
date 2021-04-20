package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.GameState;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


//TODO - Create this popup box in scene builder and then FXML load it here
public class EndCreditsController implements Initializable {


    private Game game;


    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    public void initialize(Game game) {
        //field initialization on form creation
        this.game = game;
    }



    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleYesButton(ActionEvent actionEvent) {
        game.setGameState(GameState.game_over);
        Stage currentStage = (Stage) noButton.getScene().getWindow();
        currentStage.close();
        game.startNewGame();
    }

    public void handleNoButton(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
