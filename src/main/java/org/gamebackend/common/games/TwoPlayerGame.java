package org.gamebackend.common.games;

import org.gamebackend.websocket.model.GameState;

public interface TwoPlayerGame {
    public void setGameId(int gameInt);
    public void setPlayer1(String p1);
    public void setPlayer2(String p2);
    public char checkEndGame(char turn);
    public GameState getGameState();
    public GameState playMove(int column, String playerId);
}
