package edu.colorado.fourdimensionalonedgames.game.ship;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

//a Submarine is 5 total tiles, with 4 in a straight line and the 5th being directly to the left of the 3rd in said line.
//Submarines are yellow in color with the CC belonging to the 4th tile (last in the straight line of 4 tiles)
//CAN BE SUBMERGED
public class Submarine extends Ship {

    private int depth;

    public Submarine() {
        this.size = 5;
        this.depth = 0;
    }

    @Override
    public List<Point3D> generateCoordinates(Point3D origin, Orientation direction) {

        List<Point3D> coords = new ArrayList<>();
        double originX = origin.getX();
        double originY = origin.getY();
        double originZ = origin.getZ();

        switch (direction) {
            case up:
                //originY-this.size-1 NOTE: the -1 is due to the protruding tile, the for loops below only
                //only create the linear part of the sub (4 big not 5) this.size = 5
                for (double y = originY; y > originY - this.size + 1; y--) {
                    coords.add(new Point3D(originX, y, originZ));
                }
                //now generate the protruding tile to left of 3rd tile in linear row (index 2 in ship tile array)
                coords.add(new Point3D(originX - 1, originY - 2, originZ));
                break;

            case down:
                for (double y = originY; y < originY + this.size - 1; y++) {
                    coords.add(new Point3D(originX, y, originZ));
                }

                coords.add(new Point3D(originX + 1, originY + 2, originZ));
                break;

            case right:
                for (double x = originX; x < originX + this.size - 1; x++) {
                    coords.add(new Point3D(x, originY, originZ));
                }

                coords.add(new Point3D(originX + 2, originY - 1, originZ));
                break;

            case left:
                for (double x = originX; x > originX - this.size + 1; x--) {
                    coords.add(new Point3D(x, originY, originZ));
                }

                coords.add(new Point3D(originX - 2, originY + 1, originZ));
                break;
        }

        return coords;
    }

    @Override
    public Color getBaseColor() {
        return Color.YELLOW;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String getType() {
        return "Submarine";
    }
}