package edu.colorado.fourdimensionalonedgames.render.gui;

public class PlayerFireInput {
    private String weaponChoice;
    private String xCord;
    private String yCord;


    public PlayerFireInput(String weaponChoice, String x, String y) {
        this.weaponChoice = weaponChoice;
        this.xCord = x;
        this.yCord = y;

    }

    //get
    public String getWeaponChoice() {
        return weaponChoice;
    }

    public String getxCord() {
        return xCord;
    }

    public String getyCord() {
        return yCord;
    }

    public void setWeaponChoice(String weaponChoice) {
        this.weaponChoice = weaponChoice;
    }

    public void setxCord(String xCord) {
        this.xCord = xCord;
    }

    public void setyCord(String yCord) {
        this.yCord = yCord;
    }
}