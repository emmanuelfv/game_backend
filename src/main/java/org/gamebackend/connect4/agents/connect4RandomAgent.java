package org.gamebackend.connect4.agents;

import lombok.Getter;
import org.gamebackend.common.agents.TwoPlayerAgent;
import org.gamebackend.connect4.domain.Connect4Game;
import org.gamebackend.websocket.model.GameState;
import java.util.*;

public class connect4RandomAgent implements TwoPlayerAgent {

    private static final String AGENT_NAME = "random_agent";
    private Connect4Game myGame;

    @Getter
    private String agentName;
    private String gameId;
    private char turn;
    private char[][] board;
    private char endGame;

    public connect4RandomAgent(String userName){
        this.agentName = userName + "_" + AGENT_NAME;
    }

    private int[] getValidMoves(char[][] board){
        List<Integer> l1 = new ArrayList<>();
        for (int i = 0; i < Connect4Game.COLS; i++) {
            if(this.board[i][Connect4Game.ROWS - 1] == Connect4Game.EMPTY_SLOT) {
                l1.add(i);
            }
        }
        return l1.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public void setGameState(GameState gs) {
        this.gameId = gs.getGameId();
        this.turn = gs.getTurn();
        this.board = gs.getBoard();
        this.endGame = gs.getEndGame();
    }

    @Override
    public int makeMove() {
        if (this.endGame != Connect4Game.EMPTY_SLOT) return 0;
        int[] validMoves = this.getValidMoves(this.board);
        return validMoves[new Random().nextInt(validMoves.length)];
    }

}
