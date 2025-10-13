package org.gamebackend.websocket.controler;

import lombok.extern.slf4j.Slf4j;
import org.gamebackend.connect4.service.Connect4Service;
import org.gamebackend.login.model.UserModel;
import org.gamebackend.login.service.TokenService;
import org.gamebackend.websocket.model.GameLogin;
import org.gamebackend.websocket.model.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class GameWebSocketController {
    private Map<String, UserModel> activeGames = new ConcurrentHashMap<>();
    // broker: game_backend , destination_prefix: connect4, endpoint: ws_game, mapping: move, sendTo: game_backend/move
    // publish destination /connect4/move, brokerurl url/ws_game, client subscribe to /game_backend/move

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Connect4Service connect4Service;


    @MessageMapping("/join")
    @SendTo("/game_backend/join")
    public GameLogin handleJoin(GameLogin login) {
        log.info("in handleMove login: {}", login);
        String validation = tokenService.validateToken(login.getToken());
        if(!validation.equals("validated")) {
            login.setErrorMessage(validation);
            return login;
        }

        String creationStatus = connect4Service.findOrCreateGame(login.getUserName());
        login.setGameId(creationStatus.substring(4,8));
        if(creationStatus.startsWith("new")){
            login.setPlayerId("p1");
        } else {
            login.setPlayerId("p2");
        }

        return login;
    }
    /*
    @MessageMapping("/move")
    @SendTo("/game_backend/{gameId}")
    public GameMove handleMove(@DestinationVariable String gameId, GameMove move) {
        log.info("in handleMove gameId: {}, move: {}", gameId, move);
        return move;
    }*/

    @MessageMapping("/move")
    @SendTo("/game_backend/move")
    public GameStatus handleMove(GameStatus gameStatus) {
        log.info("in handleMove gameStauts: {}", gameStatus);
        return new GameStatus(gameStatus.getMessage() + "+1");
    }
}