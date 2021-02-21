package edu.colorado.fourdimensionalonedgames;

public class PlayerFireInput {
    private String weaponChoice;
    private double xCord;
    private double yCord;


    public PlayerFireInput(String weaponChoice, String x, String y) {
        this.weaponChoice = weaponChoice;
        this.xCord = Double.parseDouble(x);
        this.yCord = Double.parseDouble(y);

    }
    //get
    public String getWeaponChoice() {
        return weaponChoice;
    }

    public double getxCord() {
        return xCord;
    }

    public double getyCord() {
        return yCord;
    }

    public void setWeaponChoice(String weaponChoice) {
        this.weaponChoice = weaponChoice;
    }

    public void setxCord(double xCord) {
        this.xCord = xCord;
    }

    public void setyCord(double yCord) {
        this.yCord = yCord;
    }
}