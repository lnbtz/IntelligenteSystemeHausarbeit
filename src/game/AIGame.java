package game;

import game.player.Player;
import game.player.ai.AIPlayer;

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

        while (!gameState.isBoardFull()) {
            int newPieceColumn;
//            long time = System.nanoTime();
            newPieceColumn = currentPlayer.nextColumn(gameState);
//            System.out.printf(" (%.5f seconds)\n", (System.nanoTime() - time) / 1_000_000_000.0);

            gameState.insertPiece(newPieceColumn, currentPlayer);

            if (gameState.checkPlayerWon()) {
                draw = false;
                break;
            }
            currentPlayer = currentPlayer.getNextPlayer();
        }
        if (draw) {
            System.out.println("It's a draw!");
            ((AIPlayer) currentPlayer).incrementDraws();
            ((AIPlayer) currentPlayer.getNextPlayer()).incrementDraws();
        } else {
            System.out.println(currentPlayer.getName() + " wins!");
            ((AIPlayer) currentPlayer).incrementWins();
            ((AIPlayer) currentPlayer.getNextPlayer()).incrementLosses();
        }
    }
}
