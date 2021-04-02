package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.render.tile.Tile;

//grid panes
public interface Observer {
    void update(Tile[][] tiles);
}
