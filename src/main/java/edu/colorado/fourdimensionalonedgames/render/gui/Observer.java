package edu.colorado.fourdimensionalonedgames.render.gui;

import edu.colorado.fourdimensionalonedgames.render.tile.Tile;

//Observer part of the Observer-Subject design pattern
//Boards are the Subjects, and Displays/EnemyDisplays are the Observers waiting for updates from the boards
public interface Observer {
    void update(Tile[][][] tiles);
}
