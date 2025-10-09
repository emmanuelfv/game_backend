package org.gamebackend.websocket.handler;

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
public class GameWebSocketHandler {
    private Map<String, UserModel> activeGames = new ConcurrentHashMap<>();

    @MessageMapping("/join")
    @SendTo("/game/status")
    public GameStatus handleJoin(UserModel player) {
        log.info("in handleMove player: {}", player);

        return new GameStatus("Jugador conectado: " + player.getName());
    }

    @MessageMapping("/move")
    @SendTo("/game/{gameId}")
    public GameMove handleMove(@DestinationVariable String gameId, GameMove move) {
        log.info("in handleMove gameId: {}, move: {}", gameId, move);
        return move;
    }
}