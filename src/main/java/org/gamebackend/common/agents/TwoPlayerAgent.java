package org.gamebackend.common.agents;

import org.gamebackend.common.games.TwoPlayerGame;
import org.gamebackend.websocket.model.GameState;

public interface TwoPlayerAgent {
    static final String AGENT_NAME = "agent";
    TwoPlayerGame game = null;
    String playerId = null;
    public void setGameState(GameState gs);
    public int makeMove();

}
