package edu.colorado.fourdimensionalonedgames.render;

import java.util.ArrayList;
import java.util.List;

//Keeps track of sprites, etc to render onto each Canvas
public class Render {

    List<IRenderable> renderRegistry = new ArrayList<>();

    public Render() {

    }

    //All render-able elements registered will have their render method called (object will be displayed on the screen)
    public void tick() {
        for (IRenderable element : renderRegistry) {
            element.render();
        }
    }

    public void register(IRenderable element) {
        renderRegistry.add(element);
    }

    public void unregister(IRenderable element) {
        renderRegistry.remove(element);
    }
}
