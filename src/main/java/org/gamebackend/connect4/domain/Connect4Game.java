package org.gamebackend.connect4.domain;

import lombok.Getter;
import org.gamebackend.websocket.model.GameState;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Connect4Game {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY_SLOT = '_';
    private static final char DRAW = 'd';

    @Getter
    private final String gameId;
    @Getter
    private final String player1;
    @Getter
    private String player2;

    private char[][] board;
    private boolean turn;
    private Character endGame;
    private int[] upperBound;

    public Connect4Game(String player1, int gameInt) {
        this.player1 = player1;
        this.gameId = "connect4_" + new DecimalFormat("0000").format(gameInt);
        this.endGame = EMPTY_SLOT;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
        this.matchStart();
    }

    public GameState playMove(int column, String player){
        if (endGame != null  ||
                player2 == null ||
                this.upperBound[column] == 0 ||
                column >= COLS ) {
            return getGameState();
        }
        int row = this.upperBound[column];
        this.board[row][column] = turn ? 'o' : 'x';
        this.upperBound[column]--;
        turn = !turn;
        endGame = checkEndGame(turn ? 'o' : 'x');
        return getGameState();
    }

    private GameState getGameState() {
        GameState gameState = new GameState();
        gameState.setGameId(this.gameId);
        gameState.setBoard(this.board);
        gameState.setTurn(this.turn ? 'o' : 'x');
        gameState.setEndGame(this.endGame);
        return gameState;
    }

    private void matchStart() {
        this.upperBound = new int[COLS-1];
        this.board = new char[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                this.board[i][j] = EMPTY_SLOT;
            }
        }
    }

    private char checkEndGame(char turn){
        boolean endGame = checkWinner(turn);
        if(endGame) return turn;
        endGame = Arrays.stream(upperBound).sum() == 0; //endGame
        return endGame ? DRAW : EMPTY_SLOT;
    }


    private boolean checkWinner(char playerToken) {
        // Check for 4 horizontal
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (this.board[row][col] == playerToken &&
                        this.board[row][col + 1] == playerToken &&
                        this.board[row][col + 2] == playerToken &&
                        this.board[row][col + 3] == playerToken) {
                    return true;
                }
            }
        }

        // Check for 4 vertical
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if (this.board[row][col] == playerToken &&
                        this.board[row + 1][col] == playerToken &&
                        this.board[row + 2][col] == playerToken &&
                        this.board[row + 3][col] == playerToken) {
                    return true;
                }
            }
        }

        // Check for 4 ascending diagonal (\)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (this.board[row][col] == playerToken &&
                        this.board[row - 1][col + 1] == playerToken &&
                        this.board[row - 2][col + 2] == playerToken &&
                        this.board[row - 3][col + 3] == playerToken) {
                    return true;
                }
            }
        }

        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (this.board[row][col] == playerToken &&
                        this.board[row + 1][col + 1] == playerToken &&
                        this.board[row + 2][col + 2] == playerToken &&
                        this.board[row + 3][col + 3] == playerToken) {
                    return true;
                }
            }
        }

        return false;
    }
}

