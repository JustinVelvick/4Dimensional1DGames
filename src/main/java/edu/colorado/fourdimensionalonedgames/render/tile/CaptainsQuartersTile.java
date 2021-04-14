package edu.colorado.fourdimensionalonedgames.render.tile;
import edu.colorado.fourdimensionalonedgames.game.ship.Ship;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CaptainsQuartersTile extends ShipTile{

    private int startingHP;
    private int hp;
    public CaptainsQuartersTile(Ship parentShip, int column, int row, int hp) {
        super(parentShip, column, row);
        this.hp = hp;
        this.startingHP = hp;
    }

    public CaptainsQuartersTile(Ship parentShip, int column, int row, int depth, int hp) {
        super(parentShip, column, row, depth);
        this.hp = hp;
        this.startingHP = hp;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor());
        Rectangle rect = new Rectangle(40,40);
        gc.fillRect(0, 0, rect.getWidth(), rect.getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeOval(5,5,30,30);
        gc.strokeOval(10,10,20,20);
    }

    @Override
    public Color getColor() {
        if (hp < startingHP) {
            return Color.ORANGE;
        } else {
            return parentShip.getColor();
        }
    }

    public int getHp() {
        return hp;
    }
    public void damage(){
        hp-=1;
    }
}
