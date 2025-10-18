package org.gamebackend.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameLogin {
    private String userName;
    private String token;
    private String gameType;
    private String gameId;
    private String playerId;
    private String turn;
    private String errorMessage;
}


