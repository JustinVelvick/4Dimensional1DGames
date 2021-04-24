package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MenuSceneController implements Initializable {

    private Game game;
    @FXML
    private Button startGameButton;

    //initializes visual side of the program
    public void initialize(Game game) {
        this.game = game;
    }

    @FXML
    public void handleStartGameButton(ActionEvent event){
        game.passSetupTurn();
    }

    //Javafx calls this method upon form creation
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

