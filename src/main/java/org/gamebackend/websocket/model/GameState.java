package org.gamebackend.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
    private String gameId;
    private char turn;
    private char[][] board;
    private char endGame;
}