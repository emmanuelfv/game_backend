package org.gamebackend.connect4.service;

import org.gamebackend.connect4.domain.Connect4Game;
import org.gamebackend.websocket.model.GameMove;
import org.gamebackend.websocket.model.GameState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Connect4Service {
    static List<Connect4Game> gameList;
    public List<String> findOrCreateGame(String playerName) {
        List<String> gameAttributeList = new ArrayList<>();
        gameAttributeList.add(gameList.getLast().getGameId());
        if(gameList.isEmpty() || gameList.getLast().getPlayer2() == null) {
            gameList.add(new Connect4Game(playerName, gameList.size()+1));
            gameAttributeList.add("p1");
            return gameAttributeList;
        }
        gameList.getLast().setPlayer2(playerName);
        gameAttributeList.add("p2");
        return gameAttributeList;
    }

    public GameState addMove(GameMove move) {
        Connect4Game game = gameList
                .stream()
                .filter(x -> x.getGameId().equals(move.getGameId()))
                .findFirst().orElse(null);
        if(game == null) return new GameState();
        return game.playMove(Integer.parseInt(move.getValue()), move.getPlayerId());
    }
}
