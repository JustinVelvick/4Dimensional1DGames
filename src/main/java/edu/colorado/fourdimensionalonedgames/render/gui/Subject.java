package edu.colorado.fourdimensionalonedgames.render.gui;

//boards
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void updateObservers();
    void updateLocalObservers();
}
