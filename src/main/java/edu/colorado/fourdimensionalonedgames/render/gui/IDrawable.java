package edu.colorado.fourdimensionalonedgames.render.gui;

import javafx.scene.canvas.GraphicsContext;

//All objects that can draw themselves on a JavaFX Canvas
public interface IDrawable {
    void draw(GraphicsContext gc);
}
