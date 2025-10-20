package org.gamebackend.connect4.service;

import org.gamebackend.connect4.domain.Connect4Game;
import org.gamebackend.websocket.model.GameMove;
import org.gamebackend.websocket.model.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Connect4Service {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    static List<Connect4Game> gameList = new ArrayList<>();
    public List<String> findOrCreateGame(String userName) {
        List<String> gameAttributeList = new ArrayList<>();
        if(gameList.isEmpty() || gameList.getLast().getPlayer2() == null) {
            gameList.add(new Connect4Game(userName, gameList.size()+1));
            gameAttributeList.add(gameList.getLast().getGameId());
            gameAttributeList.add("p1");
            return gameAttributeList;
        }
        Connect4Game game = gameList.getLast();
        game.setPlayer2(userName);

        gameAttributeList.add(game.getGameId());
        gameAttributeList.add("p2");

        String destination = "/game_backend/" + game.getGameId();
        messagingTemplate.convertAndSend(destination, game.getGameState());
        gameAttributeList.add(String.valueOf(game.getGameState().getTurn()));
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
