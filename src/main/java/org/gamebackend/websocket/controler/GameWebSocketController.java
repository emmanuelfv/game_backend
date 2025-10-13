package org.gamebackend.websocket.controler;

import lombok.extern.slf4j.Slf4j;
import org.gamebackend.login.model.UserModel;
import org.gamebackend.websocket.model.GameMove;
import org.gamebackend.websocket.model.GameStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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

    /*
    @MessageMapping("/join")
    @SendTo("/game_backend/join")
    public GameStatus handleJoin(UserModel player) {
        log.info("in handleMove player: {}", player);

        return new GameStatus("Jugador conectado: " + player.getName());
    }*/
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