package org.gamebackend.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMove {
    private String gameId;
    private String playerId;
    private String value;
}