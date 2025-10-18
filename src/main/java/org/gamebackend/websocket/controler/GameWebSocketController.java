package org.gamebackend.websocket.controler;

import lombok.extern.slf4j.Slf4j;
import org.gamebackend.connect4.service.Connect4Service;
import org.gamebackend.login.model.UserModel;
import org.gamebackend.login.service.TokenService;
import org.gamebackend.websocket.model.GameLogin;
import org.gamebackend.websocket.model.GameMove;
import org.gamebackend.websocket.model.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
public class GameWebSocketController {
    // broker: game_backend , destination_prefix: connect4, endpoint: ws_game, mapping: move, sendTo: game_backend/move
    // publish destination /connect4/move, brokerUrl url/ws_game, client subscribe to /game_backend/move

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Connect4Service connect4Service;


    @MessageMapping("/join")
    @SendTo("/game_backend/join")
    public GameLogin handleJoin(GameLogin login) {
        log.debug("in handleMove login: {}", login);
        /*
        if(login.getToken().isEmpty()) {
            try {
                UserModel user = new UserModel();
                user.setName(login.getUserName());
                user.setPassword(login.getPassword(), "login");
                login.setToken(tokenService.provideToken(user));
            } catch (Exception e) {
                log.warn("login failed for user {} with exception {}",
                        login.getUserName(), e.getMessage());
                login.setErrorMessage(e.toString());
                return login;
            }

        }*/

        String validation = tokenService.validateToken(login.getPlayerId());
        if(!validation.equals("validated")) {
            login.setErrorMessage(validation);
            return login;
        }
        List<String> creationStatus = connect4Service.findOrCreateGame(login.getPlayerId());
        login.setGameId(creationStatus.get(0));
        login.setPlayerId(creationStatus.get(1));
        login.setTurn(creationStatus.get(2));
        return login;
    }

    @MessageMapping("/move")
    @SendTo("/game_backend/{gameId}")
    public GameState handleMove(GameMove move) {
        log.info("in handleMove move: {}", move);

        return connect4Service.addMove(move);
    }

    /*
    @MessageMapping("/move")
    @SendTo("/game_backend/move")
    public GameState handleMove(GameState gameStatus) {
        log.info("in handleMove gameStatus: {}", gameStatus);
        return new GameState(gameStatus.getMessage() + "+1");
    }*/
}