package org.gamebackend.connect4.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.gamebackend.common.games.TwoPlayerGame;
import org.gamebackend.websocket.model.GameState;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

@Slf4j
public class Connect4Game implements TwoPlayerGame {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final char EMPTY_SLOT = '_';
    public static final char DRAW = 'd';

    @Getter
    private String gameId;
    @Getter
    @Setter
    private String player1;
    @Getter
    private String player2;

    private char[][] board;
    private char turn;
    private Character endGame;
    private int[] upperBound;

    private char getPlayerTurn(String playerId) {
        if (playerId.equals("p1")) return 'x';
        else if (playerId.equals("p2")) return 'o';
        return EMPTY_SLOT;
    }

    public Connect4Game() {
        this.endGame = EMPTY_SLOT;
    }

    @Override
    public void setGameId(int gameInt) {
        this.gameId = "connect4_" + new DecimalFormat("0000").format(gameInt);
    }

    @Override
    public void setPlayer2(String userName) {
        this.player2 = userName;
        this.matchStart();
    }

    @Override
    public GameState playMove(int column, String playerId){
        log.info("in playmove with col {} and player {}", column, playerId);
        if (endGame != EMPTY_SLOT  ||
                player2 == null ||
                turn != getPlayerTurn(playerId) ||
                this.upperBound[column] >= 6 ||
                column >= COLS ) {
            return getGameState();
        }
        int row = this.upperBound[column]++;
        this.board[column][row] = turn;
        turn = turn == 'x' ? 'o' : 'x';
        endGame = checkEndGame(turn);
        return getGameState();
    }

    @Override
    public GameState getGameState() {
        GameState gameState = new GameState();
        gameState.setGameId(this.gameId);
        gameState.setBoard(this.board);
        gameState.setTurn(this.turn);
        gameState.setEndGame(this.endGame);
        return gameState;
    }

    private void matchStart() {
        this.upperBound = new int[COLS];
        Arrays.fill(this.upperBound, 0);
        this.board = new char[COLS][ROWS];
        for (int i = 0; i < COLS; i++) {
            Arrays.fill(this.board[i], EMPTY_SLOT);
        }
        this.turn = 'x'; // TODO: new Random().nextBoolean() ? 'x' : 'o';
    }

    @Override
    public char checkEndGame(char turn){
        boolean endGame = checkWinner(turn);
        if(endGame) return turn;
        endGame = Arrays.stream(upperBound).sum() == (ROWS - 1) * COLS; //endGame
        return endGame ? DRAW : EMPTY_SLOT;
    }


    private boolean checkWinner(char playerToken) {
        // Check for 4 horizontal
        for (int col = 0; col < COLS - 3; col++) {
            for (int row = 0; row < ROWS; row++) {
                if (this.board[col][row] == playerToken &&
                        this.board[col + 1][row] == playerToken &&
                        this.board[col + 2][row] == playerToken &&
                        this.board[col + 3][row] == playerToken) {
                    return true;
                }
            }
        }

        // Check for 4 vertical
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS; col++) {
                if (this.board[col][row] == playerToken &&
                        this.board[col][row + 1] == playerToken &&
                        this.board[col][row + 2] == playerToken &&
                        this.board[col][row + 3] == playerToken) {
                    return true;
                }
            }
        }

        // Check for 4 ascending diagonal (\)
        for (int col = 0; col < COLS - 3; col++) {
            for (int row = 3; row < ROWS; row++) {
                if (this.board[col][row] == playerToken &&
                        this.board[col + 1][row - 1] == playerToken &&
                        this.board[col + 2][row - 2] == playerToken &&
                        this.board[col + 3][row - 3] == playerToken) {
                    return true;
                }
            }
        }

        for (int col = 0; col < COLS - 3; col++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if (this.board[col][row] == playerToken &&
                        this.board[col + 1][row + 1] == playerToken &&
                        this.board[col + 2][row + 2] == playerToken &&
                        this.board[col + 3][row + 3] == playerToken) {
                    return true;
                }
            }
        }

        return false;
    }
}

