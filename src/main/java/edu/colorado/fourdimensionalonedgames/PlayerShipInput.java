package edu.colorado.fourdimensionalonedgames;

public class PlayerShipInput {
    private String direction;
    private String shipChoice;
    private double xCord;
    private double yCord;


    public PlayerShipInput(String dir, String shipC, String x, String y) {
        this.direction = dir;
        this.shipChoice = shipC;
        this.xCord = Double.parseDouble(x);
        this.yCord = Double.parseDouble(y);
    }

    public String getDirection() {
        return direction;
    }

    public String getShipChoice() {
        return shipChoice;
    }

    public double getxCord() {
        return xCord;
    }

    public double getyCord() {
        return yCord;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setShipChoice(String shipChoice) {
        this.shipChoice = shipChoice;
    }

    public void setxCord(double xCord) {
        this.xCord = xCord;
    }

    public void setyCord(double yCord) {
        this.yCord = yCord;
    }
}
