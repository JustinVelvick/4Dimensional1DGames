package edu.colorado.fourdimensionalonedgames;


import java.util.ArrayList;
import java.util.List;

//Keeps track of sprites, etc to render onto the canvas
public class Render {
    List<IRenderable> renderRegistry = new ArrayList<>();



    public Render() {

    }

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
