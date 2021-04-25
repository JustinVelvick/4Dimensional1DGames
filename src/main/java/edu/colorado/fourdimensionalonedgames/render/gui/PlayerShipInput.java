package edu.colorado.fourdimensionalonedgames.render.gui;

//Nice little class to hold all data that a player will fill out in shipChoiceForm.fxml
public class PlayerShipInput {
    private String direction;
    private String shipChoice;
    private String submergeChoice;
    private String xCord;
    private String yCord;


    //default constructor
    public PlayerShipInput() {
        this.direction = "Default";
        this.submergeChoice = "N/A";
        this.shipChoice = "Default";
        this.xCord = "Default";
        this.yCord = "Default";
    }

    //constructor for surface ships (no submerge choice)
    public PlayerShipInput(String dir, String shipC, String x, String y) {
        this.direction = dir;
        this.shipChoice = shipC;
        this.submergeChoice = "N/A";
        this.xCord = x;
        this.yCord = y;
    }

    //constructor specifying submerge choice (for submersible ships)
    public PlayerShipInput(String dir, String shipC, String submergeChoice, String x, String y) {
        this.direction = dir;
        this.shipChoice = shipC;
        this.submergeChoice = submergeChoice;
        this.xCord = x;
        this.yCord = y;
    }

    public String getDirection() {
        return direction;
    }

    public String getShipChoice() {
        return shipChoice;
    }

    //Converts letter x coordinate into the matching numerical coordinate 1-10, then back into a string to mirror yCord
    public String getxCord() {
        return xCord;
    }

    public String getyCord() {
        return yCord;
    }

    public String getSubmergeChoice() {
        return submergeChoice;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setShipChoice(String shipChoice) {
        this.shipChoice = shipChoice;
    }

    public void setxCord(String xCord) {
        this.xCord = xCord;
    }

    public void setyCord(String yCord) {
        this.yCord = yCord;
    }

    public void setSubmergeChoice(String submergeChoice) {
        this.submergeChoice = submergeChoice;
    }
}
