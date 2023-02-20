package game;

import game.player.HumanGUIPlayer;
import game.player.Player;
import game.player.ai.MiniMaxAlphaBetaPlayer;
import game.player.ai.MiniMaxPlayer;

import java.awt.*;

public class GameConfig {
    //    public static Player PLAYER_A = new HumanGUIPlayer("RED", Color.RED);
    // MiniMaxAlphaBetaPlayer // HumanGUIPlayer
    public static int CURRENT_DEPTH = 1;
    public static double MAX_TURN_TIME = 0.5;
    public static Player PLAYER_A = new MiniMaxAlphaBetaPlayer("RED", Color.RED);
    public static Player PLAYER_B = new HumanGUIPlayer("BLUE", Color.BLUE);
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
        PLAYER_A.setDepth(1);
        PLAYER_B.setDepth(1);
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
