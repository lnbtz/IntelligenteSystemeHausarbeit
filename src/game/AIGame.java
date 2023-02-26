package game;

import game.player.Player;
import game.player.ai.AIPlayer;
import game.player.ai.evaluation.Evaluation;
import game.player.ai.MiniMaxAlphaBetaPlayer;

import java.util.*;

public class AIGame {
    public static final int NUM_OF_ROWS = 6, NUM_OF_COLUMNS = 7;

    static GameState gameState;
    static Player currentPlayer;

    public AIGame(Player player1, Player player2) {
        GameConfig.PLAYER_A = player1;
        GameConfig.PLAYER_B = player2;
    }

    public void setStartingPlayer(Player startingPlayer) {
        currentPlayer = startingPlayer;
    }

    public void play() {
        GameConfig.PLAYER_A.setCurrentMaxDepth(1);
        GameConfig.PLAYER_B.setCurrentMaxDepth(1);
        boolean draw = true;
        if (currentPlayer == null) currentPlayer = GameConfig.getRandomStartingPlayer();
        gameState = new GameState(NUM_OF_ROWS, NUM_OF_COLUMNS, currentPlayer);
        int turnCounter = 0;
        while (!gameState.isBoardFull()) {
            int newPieceColumn;
//            long time = System.nanoTime();
            newPieceColumn = currentPlayer.nextColumn(gameState);
//            System.out.printf(" (%.5f seconds)\n", (System.nanoTime() - time) / 1_000_000_000.0);

            gameState.insertPiece(newPieceColumn, currentPlayer);
            turnCounter++;
            if (gameState.checkPlayerWon()) {
                draw = false;
                break;
            }
            currentPlayer = currentPlayer.getNextPlayer();
        }
//        System.out.println("game took " + turnCounter + " turns");
        if (draw) {
//            System.out.println("It's a draw!");
            ((AIPlayer) currentPlayer).incrementDraws();
            ((AIPlayer) currentPlayer.getNextPlayer()).incrementDraws();
        } else {
            System.out.println(currentPlayer.getName() + " wins!");
            ((AIPlayer) currentPlayer).incrementWins();
            ((AIPlayer) currentPlayer.getNextPlayer()).incrementLosses();
        }
    }

    public static void main(String[] args) {
        AIPlayer player1 = new MiniMaxAlphaBetaPlayer("AB Player 1");
        AIPlayer player2 = new MiniMaxAlphaBetaPlayer("AB Player 2");
        player1.setEvaluation(new Evaluation(300, 300, 300));
        player2.setEvaluation(new Evaluation(0, 0, 0));
        AIGame game = new AIGame(player1, player2);
        game.setStartingPlayer(player1);
        game.play();
    }
}
