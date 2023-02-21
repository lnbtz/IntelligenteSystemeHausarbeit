package game;

import game.player.Player;
import game.player.ai.MiniMaxAlphaBetaPlayer;

import java.awt.*;

public class GameConfig {
    //    public static Player PLAYER_A = new HumanGUIPlayer("RED", Color.RED);
    // MiniMaxAlphaBetaPlayer // HumanGUIPlayer
    public static double MAX_TURN_TIME = 0.025;
    public static Player PLAYER_A = new MiniMaxAlphaBetaPlayer("RED", Color.RED);
    public static Player PLAYER_B = new MiniMaxAlphaBetaPlayer("BLUE", Color.BLUE);
    private static final Player DEFAULT_STARTING_PLAYER = PLAYER_B;
    public static boolean RANDOM_STARTING_PLAYER = true;

    public static Player getNextPlayer(Player player) {
        if (player == PLAYER_A) {
            return PLAYER_B;
        } else {
            return PLAYER_A;
        }
    }

    public static Player getRandomStartingPlayer() {
        PLAYER_A.setCurrentMaxDepth(1);
        PLAYER_B.setCurrentMaxDepth(1);
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
