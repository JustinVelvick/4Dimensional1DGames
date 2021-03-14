package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.gui.PlayerFireInput;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


//TODO - Create this popup box in scene builder and then FXML load it here
public class FireFormController implements Initializable {


    private Game game;

    @FXML
    private TextField xCord;

    @FXML
    private TextField yCord;

    @FXML
    private ChoiceBox<String> weaponChoiceBox;

    @FXML
    private Button confirmButton;

    private PlayerFireInput input;


    public void initialize(Game game) {

        //field initialization on form creation
        this.input = new PlayerFireInput("","0","0");
        this.game = game;
    }

    public void populateFireForm(List<Weapon> weapons){
        //Delete existing to create a fresh set of choices
        weaponChoiceBox.getItems().removeAll(weaponChoiceBox.getItems());
        List<String> choices = new ArrayList<>();
        for(Weapon weapon : weapons){
            choices.add(weapon.getType());
        }
        //add all choices to the dropdown
        weaponChoiceBox.getItems().addAll(choices);

        //if there are no choices left, hide the dropdown
        if(weaponChoiceBox.getItems().isEmpty()){
            weaponChoiceBox.hide();
        }
        else{
            //Display the first choice as dropdown default
            weaponChoiceBox.getSelectionModel().select(choices.get(0));
        }
    }

    public PlayerFireInput userInput(){
        return this.input;
    }

    @FXML
    public void handleConfirmButton(ActionEvent event){

        PlayerFireInput tentativeInput = new PlayerFireInput(weaponChoiceBox.getSelectionModel().getSelectedItem(),
                                                                xCord.getText(),
                                                                    yCord.getText());
        if(validateForm(tentativeInput)){
            //Close this window (Stage)
            input = tentativeInput;
            Stage currentStage = (Stage) confirmButton.getScene().getWindow();
            currentStage.close();
        }
        else{
            //TODO - Have a box pop up asking for valid input
        }
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {

    }


    //helper method to validate form before sending a PlayerShipInput object
    public boolean validateForm(PlayerFireInput input) {
        boolean result = false;



        return result;
    }
}
