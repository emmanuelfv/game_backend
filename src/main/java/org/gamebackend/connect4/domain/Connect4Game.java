package org.gamebackend.connect4.domain;

import java.text.DecimalFormat;

public class Connect4Game {
    public String player1;
    public String player2;
    public boolean playersSet = false;
    public String gameId;

    public Connect4Game(String player1, int gameInt) {
        this.player1 = player1;
        this.gameId = "connect4_" + new DecimalFormat("0000").format(gameInt);
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
        this.playersSet = true;
    }

}
