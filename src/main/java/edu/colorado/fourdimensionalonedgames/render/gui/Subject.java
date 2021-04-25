package edu.colorado.fourdimensionalonedgames.render.gui;

//Subject part of the Observer-Subject design pattern
//Boards are the Subjects, and Displays/EnemyDisplays are the Observers waiting for updates from the boards
public interface Subject {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void updateObservers();

    void updateLocalObservers();
}
