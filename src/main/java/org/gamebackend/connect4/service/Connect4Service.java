package org.gamebackend.connect4.service;

import org.gamebackend.common.agents.TwoPlayerAgent;
import org.gamebackend.connect4.agents.connect4RandomAgent;
import org.gamebackend.connect4.domain.Connect4Game;
import org.gamebackend.websocket.model.GameMove;
import org.gamebackend.websocket.model.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Connect4Service {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    static List<Connect4Game> gameList = new ArrayList<>();
    static Map<Connect4Game, TwoPlayerAgent> agentMap = new HashMap<>();

    public Map<String,String> findOrCreateGame(String userName, String gameType) {
        if ("connect4_one_player".equals(gameType)) {
            return createOnePlayerGame(userName);
        } else {
            return findOrCreateTwoPlayerGame(userName);
        }
    }

    private Map<String, String> createOnePlayerGame(String userName){
        Connect4Game game = new Connect4Game();

        gameList.add(game);
        game.setGameId(gameList.size());
        game.setPlayer1(userName);

        //TODO: add factory
        connect4RandomAgent agent = new connect4RandomAgent(userName);
        agentMap.put(game, agent);
        game.setPlayer2(agent.getAgentName());

        Map<String, String> gameAttributeList = new HashMap<>();
        gameAttributeList.put("gameId", game.getGameId());
        gameAttributeList.put("playerId", "p1");
        gameAttributeList.put("turn", String.valueOf(game.getGameState().getTurn()));
        return gameAttributeList;
    }

    private Map<String,String> findOrCreateTwoPlayerGame(String userName){
        Map<String,String> gameAttributes = new HashMap<>();
        Connect4Game game = getAvailableGame();

        if(game == null){
            game = new Connect4Game();
            gameList.add(game);
            game.setGameId(gameList.size());
            game.setPlayer1(userName);
            gameAttributes.put("playerId","p1");
        } else {
            gameAttributes.put("playerId","p2");
            String destination = "/game_backend/" + game.getGameId();
            messagingTemplate.convertAndSend(destination, game.getGameState());
            gameAttributes.put("turn", String.valueOf(game.getGameState().getTurn()));
        }
        gameAttributes.put("gameId", game.getGameId());

        return gameAttributes;
    }

    private Connect4Game getAvailableGame() {
        for(Connect4Game game: gameList){
            if(game.getPlayer2() == null) return game;
        }
        return null;
    }

    public GameState addMove(GameMove move) {
        Connect4Game game = gameList.stream()
                .filter(x -> x.getGameId().equals(move.getGameId()))
                .findFirst().orElse(null);
        if(game == null) return null;

        if(move.getValue().equals("START")) return game.getGameState();
        if(move.getValue().equals("AGENT_MOVE")) {
            connect4RandomAgent agent = (connect4RandomAgent) agentMap.get(game);
            agent.setGameState(game.getGameState());
            return game.playMove(agent.makeMove(), agent.getAgentName());
        }

        return game.playMove(Integer.parseInt(move.getValue()), move.getPlayerId());
    }
}
