package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.GameState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

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

    //user(s) want to play again, close this end game form and begin a new game
    public void handleYesButton(ActionEvent actionEvent) {
        game.setGameState(GameState.game_over);
        Stage currentStage = (Stage) noButton.getScene().getWindow();
        currentStage.close();
        game.startNewGame();
    }

    //user(s) do not want to play again, close the application
    public void handleNoButton(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
