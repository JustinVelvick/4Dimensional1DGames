package edu.colorado.fourdimensionalonedgames;

import edu.colorado.fourdimensionalonedgames.game.Game;
import edu.colorado.fourdimensionalonedgames.game.Player;
import edu.colorado.fourdimensionalonedgames.game.attack.weapon.Weapon;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import edu.colorado.fourdimensionalonedgames.render.gui.AlertBox;
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
        this.input = new PlayerFireInput("", "0", "0");
        this.game = game;
    }

    public void populateFireForm(List<Weapon> weapons) {
        //Delete existing to create a fresh set of choices
        weaponChoiceBox.getItems().removeAll(weaponChoiceBox.getItems());
        List<String> choices = new ArrayList<>();
        for (Weapon weapon : weapons) {
            choices.add(weapon.getType());
        }
        //add all choices to the dropdown
        weaponChoiceBox.getItems().addAll(choices);

        //if there are no choices left, hide the dropdown
        if (weaponChoiceBox.getItems().isEmpty()) {
            weaponChoiceBox.hide();
        } else {
            //Display the first choice as dropdown default
            weaponChoiceBox.getSelectionModel().select(choices.get(0));
        }
    }

    public PlayerFireInput userInput() {
        return this.input;
    }

    @FXML
    public void handleConfirmButton(ActionEvent event) {

        PlayerFireInput tentativeInput = new PlayerFireInput(weaponChoiceBox.getSelectionModel().getSelectedItem(),
                xCord.getText(),
                yCord.getText());
        if (validateForm(tentativeInput)) {
            //Close this window (Stage)
            input = tentativeInput;
            Stage currentStage = (Stage) confirmButton.getScene().getWindow();
            currentStage.close();
        } else {
            AlertBox.display("Invalid Input!", "Please enter valid coordinates for weapon launch.");
        }
    }

    //JavaFX calls this at the creation of any new form
    public void initialize(URL location, ResourceBundle resources) {

    }


    //helper method to validate form before sending a PlayerShipInput object
    //NOTE: Since weapons are selected from dropdown populated by player object, no need to check for bad weapon input
    public boolean validateForm(PlayerFireInput input) {
        //for x, values entered are either a-j or A-J, use ascii table values to check

        //len of x cord input should be 1 (aka a letter between a-j)
        //len of y cord input should be 1 or 2 (number 1-10 including 1 and 10)
        if ((input.getxCord().length() != 1) || (input.getyCord().length() > 2) || (input.getyCord().length() < 1)) {
            return false;
        }

        boolean xValid = false;
        int xAscii = input.getxCord().charAt(0);

        //upper case A-J check
        if (xAscii > 64 && xAscii < 75) {
            xValid = true;
            xAscii = xAscii - 64;
            input.setxCord(String.valueOf(xAscii));
        }
        //lower case a-j check
        else if (xAscii > 96 && xAscii < 107) {
            xValid = true;
            xAscii = xAscii - 96;
            input.setxCord(String.valueOf(xAscii));
        }

        //y values can be integers [1,10]
        boolean yValid = false;
        int y = Integer.parseInt(input.getyCord());
        if (y > 0 && y < 11) {
            yValid = true;
        }

        return xValid && yValid;
    }
}
