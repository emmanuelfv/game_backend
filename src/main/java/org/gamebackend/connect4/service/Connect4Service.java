package org.gamebackend.connect4.service;

import org.gamebackend.connect4.domain.Connect4Game;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Connect4Service {
    static List<Connect4Game> gameList;
    public String findOrCreateGame(String playerName){
        if(gameList.isEmpty() || gameList.getLast().playersSet) {
            gameList.add(new Connect4Game(playerName, gameList.size()+1));
            return "new_" + gameList.getLast().gameId;
        }
        gameList.getLast().setPlayer2(playerName);
        return "old_" + gameList.getLast().gameId;
    }
}
