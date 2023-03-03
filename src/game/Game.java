package game;

import game.player.HumanGUIPlayer;
import game.player.Player;
import game.player.ai.AIPlayer;
import game.player.ai.evaluation.Evaluation;
import visualization.ConnectFourVisualization;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game {
    public static final int NUM_OF_ROWS = 6, NUM_OF_COLUMNS = 7;

    static GameState gameState;
    static Player currentPlayer;
    static ConnectFourVisualization vis;

    static List<AIPlayer> play() {
        boolean draw = true;
        currentPlayer = GameConfig.getRandomStartingPlayer();
        System.out.println("starting Player: " + currentPlayer.getName());
        gameState = new GameState(NUM_OF_ROWS, NUM_OF_COLUMNS, currentPlayer);
        vis = new ConnectFourVisualization("Connect Four", NUM_OF_ROWS, NUM_OF_COLUMNS, currentPlayer.getColor());
        vis.start();
        int turnCounter = 0;
        while (!gameState.isBoardFull()) {
//            System.out.println(currentPlayer.getName() + "'s turn.");
            int newPieceColumn;
            if (currentPlayer instanceof HumanGUIPlayer) {
                newPieceColumn = vis.nextClickedColumn();
            } else {
                long time = System.nanoTime();
                newPieceColumn = currentPlayer.nextColumn(gameState);
                System.out.printf(" (%.5f seconds)\n", (System.nanoTime() - time) / 1_000_000_000.0);
            }

//            System.out.println();

            int newPieceRow = gameState.insertPiece(newPieceColumn, currentPlayer);

            vis.colorPiece(newPieceRow, newPieceColumn,
                    currentPlayer.getColor(), currentPlayer.getNextPlayer().getColor());
            turnCounter++;
            if (gameState.checkPlayerWon()) {
                draw = false;
                break;
            }

            currentPlayer = currentPlayer.getNextPlayer();
        }
        System.out.println("game took " + turnCounter + " turns");

        if (draw) {
            System.out.println("It's a draw!");
        } else {
            System.out.println(currentPlayer.getName() + " wins!");
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        GameConfig.setEvaluation(new Evaluation(15, 5, 3));
        play();
        GameConfig.setEvaluation(new Evaluation(37, 4, 82));
        play();
    }
}
