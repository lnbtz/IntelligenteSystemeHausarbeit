package game;

import game.player.Player;
import game.player.ai.MiniMaxAlphaBetaPlayer;

import java.awt.*;

public class GameConfig {
    //    public static Player PLAYER_A = new HumanGUIPlayer("RED", Color.RED);
    // MiniMaxAlphaBetaPlayer // HumanGUIPlayer
    public static Player PLAYER_A = new MiniMaxAlphaBetaPlayer("RED", Color.RED);
    public static Player PLAYER_B = new MiniMaxAlphaBetaPlayer("BLUE", Color.BLUE);
    //    public static Player PLAYER_B = new MiniMaxPlayer("BLUE", Color.BLUE);
    private static final Player DEFAULT_STARTING_PLAYER = PLAYER_A;

    public static boolean RANDOM_STARTING_PLAYER = false;

    public static Player getNextPlayer(Player player) {
        if (player == PLAYER_A) {
            return PLAYER_B;
        } else {
            return PLAYER_A;
        }
    }

    public static Player getRandomStartingPlayer() {
        if (RANDOM_STARTING_PLAYER) {
            double rand = Math.random();

            if (rand < 0.5) {
                return PLAYER_A;
            } else {
                return PLAYER_B;
            }
        } else return DEFAULT_STARTING_PLAYER;
    }
}
