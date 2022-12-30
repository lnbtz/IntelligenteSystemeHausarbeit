package game.player;

import game.GameState;
import game.GameConfig;

import java.awt.*;

public interface Player {

    int nextColumn(GameState gameState);

    Color getColor();

    String getName();

    default Player getNextPlayer() {
        return GameConfig.getNextPlayer(this);
    }
}
