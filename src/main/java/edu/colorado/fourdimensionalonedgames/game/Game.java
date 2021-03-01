package edu.colorado.fourdimensionalonedgames.game;

import edu.colorado.fourdimensionalonedgames.MainSceneController;
import edu.colorado.fourdimensionalonedgames.render.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private boolean player1Turn;
    private List<Player> players = new ArrayList<>();
    private Render renderer;


    //Game constructor which creates the Player objects
    /**
     * @param renderer (One renderer to contain all IRenderable objects)
     * @param numberofPlayers (number of players in the game)
     * @param speed (frequency of gui refresh)
     * @param tileSize (size of each tile in pixels
     * @param columns (number of columns on a single battleships board)
     * @param rows (number of rows on a single battleships board)
     */
    public Game(Render renderer, int numberofPlayers, int speed, int tileSize, int columns, int rows){
        this.renderer = renderer;


        for(int i = 0; i < numberofPlayers; i++){
            players.add(new Player(new Board(columns, rows, this.renderer)));
        }




    }

    public List<Player> getPlayers() {
        return players;
    }

    public Render getRenderer() {
        return renderer;
    }
}
