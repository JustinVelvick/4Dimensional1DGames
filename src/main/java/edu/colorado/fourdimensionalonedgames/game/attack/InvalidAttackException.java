package edu.colorado.fourdimensionalonedgames.game.attack;

public class InvalidAttackException extends RuntimeException {

    private String msg;

    public InvalidAttackException(String errorMsg){
        msg = errorMsg;
    }

    public String getErrorMsg() {
        return msg;
    }

}
